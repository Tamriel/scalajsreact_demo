package app

import app.BeforeNext.Before
import app.DataModel.{Tree, TreeItem}
import monocle.macros.GenLens
import monocle.function.At.at
import monocle.function.Index.index
import monocle.macros.syntax.lens._
import DataModel.ROOTID

sealed trait BeforeNext
object BeforeNext {
  case object Before extends BeforeNext
  case object Next extends BeforeNext
}

case class SimpleDatabase(tree: Tree,
                          selected: Option[String] = None,
                          editing: Option[String] = None) {

  private val itemsLens = GenLens[SimpleDatabase](_.tree.items)
  private val textLens = GenLens[TreeItem](_.text)
  private val childrenLens = GenLens[TreeItem](_.childrenIds)

  def isEditing(itemId: String): Boolean = editing.contains(itemId)

  def getItem(id: String) = tree.items(id)

  def select(beforeNext: BeforeNext): SimpleDatabase = select(getItem(selected.get), beforeNext)

  def select(item: TreeItem): SimpleDatabase = select(item.id)

  def select(id: String): SimpleDatabase = copy(selected = Some(id))

  private def select(fromItem: TreeItem, beforeNext: BeforeNext): SimpleDatabase = {
    val parent = getItem(fromItem.parentId)
    val diff = if (beforeNext == Before) -1 else +1
    val newIndex = parent.indexOf(fromItem) + diff
    if (newIndex < 0) {
      if (parent.id == ROOTID) this
      else select(parent)
    } else if (newIndex >= parent.childrenIds.length) {
      if (fromItem.id == ROOTID) this
      // if has children and expanded: select first child
      else if (fromItem.expanded && fromItem.childrenIds.nonEmpty) select(fromItem.childrenIds(0))
      // select next below parent
      else select(parent, beforeNext)
    } else select(parent.childrenIds(newIndex))
  }

  def expand: SimpleDatabase =
    selected match {
      case Some(id) =>
        val item = getItem(id)
        if (item.expanded) this
        else toggleExpanded(item)
      case _ => this // todo: cleaner
    }

  def collapse: SimpleDatabase =
    selected match {
      case Some(id) =>
        val item = getItem(id)
        if (item.expanded) toggleExpanded(item)
        else this
      case _ => this
    }

  def toggleExpanded(item: TreeItem): SimpleDatabase = {
    def toggle(itemO: Option[TreeItem]): Option[TreeItem] =
      Some(itemO.get.lens(_.expanded).set(!itemO.get.expanded))
    (itemsLens composeLens at(item.id)).modify(toggle)(this)
  }

  def setText(item: TreeItem, newText: String): SimpleDatabase = {
    def modifyText(itemO: Option[TreeItem]): Option[TreeItem] =
      Some(textLens.set(newText)(itemO.get))
    (itemsLens composeLens at(item.id)).modify(modifyText)(this)
  }

  def deleteItem(item: TreeItem): SimpleDatabase = {
    val newDatabase = (itemsLens composeLens at(item.id)).set(None)(this)
    val parent = getItem(item.parentId)

    def setChild(i: Option[TreeItem]): Option[TreeItem] =
      Some((childrenLens composeOptional index(parent.indexOf(item))).set("aa")(i.get)) // todo
    (itemsLens composeLens at(item.parentId)).modify(setChild)(newDatabase)
  }

  def addSibling(item: TreeItem): SimpleDatabase = {
    val parent = getItem(item.parentId)
    val ownPos = parent.indexOf(item)
    addChild(parent, ownPos + 1)
  }

  def addChild(parentItem: TreeItem, position: Int): SimpleDatabase = {
    val newItem = TreeItem(parentId = parentItem.id)
    val newDatabase = (itemsLens composeLens at(newItem.id)).set(Some(newItem))(this)

    def setChild(i: Option[TreeItem]): Option[TreeItem] =
      Some((childrenLens composeOptional index(position)).set(newItem.id)(i.get)) // Todo: keine auswirkung
    (itemsLens composeLens at(parentItem.id)).modify(setChild)(newDatabase)
  }

//  def moveUp(id: String): Tree = ???
//      Some((childrenLens composeOptional index(position)).set("1")(item.get))
// def moveDown(id: String): Tree = ???
//
//  def moveLeft(id: String): Tree = ???
//
//  def moveRight(id: String): Tree = ???
}
