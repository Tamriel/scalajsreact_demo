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

  def expand(): SimpleDatabase =
    selected match {
      case Some(id) =>
        val item = getItem(id)
        if (item.expanded) this
        else toggleExpanded(item)
      case _ => this // todo: cleaner
    }

  def collapse(): SimpleDatabase =
    selected match {
      case Some(id) =>
        val item = getItem(id)
        if (item.expanded) toggleExpanded(item)
        else this
      case _ => this
    }

  def toggleExpanded(item: TreeItem): SimpleDatabase =
    this.modify(_.tree.items.at(item.id).expanded).setTo(!item.expanded)

  def setText(item: TreeItem, newText: String): SimpleDatabase =
    this.modify(_.tree.items.at(item.id).text).setTo(newText)

  def deleteItem(): SimpleDatabase = {
    val item = getItem(selected.get)
    val result = this.modify(_.tree.items).using(_.filter(_._2 != item))
    result.modify(_.tree.items.at(item.parentId).childrenIds).using(_.filter(_ != item.id))
  }

  def addSibling(): SimpleDatabase = {
    val item = getItem(selected.get)
    val parent = getItem(item.parentId)
    val ownPos = parent.indexOf(item)
    addChild(parent, ownPos + 1)
  }

  def addChild(): SimpleDatabase = addChild(getItem(selected.get), 0)

  def addChild(parentItem: TreeItem, position: Int): SimpleDatabase = {
    val newItem = TreeItem(parentId = parentItem.id)
    val result = this.modify(_.tree.items).using(_ + (newItem.id -> newItem))
    def insert(vector: Vector[String]) = {
      val splitted = vector.splitAt(position)
      splitted._1 ++ Vector(newItem.id) ++ splitted._2
    }
    result.modify(_.tree.items.at(parentItem.id).childrenIds).using(insert).select(newItem)
  }

//  def moveUp(id: String): Tree = ???
//      Some((childrenLens composeOptional index(position)).set("1")(item.get))
// def moveDown(id: String): Tree = ???
//
//  def moveLeft(id: String): Tree = ???
//
//  def moveRight(id: String): Tree = ???
}

case object SimpleDatabase {
  def exampleDatabase: SimpleDatabase = {
    val rootItem = TreeItem(id = ROOTID)
    val emptyDatabase = SimpleDatabase(Tree(Map(ROOTID -> rootItem)))
    emptyDatabase.addChild(rootItem, 0).addChild(rootItem, 1).addChild(rootItem, 2)
  }
}
