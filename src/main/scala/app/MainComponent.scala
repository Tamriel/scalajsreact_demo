package app

import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{Callback, ScalaComponent, _}
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.html_<^._
import scala.util.Random

object MainComponent {

  case class TreeItem(text: String = "",
                      id: String = Random.alphanumeric.take(10).mkString,
                      parentId: String = "is set later",
                      childrenIds: Vector[String] = Vector.empty,
                      deleted: Boolean = false) {
    def indexOf(item: TreeItem): Int = childrenIds.indexOf(item.id)
  }

  case class Tree(items: Map[String, TreeItem])

  val ROOTID = "root"

  case class Props(stateSnap: StateSnapshot[SimpleDatabase], itemId: String)

  val TreeItemComponent = ScalaComponent
    .builder[Props]("TreeItem")
    .renderBackend[TreeItemBackend]
    .build

  class TreeItemBackend($ : BackendScope[Props, Unit]) {
    def render(props: Props): VdomElement = {
      val item = props.stateSnap.value.tree.items(props.itemId)

      def mod(fn: SimpleDatabase => SimpleDatabase): Callback =
        props.stateSnap.modState(fn)

      def updateText(e: ReactEventFromInput) =
        mod(_.setText(item, e.target.value))

      val children = item.childrenIds.toVdomArray(childId =>
        TreeItemComponent.withKey(childId)(props.copy(itemId = childId)))

      if (item.id == ROOTID)
        <.ul(children) // the root item is invisible, just show its children
      else {
        <.li(
          <.input.text(^.value := item.text, ^.onChange ==> updateText),
          <.button(^.onClick --> mod(_.deleteItem(item)), "✖️"),
          <.button(^.onClick --> mod(_.addSibling(item)), "➕"),
          <.button(^.onClick --> mod(_.addChild(item, item.childrenIds.length)),
                   "➕➡"),
//              <.button(^.onClick --> mod(_.moveUp(item)), "⬆️"),
//              <.button(^.onClick --> mod(_.moveDown(item)), "⬇️"),
//              <.button(^.onClick --> mod(_.moveLeft(item)), "⬅️"),
//              <.button(^.onClick --> mod(_.moveRight(item)), "➡️"),
          <.ul(children)
        )
      }
    }
  }

  class MainBackend($ : BackendScope[Unit, SimpleDatabase]) {
    def render(db: SimpleDatabase): VdomElement = {
      val rootItem = TreeItemComponent.withKey(ROOTID)(
        Props(StateSnapshot(db).setStateVia($), ROOTID))
      <.div(
        <.p("Tree:"),
        rootItem
      )
    }
  }
  val exampleTree = {
    val child1 = TreeItem("1")
    val child21 = TreeItem("2.1")
    val child2 = TreeItem("2", childrenIds = Vector(child21.id))
    val root = TreeItem(id = ROOTID, childrenIds = Vector(child1.id, child2.id))
    Tree(
      Map(child1.id -> child1,
          child21.id -> child21,
          child2.id -> child2,
          root.id -> root))
  }

  val rootItem = TreeItem(id = ROOTID)
  val emptyDatabase = SimpleDatabase(Tree(Map(ROOTID -> rootItem)))
  val exampleDatabase =
    emptyDatabase.addChild(rootItem, 0).addChild(rootItem, 1)

  val Component = ScalaComponent
    .builder[Unit]("TreeNote")
    .initialState(SimpleDatabase(exampleTree))
    .renderBackend[MainBackend]
    .build

  def apply() = Component()
}
