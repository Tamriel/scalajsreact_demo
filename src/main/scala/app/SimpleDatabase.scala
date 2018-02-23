package app

import java.util.UUID

import app.BeforeNext.{Before, Next}
import app.DataModel.{ItemId, ROOTID, Tree, TreeItem}
import com.softwaremill.quicklens._
import io.circe.{KeyDecoder, KeyEncoder}
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

sealed trait BeforeNext
object BeforeNext {
  case object Before extends BeforeNext
  case object Next extends BeforeNext
}

case class SimpleDatabase(tree: Tree,
                          selected: Option[ItemId] = None,
                          editing: Option[ItemId] = None,
                          instructions: Instructions = Instructions(),
                          lastSelectDirection: BeforeNext = Before) {

  def isEditing(itemId: ItemId): Boolean = editing.contains(itemId)

  def startEditing(): SimpleDatabase =
    if (selected.isDefined)
      copy(editing = Some(selected.get)).modify(_.instructions.tabEdit.completed).setTo(true)
    else this

  def startEditing(item: TreeItem): SimpleDatabase =
    copy(editing = Some(item.id)).modify(_.instructions.clickEdit.completed).setTo(true)

  def completeEdit(): SimpleDatabase = copy(editing = None)

  def getParent(item: TreeItem): TreeItem = getItem(item.parentId)

  def getItem(id: ItemId): TreeItem = tree.items(id)

  def rootItem: TreeItem = getItem(ROOTID)

  def select(beforeNext: BeforeNext): SimpleDatabase = select(getItem(selected.get), beforeNext)

  def select(item: TreeItem): SimpleDatabase = select(item.id)

  def select(id: ItemId): SimpleDatabase =
    copy(selected = Some(id)).modify(_.instructions.upDown.completed).setTo(true)

  private def select(fromItem: TreeItem, beforeNext: BeforeNext): SimpleDatabase = {
    val parent = getParent(fromItem)
    val diff = if (beforeNext == Before) -1 else +1
    val newIndex = parent.indexOf(fromItem) + diff
    // if has children and expanded: select first child
    if (beforeNext == Next && fromItem.expanded && fromItem.childrenIds.nonEmpty)
      select(fromItem.childrenIds.head).copy(lastSelectDirection = Next)
    else if (newIndex < 0) {
      if (parent.id == ROOTID) this // don't change selection if at top
      else select(parent).copy(lastSelectDirection = Before)
    } else if (newIndex >= parent.childrenIds.length) {
      if (parent.id == ROOTID) this // don't change selection if at bottom
      else {
        def selectNextParentFromChild(parentItem: TreeItem, lastChild: TreeItem): SimpleDatabase =
          parentItem.childrenIds.lift(parentItem.indexOf(lastChild) + 1) match {
            case Some(childId) => select(childId).copy(lastSelectDirection = Next)
            case None          => selectNextParentFromChild(getParent(parentItem), parentItem)
          }
        selectNextParentFromChild(getParent(parent), parent)
      }
    } else {
      val newId = parent.childrenIds(newIndex)
      if (beforeNext == Next) select(newId).copy(lastSelectDirection = Next)
      else {
        def selectBeforeChildFromParent(id: ItemId): SimpleDatabase = {
          val beforeItem = getItem(id)
          if (beforeItem.expanded && beforeItem.childrenIds.nonEmpty)
            selectBeforeChildFromParent(beforeItem.childrenIds.last)
          else select(beforeItem).copy(lastSelectDirection = Before)
        }
        selectBeforeChildFromParent(newId)
      }
    }
  }

  def expandOrSelectChild(): SimpleDatabase =
    selected match {
      case Some(id) =>
        val item = getItem(id)
        if (item.childrenIds.nonEmpty) {
          if (!item.expanded)
            toggleExpanded(item).modify(_.instructions.right.completed).setTo(true)
          else select(Next)
        } else this
      case _ => this // todo: cleaner
    }

  def collapseOrJumpUp(): SimpleDatabase =
    selected match {
      case Some(id) =>
        val item = getItem(id)
        val parent = getParent(item)
        if (item.childrenIds.nonEmpty && item.expanded) toggleExpanded(item)
        else if (parent.id != ROOTID)
          select(parent).modify(_.instructions.left.completed).setTo(true)
        else this
      case _ => this
    }

  def toggleExpanded(item: TreeItem): SimpleDatabase = setExpanded(item, !item.expanded)

  def setExpanded(item: TreeItem, expanded: Boolean): SimpleDatabase =
    this.modify(_.tree.items.at(item.id).expanded).setTo(expanded)

  def setText(item: TreeItem, newText: String): SimpleDatabase =
    this.modify(_.tree.items.at(item.id).text).setTo(newText)

  def deleteItem(): SimpleDatabase = {
    val item = getItem(selected.get)
    val res0 = select(Before).modify(_.tree.items).using(_.filter(_._2 != item))
    val res1 = res0.deleteId(item.parentId, item.id)
    val res2 = res1.modify(_.instructions.delete.completed).setTo(true)
    if (res2.selected.get == item.id) // if not changed due to being the top item
      res2.select(item, Next)
    else res2
  }

  private def deleteId(parentId: ItemId, id: ItemId): SimpleDatabase =
    this.modify(_.tree.items.at(parentId).childrenIds).using(_.filter(_ != id))

  def addSibling(): SimpleDatabase =
    if (rootItem.childrenIds.isEmpty) addChild(rootItem, 0)
    else {
      val item = getItem(selected.get)
      val parent = getParent(item)
      val ownPos = parent.indexOf(item)
      addChild(parent, ownPos + 1).modify(_.instructions.create.completed).setTo(true)
    }

  def addChild(): SimpleDatabase =
    if (rootItem.childrenIds.isEmpty) addChild(rootItem, 0)
    else addChild(getItem(selected.get), 0).modify(_.instructions.createChild.completed).setTo(true)

  def addChild(parentItem: TreeItem, position: Int): SimpleDatabase = {
    val newItem = TreeItem(parentItem.id)
    val res0 = this.modify(_.tree.items).using(_ + (newItem.id -> newItem))
    val res1 = res0.insertId(parentItem.id, position, newItem.id).select(newItem)
    res1.setExpanded(parentItem, expanded = true).copy(lastSelectDirection = Next)
  }

  private def insertId(parentId: ItemId, position: Int, id: ItemId): SimpleDatabase = {
    def insert(vector: Vector[ItemId]) = {
      val splitted = vector.splitAt(position)
      splitted._1 ++ Vector(id) ++ splitted._2
    }
    val res = this.modify(_.tree.items.at(parentId).childrenIds).using(insert)
    res.modify(_.tree.items.at(id).parentId).setTo(parentId)
  }

  def moveUp(): SimpleDatabase = moveVertically(Before)

  def moveDown(): SimpleDatabase = moveVertically(Next).copy(lastSelectDirection = Next)

  private def moveVertically(beforeNext: BeforeNext): SimpleDatabase = {
    val item = getItem(selected.get)
    val parent = getParent(item)
    val diff = if (beforeNext == Before) -1 else +1
    val newPosition = parent.indexOf(item) + diff
    if (newPosition >= 0 && newPosition <= parent.childrenIds.length) {
      val res0 = deleteId(parent.id, item.id)
      val res1 = res0.insertId(item.parentId, newPosition, item.id)
      res1.modify(_.instructions.moveVertically.completed).setTo(true)
    } else this
  }

  def moveLeft(): SimpleDatabase = {
    val item = getItem(selected.get)
    val parent = getParent(item)
    if (parent.id != ROOTID) {
      val newParent = getParent(parent)
      val newPosition = newParent.indexOf(parent) + 1
      val res0 = deleteId(parent.id, item.id)
      val res1 = res0.insertId(newParent.id, newPosition, item.id)
      res1.modify(_.instructions.moveLeft.completed).setTo(true)
    } else this
  }

  def moveRight(): SimpleDatabase = {
    val item = getItem(selected.get)
    val parent = getParent(item)
    val currentPosition = parent.indexOf(item)
    if (currentPosition != 0) {
      val newParent = getItem(parent.childrenIds(currentPosition - 1))
      val res0 = deleteId(parent.id, item.id)
      val res1 = res0.insertId(newParent.id, newParent.childrenIds.length, item.id)
      val res2 = res1.setExpanded(newParent, expanded = true)
      res2.modify(_.instructions.moveRight.completed).setTo(true)
    } else this
  }

  def toJson: String = {
    // https://circe.github.io/circe/codec.html#custom-key-types
    implicit val itemIdKeyEncoder: KeyEncoder[ItemId] = (item: ItemId) => item.id.toString
    this.asJson.toString()
  }
}

case object SimpleDatabase {
  def exampleDatabase: SimpleDatabase = {
    implicit val itemIdKeyDecoder: KeyDecoder[ItemId] =
      (key: String) => Some(ItemId(UUID.fromString(key)))
    decode[SimpleDatabase](ExampleDatabase.json).right.get
  }
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
      "Wandere mit den Pfeiltasten <kbd><i class=\"fas fa-arrow-up fa-xs\"></i></kbd> und <kbd><i class=\"fas fa-arrow-down fa-xs\"></i></kbd> durch die Einträge."),
    right: Instruction = Instruction(
      "Klappe mit <kbd><i class=\"fas fa-arrow-right fa-xs\"></i></kbd> den selektierten Eintrag aus."),
    left: Instruction = Instruction(
      "Springe mit <kbd><i class=\"fas fa-arrow-left fa-xs\"></i></kbd> zum Eltern-Eintrag."),
    tabEdit: Instruction = Instruction("Bearbeite den Text, indem du <kbd>Tab</kbd> drückst."),
    clickEdit: Instruction = Instruction("Oder indem du einen Eintrag doppelklickst."),
    create: Instruction = Instruction("Erstelle einen Eintrag mit <kbd>Enter</kbd>."),
    createChild: Instruction = Instruction(
      "Erstelle einen Unter-Eintrag mit <kbd>Shift</kbd> + <kbd>Enter</kbd>."),
    delete: Instruction = Instruction("Lösche den selektierten Eintrag mit <kbd>Entf</kbd>."),
    moveVertically: Instruction = Instruction(
      "Bewege den selektierten Eintrag mit <kbd>W</kbd> und <kbd>S</kbd> nach oben und unten."),
    moveRight: Instruction = Instruction(
      "<kbd>D</kbd> verschiebt den selektierten Eintrag eine Ebene tiefer, in diese Richtung <i class=\"fas fa-hand-point-right\"></i>."),
    moveLeft: Instruction = Instruction(
      "<kbd>A</kbd> verschiebt den selektierten Eintrag eine Ebene höher, in diese Richtung <i class=\"fas fa-hand-point-left\"></i>."))
