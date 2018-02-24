package app

import app.BeforeNext.{Before, Next}
import app.DataModel.{ItemId, ROOTID}
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{Callback, ScalaComponent, _}
import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html

import scalacss.ScalaCssReact._

object MainComponent {

  case class Props(stateSnap: StateSnapshot[SimpleDatabase], itemId: ItemId)

  /** Displays a single instruction / a task with a checkbox in front. Gets checked and greyed out when accomplished. */
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

  /** Displays all instructions in a list with a few headings. */
  private val ManualComponent = ScalaComponent
    .builder[Instructions]("Manual")
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
    // when the user creates an item, it shall get focused and get scrolled into the visible area
    .componentDidMount(x => x.backend.focusInput(x.props) >> x.backend.scrollIntoView(x.props))
    // do the same when the user starts editing (which is equal to "do it, unless he was editing before")
    .componentDidUpdate(x =>
      (x.backend.focusInput(x.currentProps) >> x.backend.scrollIntoView(x.currentProps))
        .unless_(x.prevProps.stateSnap.value.isEditing(x.currentProps.itemId)))
    .build

  class TreeItemBackend($ : BackendScope[Props, Unit]) {
    var inputRef: html.TextArea = _
    var rowRef: html.Element = _

    /** Focus the input and sets the cursor position to the end of the text of the edited item.*/
    def focusInput(props: Props) = Callback {
      val db = props.stateSnap.value
      if (db.isEditing(props.itemId)) {
        inputRef.focus()
        val textLength = db.getItem(props.itemId).text.length
        inputRef.setSelectionRange(textLength, textLength)
      }
    }

    /** Scroll to selection if it's outside the viewport. */
    def scrollIntoView(props: Props) = Callback {
      val db = props.stateSnap.value
      if (db.selected == props.itemId && rowRef != null) {
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
        TreeItemComponent.withKey(childId.toString)(props.copy(itemId = childId)))

      if (item.id == ROOTID) {
        <.ul(CSS.maximize, children) // the root item is invisible, just show its children
      } else {
        def mod(fn: SimpleDatabase => SimpleDatabase): Callback = props.stateSnap.modState(fn)

        def updateText(e: ReactEventFromInput) =
          mod(_.setText(item, e.target.value))

        def editFieldKeyDown(e: ReactKeyboardEvent): Callback =
          CallbackOption.keyCodeSwitch(e) {
            case KeyCode.Escape | KeyCode.Enter | KeyCode.Tab =>
              mod(_.completeEdit()) >> e.preventDefaultCB
          }

        def toggleExpanded(e: ReactEvent) = {
          // when clicking the toggle arrow, the underlying item gets selected due to the click handler in the div
          // prevent that:
          e.stopPropagation()
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
            if (snap.selected == item.id) CSS.selected else CSS.hover,
            ^.onDoubleClick --> mod(_.startEditing(item)),
            ^.onClick --> mod(_.select(item)),
            expandIcon,
            <.span(CSS.centerVertically,
                   CSS.invisible.when(editing),
                   CSS.marginTextToIcon,
                   item.text),
            <.textarea(
              CSS.centerVertically,
              CSS.input,
              CSS.invisible.unless(editing),
              ^.value := item.text,
              ^.onChange ==> updateText,
              ^.onKeyDown ==> editFieldKeyDown,
              ^.onBlur --> mod(_.completeEdit()) // onBlur is called when clicking outside the textarea / input
            ).ref(inputRef = _)
          ).ref(rowRef = _),
          <.ul(CSS.ulMargins, children).when(item.expanded)
        )
      }
    }
  }

  class MainBackend($ : BackendScope[Unit, SimpleDatabase]) {
    private var mainDivRef: html.Element = _

    def focus: Callback =
      Callback {
        val x = dom.window.pageXOffset.toInt // Disable scrolling when focusing
        val y = dom.window.pageYOffset.toInt
        mainDivRef.focus()
        dom.window.scrollTo(x, y)
      }

    def render(db: SimpleDatabase): VdomElement = {
      val snap = StateSnapshot(db).setStateVia($)
      val rootItem = TreeItemComponent.withKey(ROOTID.toString)(Props(snap, ROOTID))

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

        def shiftKey: CallbackOption[Unit] =
          CallbackOption.keyCodeSwitch(e, shiftKey = true) {
            case KeyCode.Enter => snap.modState(_.addChild().startEditing())
          }

        def ctrlKey: CallbackOption[Unit] =
          CallbackOption.keyCodeSwitch(e, ctrlKey = true) {
            case KeyCode.I => snap.modState(x => x.addFromPlainText(x.selectedItem.text))
            case KeyCode.P =>
              Callback {
                println(snap.value.toJson)
                println(snap.value.toPlainText)
              }
          }

        if (db.editing.isEmpty)
          (plainKey orElse shiftKey orElse ctrlKey) >> e.preventDefaultCB
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
            // "col-5 col-xl-6" results in a width of 5, but a width of 6 on screens < 1280px
            // "col-ml-auto" means 'margin-left: auto' and results in right alignment
            ^.className := "column col-5 col-xl-6 col-ml-auto",
            ^.paddingRight := "2em",
            ^.paddingLeft := "1em",
            ManualComponent(db.instructions)
          ),
          <.div(^.className := "column col-5 col-xl-6 col-mr-auto",
                ^.paddingLeft := "2em",
                <.div(<.h5("Beispielbaum"), <.div(CSS.treeDiv, rootItem)))
        )
      ).ref(mainDivRef = _) // set reference (doc: https://github.com/japgolly/scalajs-react/blob/master/doc/REFS.md )
    }
  }

  private val Component = ScalaComponent
    .builder[Unit]("TreeNote")
    .initialState(SimpleDatabase.exampleDatabase)
    .renderBackend[MainBackend]
    .componentDidMount(_.backend.focus)
    // The mainDiv needs to have focus to capture keys. So when the user is not editing: focus it
    .componentDidUpdate(x => x.backend.focus.when(x.currentState.editing.isEmpty).void)
    .build

  def apply(): Unmounted[Unit, SimpleDatabase, MainBackend] = Component()
}
