package app

import app.BeforeNext.Before
import app.MainComponent.{Tree, TreeItem}
import monocle.macros.GenLens
import monocle.function.At.at
import monocle.function.Index.index
import monocle.macros.syntax.lens._

sealed trait BeforeNext
object BeforeNext {
  case object Before extends BeforeNext
  case object Next extends BeforeNext
}

case class SimpleDatabase(tree: Tree,
                          selected: Option[String] = None,
                          editing: Option[String] = None) {

  val itemsLens = GenLens[SimpleDatabase](_.tree.items)
  val textLens = GenLens[TreeItem](_.text)
  val childrenLens = GenLens[TreeItem](_.childrenIds)

  def isEditing(itemId: String): Boolean = editing.contains(itemId)

  def getItem(id: String) = tree.items(id)

  def select(beforeNext: BeforeNext): SimpleDatabase = {
    val selectedItem = getItem(selected.get)
    val parent = getItem(selectedItem.parentId)
    val diff = if (beforeNext == Before) -1 else +1
    val newIndex = parent.indexOf(selectedItem) + diff
    if (newIndex < 0 || newIndex >= parent.childrenIds.length) this
    else this.copy(selected = Some(parent.childrenIds(newIndex)))
  }

  def insertCharacter(item: TreeItem, pos: Int, char: String): SimpleDatabase = {
    val (beginning, end) = item.text.splitAt(pos)
    setText(item, beginning + char + end)
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
