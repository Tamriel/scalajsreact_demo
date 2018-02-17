package app

import app.BeforeNext.{Before, Next}
import app.DataModel.ROOTID
import io.circe.generic.auto._
import io.circe.syntax._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{Callback, ScalaComponent, _}
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html

import scalacss.ScalaCssReact._

object MainComponent {

  case class Props(stateSnap: StateSnapshot[SimpleDatabase], itemId: String)

  private val TreeItemComponent = ScalaComponent
    .builder[Props]("TreeItem")
    .renderBackend[TreeItemBackend]
    .componentDidMount(x => x.backend.focusInput(x.props))
    .componentDidUpdate(x => x.backend.focusInput(x.currentProps, Some(x.prevProps)))
    .build

  class TreeItemBackend($ : BackendScope[Props, Unit]) {
    var inputRef: html.Input = _

    def focusInput(props: Props, prevProps: Option[Props] = None) = Callback {
      val db = props.stateSnap.value
      if (db.isEditing(props.itemId) &&
          // don't focus, if previous Props was editing
          (prevProps.isEmpty || !prevProps.get.stateSnap.value.isEditing(props.itemId))) {
        inputRef.focus()
        val end = db.getItem(props.itemId).text.length
        inputRef.setSelectionRange(end, end)
      }
    }

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

        def editFieldKeyDown(e: ReactKeyboardEvent): Callback =
          CallbackOption.keyCodeSwitch(e) {
            case KeyCode.Escape | KeyCode.Enter => mod(_.copy(editing = None))
          }

        val editing = snap.isEditing(item.id)
        val expandSymbol = if (item.expanded) "▼" else "▶"
        <.li(
          <.div(
            if (snap.selected.contains(item.id)) CSS.selected else CSS.hover,
            <.span(expandSymbol, CSS.pointer, ^.onClick --> mod(_.toggleExpanded(item)))
              .when(item.childrenIds.nonEmpty),
            <.div(
              CSS.invisible.when(editing),
              <.p("Platzhaltertext | " + item.text,
                  ^.onDoubleClick --> mod(_.startEditing(item)),
                  ^.onClick --> mod(_.select(item)))
            ),
            <.input(
              CSS.input,
              CSS.invisible.unless(editing),
              ^.value := item.text,
              ^.onChange ==> updateText,
              ^.onKeyDown ==> editFieldKeyDown,
              ^.onBlur --> mod(_.copy(editing = None))
            ).ref(inputRef = _)
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

      def handleKey(e: ReactKeyboardEvent): Callback = {
        def plainKey: CallbackOption[Unit] = // CallbackOption will stop if a key isn't matched
          CallbackOption.keyCodeSwitch(e) {
            case KeyCode.Delete | KeyCode.Backspace => snap.modState(_.deleteItem())
            case KeyCode.Enter                      => snap.modState(_.addSibling().startEditing())
            case KeyCode.Up                         => snap.modState(_.select(Before))
            case KeyCode.Down                       => snap.modState(_.select(Next))
            case KeyCode.Left                       => snap.modState(_.collapseOrJumpUp())
            case KeyCode.Right                      => snap.modState(_.expandOrSelectChild())
            case KeyCode.W                          => snap.modState(_.moveUp())
            case KeyCode.S                          => snap.modState(_.moveDown())
            case KeyCode.A                          => snap.modState(_.moveLeft())
            case KeyCode.D                          => snap.modState(_.moveRight())
            case KeyCode.Tab | KeyCode.F2           => snap.modState(_.startEditing())
          }

        def ctrlKey: CallbackOption[Unit] =
          CallbackOption.keyCodeSwitch(e, shiftKey = true) {
            case KeyCode.Enter => snap.modState(_.addChild().startEditing())
          }

        if (db.editing.isEmpty)
          (plainKey orElse ctrlKey) >> e.preventDefaultCB
        else Callback()
      }

      <.div(
        CSS.maximize,
        ^.tabIndex := 0, // needs to be focusable to receive key presses
        ^.onKeyDown ==> handleKey,
        rootItem
//        <.button(^.onClick --> Callback(println(snap.value.asJson)),
//                 "Print tree as JSON to developer console")
      ).ref(mainDivRef = _)
    }
  }

  private val Component = ScalaComponent
    .builder[Unit]("TreeNote")
    .initialState(SimpleDatabase.exampleDatabase)
    .renderBackend[MainBackend]
    .componentDidMount(_.backend.init)
    .componentDidUpdate(x => x.backend.init.when(x.currentState.editing.isEmpty).void)
    .build

  def apply(): Unmounted[Unit, SimpleDatabase, MainBackend] = Component()
}
