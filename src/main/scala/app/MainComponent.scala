package app

import app.BeforeNext.{Before, Next}
import app.DataModel.{Tree, TreeItem}
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{Callback, ScalaComponent, _}
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html
import DataModel.ROOTID

import scalacss.ScalaCssReact._

object MainComponent {

  case class Props(stateSnap: StateSnapshot[SimpleDatabase], itemId: String)

  private val TreeItemComponent = ScalaComponent
    .builder[Props]("TreeItem")
    .renderBackend[TreeItemBackend]
    .componentDidUpdate(x =>
      Callback {
        if (x.currentProps.stateSnap.value.isEditing(x.currentProps.itemId))
          x.raw.backend.inputRef.focus()
    })
    .build

  class TreeItemBackend($ : BackendScope[Props, Unit]) {
    var inputRef: html.Input = _

    def render(props: Props): VdomElement = {
      val snap = props.stateSnap.value
      val item = snap.tree.items(props.itemId)

      val children = item.childrenIds.toVdomArray(childId =>
        TreeItemComponent.withKey(childId)(props.copy(itemId = childId)))

      if (item.id == ROOTID)
        <.ul(CSS.maximize, children) // the root item is invisible, just show its children
      else {
        def mod(fn: SimpleDatabase => SimpleDatabase): Callback = props.stateSnap.modState(fn)

        def updateText(e: ReactEventFromInput) =
          mod(_.setText(item, e.target.value))

        val editFieldKeyDown: ReactKeyboardEvent => Option[Callback] =
          e => {
            e.nativeEvent.keyCode match {
              case KeyCode.Escape | KeyCode.Enter => Some(mod(_.copy(editing = None)))
              case _                              => None
            }
          }

        val editing = snap.isEditing(item.id)
        val expandSymbol = if (item.expanded) "▼" else "▶"
        <.li(
          <.div(
            CSS.selected.when(snap.selected.contains(item.id)),
            <.span(expandSymbol, CSS.pointer, ^.onClick --> mod(_.toggleExpanded(item)))
              .when(item.childrenIds.nonEmpty),
            <.div(
              CSS.invisible.when(editing),
              <.label(item.text,
                      ^.onDoubleClick --> mod(_.copy(editing = Some(item.id))),
                      ^.onClick --> mod(_.select(item))),
              <.button(^.onClick --> mod(_.deleteItem(item)), "✖️"),
              <.button(^.onClick --> mod(_.addSibling(item)), "➕"),
              <.button(^.onClick --> mod(_.addChild(item, item.childrenIds.length)), "➕➡")
//              <.button(^.onClick --> mod(_.moveUp(item)), "⬆️"),
//              <.button(^.onClick --> mod(_.moveDown(item)), "⬇️"),
//              <.button(^.onClick --> mod(_.moveLeft(item)), "⬅️"),
//              <.button(^.onClick --> mod(_.moveRight(item)), "➡️"),
            ),
            <.input(CSS.invisible.unless(editing),
                    ^.value := item.text,
                    ^.onChange ==> updateText,
                    ^.onKeyDown ==>? editFieldKeyDown)
              .ref(inputRef = _)
          ),
          <.ul(children).when(item.expanded)
        )
      }
    }
  }

  class MainBackend($ : BackendScope[Unit, SimpleDatabase]) {
    private var mainDivRef: html.Element = _

    def init: Callback = Callback(mainDivRef.focus())

    def render(db: SimpleDatabase): VdomElement = {
      val snap = StateSnapshot(db).setStateVia($)
      val rootItem =
        TreeItemComponent.withKey(ROOTID)(Props(snap, ROOTID))

      val keyDown: ReactKeyboardEvent => Option[Callback] =
        e => {
          e.nativeEvent.keyCode match {
            case KeyCode.Up    => Some(snap.modState(_.select(Before)))
            case KeyCode.Down  => Some(snap.modState(_.select(Next)))
            case KeyCode.Left  => Some(snap.modState(_.collapse))
            case KeyCode.Right => Some(snap.modState(_.expand))
            case _             => None
          }
        }

      <.div(CSS.maximize,
            ^.tabIndex := 0, // needs to be focusable to receive key presses
            ^.onKeyDown ==>? keyDown,
            rootItem)
        .ref(mainDivRef = _)
    }
  }
  private val uglyExampleDatabase = {
    val child1 = TreeItem("1", parentId = ROOTID)
    val child21 = TreeItem("2.1", parentId = "2")
    val child2 = TreeItem("2", id = "2", childrenIds = Vector(child21.id), parentId = ROOTID)
    val root = TreeItem(id = ROOTID, childrenIds = Vector(child1.id, child2.id))
    val tree = Tree(
      Map(child1.id -> child1, child21.id -> child21, child2.id -> child2, root.id -> root))
    SimpleDatabase(tree).select(child1)
  }

  val rootItem = TreeItem(id = ROOTID)
  private val emptyDatabase = SimpleDatabase(Tree(Map(ROOTID -> rootItem))).select("todo")
  private val exampleDatabase = emptyDatabase.addChild(rootItem, 0).addChild(rootItem, 1)

  private val Component = ScalaComponent
    .builder[Unit]("TreeNote")
    .initialState(uglyExampleDatabase)
    .renderBackend[MainBackend]
    .componentDidMount(_.backend.init)
    .build

  def apply(): Unmounted[Unit, SimpleDatabase, MainBackend] = Component()
}
