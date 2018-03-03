package app

import java.util.UUID

import app.BeforeNext.{Before, Next}
import app.DataModel.ItemType.{DoneTask, Note, Task}
import app.DataModel.{ItemId, ItemType, ROOTID, Tree, TreeItem}
import com.softwaremill.quicklens._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.circe.{KeyDecoder, KeyEncoder}

sealed trait BeforeNext { def value: Int }
object BeforeNext {
  case object Before extends BeforeNext { val value = -1 }
  case object Next extends BeforeNext { val value = +1 }
}

/** @param selected is the Id of the currently selected item.
  * @param editing is None if the user does not edit an item. If he edits an item, it is set to the id of the item.
  * @param instructions is a list of textual instructions. They are marked as completed on special events.
  *
  * To modify deeply nested fields of immutable objects, we use a lens library:
  * http://www.warski.org/blog/2015/02/quicklens-modify-deeply-nested-case-class-fields/ */
case class SimpleDatabase(tree: Tree,
                          selected: ItemId = ROOTID,
                          editing: Option[ItemId] = None,
                          instructions: Instructions = Instructions(),
                          lastSelectDirection: BeforeNext = Before) {

  def isEditing(itemId: ItemId): Boolean = editing.contains(itemId)

  def startEditing(): SimpleDatabase =
    copy(editing = Some(selected)).modify(_.instructions.tabEdit.completed).setTo(true)

  def startEditing(item: TreeItem): SimpleDatabase =
    copy(editing = Some(item.id)).modify(_.instructions.clickEdit.completed).setTo(true)

  def completeEdit(): SimpleDatabase = copy(editing = None)

  def getParent(item: TreeItem): TreeItem =
    if (item.id == ROOTID)
      throw new Exception("Could not get parent of the root item, since it has no parent.")
    else getItem(item.parentId)

  def getItem(id: ItemId): TreeItem = tree.items(id)

  def rootItem: TreeItem = getItem(ROOTID)

  def selectedItem: TreeItem = getItem(selected)

  def select(item: TreeItem): SimpleDatabase = select(item.id)

  def select(id: ItemId): SimpleDatabase =
    copy(selected = id).modify(_.instructions.upDown.completed).setTo(true)

  def select(beforeNext: BeforeNext): SimpleDatabase =
    select(selectedItem, beforeNext).getOrElse(this)

  /** Returns true if the selection was successful. */
  private def select(fromItem: TreeItem, beforeNext: BeforeNext): Option[SimpleDatabase] = {
    val parent = getParent(fromItem)
    val newIndex = parent.indexOf(fromItem) + beforeNext.value
    // if has children and expanded: select first child
    if (beforeNext == Next && fromItem.expanded && fromItem.childrenIds.nonEmpty)
      Some(select(fromItem.childrenIds.head).copy(lastSelectDirection = Next))
    else if (newIndex < 0) {
      if (parent.id == ROOTID) None // don't change selection if at top
      else Some(select(parent).copy(lastSelectDirection = Before))
    } else if (newIndex >= parent.childrenIds.length) {
      if (parent.id == ROOTID) None
      else {
        def selectNextParentFromChild(parentItem: TreeItem,
                                      lastChild: TreeItem): Option[SimpleDatabase] =
          // get child if it exists, else try one level up
          parentItem.childrenIds.lift(parentItem.indexOf(lastChild) + 1) match {
            case Some(childId) => Some(select(childId).copy(lastSelectDirection = Next))
            case None =>
              if (parentItem.id == ROOTID) None // don't change selection if at bottom
              else selectNextParentFromChild(getParent(parentItem), parentItem)
          }
        selectNextParentFromChild(getParent(parent), parent)
      }
    } else {
      val newId = parent.childrenIds(newIndex)
      if (beforeNext == Next) Some(select(newId).copy(lastSelectDirection = Next))
      else {
        def selectBeforeChildFromParent(id: ItemId): SimpleDatabase = {
          val beforeItem = getItem(id)
          if (beforeItem.expanded && beforeItem.childrenIds.nonEmpty)
            selectBeforeChildFromParent(beforeItem.childrenIds.last)
          else select(beforeItem).copy(lastSelectDirection = Before)
        }
        Some(selectBeforeChildFromParent(newId))
      }
    }
  }

  def expandOrSelectChild(): SimpleDatabase =
    if (selectedItem.childrenIds.nonEmpty) {
      if (!selectedItem.expanded)
        toggleExpanded(selectedItem).modify(_.instructions.right.completed).setTo(true)
      else select(Next)
    } else this

  def collapseOrJumpUp(): SimpleDatabase = {
    val parent = getParent(selectedItem)
    if (selectedItem.childrenIds.nonEmpty && selectedItem.expanded) toggleExpanded(selectedItem)
    else if (parent.id != ROOTID)
      select(parent).modify(_.instructions.left.completed).setTo(true)
    else this
  }

  def toggleExpanded(item: TreeItem): SimpleDatabase = setExpanded(item, !item.expanded)

  def setExpanded(item: TreeItem, expanded: Boolean): SimpleDatabase =
    this.modify(_.tree.items.at(item.id).expanded).setTo(expanded)

  def setText(item: TreeItem, newText: String): SimpleDatabase =
    this.modify(_.tree.items.at(item.id).text).setTo(newText)

  def toggleType(): SimpleDatabase = toggleType(selectedItem)

  def toggleType(item: TreeItem): SimpleDatabase = item.itemType match {
    case Note     => setType(item, Task)
    case Task     => setType(item, DoneTask)
    case DoneTask => setType(item, Note)
  }

  def setType(item: TreeItem, newItemType: ItemType): SimpleDatabase =
    this.modify(_.tree.items.at(item.id).itemType).setTo(newItemType)

  def deleteItem(): SimpleDatabase = {
    // if selection is not successful due to being the top item: select the next item
    val res0 = select(selectedItem, Before) match {
      case Some(db) => db
      case None     => select(Next)
    }
    val res1 = res0.modify(_.tree.items).using(_.filter(_._2 != selectedItem))
    res1.deleteId(selectedItem.parentId, selectedItem.id)
  }

  private def deleteId(parentId: ItemId, id: ItemId): SimpleDatabase =
    this.modify(_.tree.items.at(parentId).childrenIds).using(_.filter(_ != id))

  def addSibling(): SimpleDatabase =
    if (rootItem.childrenIds.isEmpty) addChild(rootItem, 0)
    else {
      val parent = getParent(selectedItem)
      val ownPos = parent.indexOf(selectedItem)
      addChild(parent, ownPos + 1).modify(_.instructions.create.completed).setTo(true)
    }

  def addChild(): SimpleDatabase =
    if (rootItem.childrenIds.isEmpty) addChild(rootItem, 0)
    else addChild(selectedItem, 0)

  def addChild(parentItem: TreeItem, position: Int): SimpleDatabase =
    addChild(parentItem, position, "")

  def addChild(parentItem: TreeItem, position: Int, text: String): SimpleDatabase = {
    val newItem = TreeItem(parentItem.id, text = text)
    val res0 = this.modify(_.tree.items).using(_ + (newItem.id -> newItem))
    val res1 = res0.insertId(parentItem.id, position, newItem.id).select(newItem)
    res1.setExpanded(parentItem, expanded = true).copy(lastSelectDirection = Next)
  }

  private def insertId(parentId: ItemId, position: Int, id: ItemId): SimpleDatabase = {
    val res = this.modify(_.tree.items.at(parentId).childrenIds).using(_.insert(position, id))
    res.modify(_.tree.items.at(id).parentId).setTo(parentId)
  }

  def moveUp(): SimpleDatabase = moveVertically(Before)

  def moveDown(): SimpleDatabase = moveVertically(Next).copy(lastSelectDirection = Next)

  private def moveVertically(beforeNext: BeforeNext): SimpleDatabase = {
    val parent = getParent(selectedItem)
    val newPosition = parent.indexOf(selectedItem) + beforeNext.value
    if (newPosition >= 0 && newPosition <= parent.childrenIds.length) {
      val res0 = deleteId(parent.id, selectedItem.id)
      val res1 = res0.insertId(selectedItem.parentId, newPosition, selectedItem.id)
      res1.modify(_.instructions.moveVertically.completed).setTo(true)
    } else this
  }

  def moveLeft(): SimpleDatabase = {
    val parent = getParent(selectedItem)
    if (parent.id != ROOTID) {
      val newParent = getParent(parent)
      val newPosition = newParent.indexOf(parent) + 1
      val res0 = deleteId(parent.id, selectedItem.id)
      val res1 = res0.insertId(newParent.id, newPosition, selectedItem.id)
      res1.modify(_.instructions.moveLeft.completed).setTo(true)
    } else this
  }

  def moveRight(): SimpleDatabase = {
    val parent = getParent(selectedItem)
    val currentPosition = parent.indexOf(selectedItem)
    if (currentPosition != 0) {
      val newParent = getItem(parent.childrenIds(currentPosition - 1))
      val res0 = deleteId(parent.id, selectedItem.id)
      val res1 = res0.insertId(newParent.id, newParent.childrenIds.length, selectedItem.id)
      val res2 = res1.setExpanded(newParent, expanded = true)
      res2.modify(_.instructions.moveRight.completed).setTo(true)
    } else this
  }

  def toJson: String = {
    // https://circe.github.io/circe/codec.html#custom-key-types
    implicit val itemIdKeyEncoder: KeyEncoder[ItemId] = (item: ItemId) => item.id.toString
    this.asJson.toString()
  }

  /** Creates a plaintext version of the tree. Items get indented with tabs. */
  def toPlainText: String = {
    def itemString(item: TreeItem, level: Int): String = {
      val buf = new StringBuilder
      buf ++= "\t" * level + "- " + item.text + "\n"
      for (childId <- item.childrenIds) {
        buf ++= itemString(tree.items(childId), level + 1)
      }
      buf.toString()
    }
    itemString(rootItem, 0)
  }

  /** Takes a plaintext tree (where the items are indented with tabs) and adds them to the database. */
  def addFromPlainText(text: String): SimpleDatabase = addFromPlainText(text.split('\n').toList)

  private def addFromPlainText(lines: List[String]): SimpleDatabase =
    // New items are inserted from top to bottom.
    lines match {
      case line :: remainingLines =>
        val strippedLine = line.dropWhile(_.toString == "\t")
        val indention = line.length - strippedLine.length
        // get the bottom-most item for a specific indention
        def getLowestItemOfIndention(currentItem: TreeItem, indention: Int): TreeItem =
          indention match {
            case 0 => currentItem
            case i => getLowestItemOfIndention(getItem(currentItem.childrenIds.last), i - 1)
          }
        val parent = getLowestItemOfIndention(rootItem, indention)
        val index = parent.childrenIds.length
        val res = addChild(parent, index, strippedLine.stripPrefix("- "))
        res.addFromPlainText(remainingLines)
      case _ => this
    }
}

case object SimpleDatabase {

  /** Loads a previously saved JSON version of the tree. */
  def exampleDatabase: SimpleDatabase = {
    implicit val itemIdKeyDecoder: KeyDecoder[ItemId] =
      (key: String) => Some(ItemId(UUID.fromString(key)))
    decode[SimpleDatabase](ExampleDatabase.json).right.get
  }

  /** Creates a new database with just one item. */
  def simpleDatabase: SimpleDatabase = {
    // for the parent UUID of the root exists no TreeItem, so getting the root parent item will throw NoSuchElementException
    val rootsParentId = ItemId(UUID.fromString("f52ac4f6-0aca-47f8-b9cc-f4a89599b005"))
    val rootItem = TreeItem(rootsParentId, ROOTID)
    SimpleDatabase(Tree(Map(rootItem.id -> rootItem))).addChild(rootItem, 0)
  }
}

case class Instruction(text: String, completed: Boolean = false)
case class Instructions(
    upDown: Instruction = Instruction(
      "Wandere mit den Pfeiltasten <kbd><i style=\"font-size: 12px;\" class=\"icon-up\"></i></kbd> und <kbd><i style=\"font-size: 12px;\" class=\"icon-down\"></i></kbd> durch die Einträge"),
    right: Instruction = Instruction(
      "Klappe mit <kbd><i style=\"font-size: 12px;\" class=\"icon-right\"></i></kbd> den selektierten Eintrag aus"),
    left: Instruction = Instruction(
      "Springe mit <kbd><i style=\"font-size: 12px;\" class=\"icon-left\"></i></kbd> zum Eltern-Eintrag"),
    tabEdit: Instruction = Instruction("Bearbeite den Text, indem du <kbd>Tab</kbd> drückst"),
    clickEdit: Instruction = Instruction("Oder indem du einen Eintrag doppelklickst"),
    create: Instruction = Instruction("Erstelle einen Eintrag mit <kbd>Enter</kbd>"),
    moveVertically: Instruction = Instruction(
      "Bewege den selektierten Eintrag mit <kbd>W</kbd> und <kbd>S</kbd> nach oben und unten"),
    moveRight: Instruction = Instruction(
      "<kbd>D</kbd> verschiebt den selektierten Eintrag eine Ebene tiefer, in diese Richtung <i class=\"icon-right-hand\"></i>"),
    moveLeft: Instruction = Instruction(
      "<kbd>A</kbd> verschiebt den selektierten Eintrag eine Ebene höher, in diese Richtung <i class=\"icon-left-hand\"></i>"))
