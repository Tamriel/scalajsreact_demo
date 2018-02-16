package app

import app.BeforeNext.{Before, Next}
import app.DataModel.{ROOTID, Tree, TreeItem}
import com.softwaremill.quicklens._

sealed trait BeforeNext
object BeforeNext {
  case object Before extends BeforeNext
  case object Next extends BeforeNext
}

case class SimpleDatabase(tree: Tree,
                          selected: Option[String] = None,
                          editing: Option[String] = None) {

  def isEditing(itemId: String): Boolean = editing.contains(itemId)

  def startEditing(): SimpleDatabase =
    if (selected.isDefined) startEditing(getItem(selected.get)) else this

  def startEditing(item: TreeItem): SimpleDatabase = copy(editing = Some(item.id))

  def getItem(id: String) = tree.items(id)

  def select(beforeNext: BeforeNext): SimpleDatabase = select(getItem(selected.get), beforeNext)

  def select(item: TreeItem): SimpleDatabase = select(item.id)

  def select(id: String): SimpleDatabase = copy(selected = Some(id))

  private def select(fromItem: TreeItem, beforeNext: BeforeNext): SimpleDatabase = {
    val parent = getItem(fromItem.parentId)
    val diff = if (beforeNext == Before) -1 else +1
    val newIndex = parent.indexOf(fromItem) + diff
    // if has children and expanded: select first child
    if (beforeNext == Next && fromItem.expanded && fromItem.childrenIds.nonEmpty)
      select(fromItem.childrenIds.head)
    else if (newIndex < 0) {
      if (parent.id == ROOTID) this // don't change selection if at top
      else select(parent)
    } else if (newIndex >= parent.childrenIds.length) {
      if (parent.id == ROOTID) this // don't change selection if at bottom
      else {
        def selectNextParentFromChild(parentId: String, lastChild: TreeItem): SimpleDatabase = {
          val parentItem = getItem(parentId)
          parentItem.childrenIds.lift(parentItem.indexOf(lastChild) + 1) match {
            case Some(childId) => select(childId)
            case None          => selectNextParentFromChild(parentItem.parentId, parentItem)
          }
        }
        selectNextParentFromChild(parent.parentId, parent)
      }
    } else {
      val newId = parent.childrenIds(newIndex)
      if (beforeNext == Next) select(newId)
      else {
        def selectBeforeChildFromParent(id: String): SimpleDatabase = {
          val beforeItem = getItem(id)
          if (beforeItem.expanded && beforeItem.childrenIds.nonEmpty)
            selectBeforeChildFromParent(beforeItem.childrenIds.last)
          else select(beforeItem)
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
          if (!item.expanded) toggleExpanded(item)
          else select(Next)
        } else this
      case _ => this // todo: cleaner
    }

  def collapseOrJumpUp(): SimpleDatabase =
    selected match {
      case Some(id) =>
        val item = getItem(id)
        if (item.childrenIds.nonEmpty && item.expanded) toggleExpanded(item)
        else if (item.parentId != ROOTID) select(item.parentId)
        else this
      case _ => this
    }

  def toggleExpanded(item: TreeItem): SimpleDatabase =
    this.modify(_.tree.items.at(item.id).expanded).setTo(!item.expanded)

  def setText(item: TreeItem, newText: String): SimpleDatabase =
    this.modify(_.tree.items.at(item.id).text).setTo(newText)

  def deleteItem(): SimpleDatabase = {
    val item = getItem(selected.get)
    val res0 = select(Before).modify(_.tree.items).using(_.filter(_._2 != item))
    val res1 = res0.deleteId(item.parentId, item.id)
    if (res1.selected.get == item.id) // if not changed due to being the top item
      res1.select(item, Next)
    else res1
  }

  private def deleteId(parentId: String, id: String): SimpleDatabase =
    this.modify(_.tree.items.at(parentId).childrenIds).using(_.filter(_ != id))

  def addSibling(): SimpleDatabase =
    if (getItem(ROOTID).childrenIds.isEmpty) addChild(getItem(ROOTID), 0)
    else {
      val item = getItem(selected.get)
      val parent = getItem(item.parentId)
      val ownPos = parent.indexOf(item)
      addChild(parent, ownPos + 1)
    }

  def addChild(): SimpleDatabase =
    if (getItem(ROOTID).childrenIds.isEmpty) addChild(getItem(ROOTID), 0)
    else addChild(getItem(selected.get), 0)

  def addChild(parentItem: TreeItem, position: Int): SimpleDatabase = {
    val newItem = TreeItem()
    val result = this.modify(_.tree.items).using(_ + (newItem.id -> newItem))
    result.insertId(parentItem.id, position, newItem.id).select(newItem)
  }

  private def insertId(parentId: String, position: Int, id: String): SimpleDatabase = {
    def insert(vector: Vector[String]) = {
      val splitted = vector.splitAt(position)
      splitted._1 ++ Vector(id) ++ splitted._2
    }
    val res = this.modify(_.tree.items.at(parentId).childrenIds).using(insert)
    res.modify(_.tree.items.at(id).parentId).setTo(parentId)
  }

  def moveUp(): SimpleDatabase = moveVertically(Before)

  def moveDown(): SimpleDatabase = moveVertically(Next)

  private def moveVertically(beforeNext: BeforeNext): SimpleDatabase = {
    val item = getItem(selected.get)
    val parent = getItem(item.parentId)
    val diff = if (beforeNext == Before) -1 else +1
    val newPosition = parent.indexOf(item) + diff
    if (newPosition >= 0 && newPosition <= parent.childrenIds.length) {
      val res = deleteId(parent.id, item.id)
      res.insertId(item.parentId, newPosition, item.id)
    } else this
  }

  def moveLeft(): SimpleDatabase = {
    val item = getItem(selected.get)
    val parent = getItem(item.parentId)
    if (parent.id != ROOTID) {
      val newParent = getItem(parent.parentId)
      val newPosition = newParent.indexOf(parent) + 1
      val res = deleteId(parent.id, item.id)
      res.insertId(newParent.id, newPosition, item.id)
    } else this
  }

  def moveRight(): SimpleDatabase = {
    val item = getItem(selected.get)
    val parent = getItem(item.parentId)
    val currentPosition = parent.indexOf(item)
    if (currentPosition != 0) {
      val newParent = getItem(parent.childrenIds(currentPosition - 1))
      val res = deleteId(parent.id, item.id)
      res.insertId(newParent.id, 0, item.id)
    } else this
  }
}

case object SimpleDatabase {
  def exampleDatabase: SimpleDatabase = {
    val rootItem = TreeItem(id = ROOTID)
    val emptyDatabase = SimpleDatabase(Tree(Map(ROOTID -> rootItem)))
    emptyDatabase.addChild(rootItem, 0).addChild(rootItem, 1).addChild(rootItem, 2)
  }
}
