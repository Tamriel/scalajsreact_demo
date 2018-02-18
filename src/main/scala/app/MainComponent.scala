package app

import app.BeforeNext.{Before, Next}
import app.DataModel.ROOTID
import io.circe.generic.auto._
import io.circe.syntax._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{Callback, ScalaComponent, _}
import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html

import scalacss.ScalaCssReact._

object MainComponent {

  case class Props(stateSnap: StateSnapshot[SimpleDatabase], itemId: String)

  private val InstructionComponent = ScalaComponent
    .builder[Instruction]("Instruction")
    .render_P { instruction =>
      <.li(
        CSS.veryLightGrey.when(instruction.completed),
        <.i(if (instruction.completed) CSS.checkSquare else CSS.square),
        <.span(^.dangerouslySetInnerHtml := " " + instruction.text)
      )
    }
    .build

  private val IntroComponent = ScalaComponent
    .builder[Instructions]("Intro")
    .render_P { ins =>
      def comp(instruction: Instruction) =
        InstructionComponent.withKey(instruction.text)(instruction)
      <.div(
        <.h5("Anleitung"),
        <.ul(
          ^.marginLeft := "0",
          <.div(<.h6(CSS.topMargin, "Ansehen"),
                <.ul(CSS.fontAwesomeUl, comp(ins.upDown), comp(ins.right), comp(ins.left))),
          <.div(<.h6(CSS.topMargin, "Bearbeiten"),
                <.ul(CSS.fontAwesomeUl, comp(ins.tabEdit), comp(ins.clickEdit))),
          <.div(<.h6(CSS.topMargin, "Hinzufügen"),
                <.ul(CSS.fontAwesomeUl, comp(ins.create), comp(ins.createChild), comp(ins.delete))),
          <.div(<.h6(CSS.topMargin, "Srukturieren"),
                <.ul(CSS.fontAwesomeUl,
                     comp(ins.moveVertically),
                     comp(ins.moveRight),
                     comp(ins.moveLeft)))
        )
      )
    }
    .build

  private val TreeItemComponent = ScalaComponent
    .builder[Props]("TreeItem")
    .renderBackend[TreeItemBackend]
    .componentDidMount(x => x.backend.focusInputAndScrollTo(x.props))
    .componentDidUpdate(x => x.backend.focusInputAndScrollTo(x.currentProps, Some(x.prevProps)))
    .build

  class TreeItemBackend($ : BackendScope[Props, Unit]) {
    var inputRef: html.Input = _
    var rowRef: html.Element = _

    def focusInputAndScrollTo(props: Props, prevPropsOpt: Option[Props] = None) = Callback {
      val db = props.stateSnap.value

      if (db.isEditing(props.itemId) &&
          (prevPropsOpt.isEmpty || !prevPropsOpt.get.stateSnap.value.isEditing(props.itemId))) {
        inputRef.focus()
        val end = db.getItem(props.itemId).text.length
        inputRef.setSelectionRange(end, end)
      }

      // scroll to selection if it's outside the viewport
      if (db.selected.contains(props.itemId) && rowRef != null) {
        val rect = rowRef.getBoundingClientRect()
        val isInViewport = rect.top.toInt >= 0 && rect.bottom.toInt <= dom.window.innerHeight
        if (!isInViewport) {
          db.lastSelectDirection match {
            case (Before) => // top of the element will be aligned to the top of the visible area
              rowRef.scrollIntoView(true)
            case Next => //  the bottom of the element will be aligned to the bottom of the visible area
              rowRef.scrollIntoView(false)
          }
        }
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
            case KeyCode.Escape | KeyCode.Enter | KeyCode.Tab =>
              mod(_.completeEdit()) >> e.preventDefaultCB
          }

        def toggleExpanded(e: ReactEvent) = {
          e.stopPropagation() // the select on click handler shall not be called
          mod(_.toggleExpanded(item))
        }

        val editing = snap.isEditing(item.id)
        val expandIcon =
          if (item.childrenIds.nonEmpty)
            <.i(if (item.expanded) CSS.angleDown else CSS.angleRight,
                CSS.pointer,
                CSS.centerVertically,
                CSS.icon,
                ^.onClick ==> toggleExpanded)
          else <.i(CSS.icon)
        <.div(
          <.div(
            CSS.row,
            if (snap.selected.contains(item.id)) CSS.selected else CSS.hover,
            ^.onDoubleClick --> mod(_.startEditing(item)),
            ^.onClick --> mod(_.select(item)),
            expandIcon,
            <.span(CSS.centerVertically,
                   CSS.invisible.when(editing),
                   CSS.marginTextToIcon,
                   item.text),
            <.input(
              CSS.centerVertically,
              CSS.input,
              CSS.invisible.unless(editing),
              ^.value := item.text,
              ^.onChange ==> updateText,
              ^.onKeyDown ==> editFieldKeyDown,
              ^.onBlur --> mod(_.completeEdit())
            ).ref(inputRef = _)
          ).ref(rowRef = _),
          <.ul(CSS.ulMargins, children).when(item.expanded)
        )
      }
    }
  }

  class MainBackend($ : BackendScope[Unit, SimpleDatabase]) {
    private var mainDivRef: html.Element = _

    def init: Callback =
      Callback { // The mainDiv needs focus to capture keys. But focus without scrolling.
        val x = dom.window.pageXOffset.toInt
        val y = dom.window.pageYOffset.toInt
        mainDivRef.focus()
        dom.window.scrollTo(x, y)
      }

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
        CSS.mainContainer,
        ^.tabIndex := 0, // needs to be focusable to receive key presses
        ^.onKeyDown ==> handleKey,
        <.div(
          CSS.columns,
          ^.paddingTop := "10px",
          ^.paddingBottom := "55px",
          <.div(^.className := "column col-5",
                <.img(^.src := "res/logo_violett_90px.png", ^.className := "float-right")),
          <.div(
            ^.className := "column col-4",
            <.h1("TreeNote",
                 ^.fontSize := "1.6rem",
                 ^.marginTop := ".05em",
                 ^.marginBottom := ".2em"),
            <.h2("Kollaboratives Wissens- und Projektmanagement",
                 ^.fontSize := ".9rem",
                 ^.fontWeight := "400")
          )
        ),
        <.div(
          CSS.columns,
          <.div(
            ^.className := "column col-5 col-xl-6 col-ml-auto", // width of 5, but width of 6 on screens < 1280px
            ^.paddingRight := "2em",
            ^.paddingLeft := "1em",
            IntroComponent(db.instructions)
          ),
          <.div(^.className := "column col-5 col-xl-6 col-mr-auto",
                ^.paddingLeft := "2em",
                <.div(<.h5("Beispielbaum"), <.div(CSS.treeDiv, rootItem)))
        )
//      <.button(^.onClick --> Callback(println(snap.value.asJson)),
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
