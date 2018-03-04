package app

import app.BeforeNext.{Before, Next}
import app.DataModel.ItemType.{DoneTask, Note, Task}
import app.DataModel.{ItemId, ROOTID}
import app.Util._
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.html_<^._
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
        if (instruction.completed) CSS.checkedCheckBox else CSS.checkBox,
        CSS.iconLi,
        CSS.veryLightGrey.when(instruction.completed),
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
          <.div(topMarginH6("Ansehen"),
                awesomeUl(comp(ins.upDown), comp(ins.right), comp(ins.left))),
          <.div(topMarginH6("Bearbeiten"),
                awesomeUl(comp(ins.tabEdit), comp(ins.clickEdit), comp(ins.create))),
          <.div(topMarginH6("Strukturieren"),
                awesomeUl(comp(ins.moveVertically), comp(ins.moveRight), comp(ins.moveLeft)))
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
      val db = props.stateSnap.value
      val item = db.tree.items(props.itemId)

      val children = item.childrenIds.toVdomArray(childId =>
        TreeItemComponent.withKey(childId.toString)(props.copy(itemId = childId)))

      if (item.id == db.currentRootOfView) {
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

        def zoom(e: ReactEvent) = {
          e.stopPropagation()
          mod(_.zoomInto(item.id))
        }

        def toggleType(e: ReactEvent) = {
          e.stopPropagation()
          mod(_.toggleType(item))
        }

        val editing = db.isEditing(item.id)
        val expandIcon =
          if (item.isProject)
            <.i(CSS.project, CSS.pointer, CSS.expandIcon, ^.onClick ==> zoom)
          else if (item.childrenIds.nonEmpty)
            <.i(if (item.expanded) CSS.angleDown else CSS.angleRight,
                CSS.pointer,
                CSS.expandIcon,
                ^.onClick ==> toggleExpanded)
          else <.i(CSS.expandIcon)

        <.div(
          <.div(
            CSS.row,
            if (db.selected == item.id) CSS.selected else CSS.hover,
            ^.onDoubleClick --> mod(_.startEditing(item)),
            ^.onClick --> mod(_.select(item)),
            expandIcon,
            <.i(
              CSS.taskIcon,
              CSS.lightGrey.when(item.itemType == DoneTask),
              CSS.checkedCheckBox.when(item.itemType == DoneTask),
              CSS.checkBox.when(item.itemType == Task),
              CSS.pointer,
              CSS.centerVertically,
              ^.onClick ==> toggleType
            ).when(item.itemType != Note),
            <.span(
              CSS.lightGrey.when(item.itemType == DoneTask),
              CSS.centerVertically,
              CSS.invisible.when(editing),
              CSS.semiBold.when(item.isProject),
              CSS.marginBeforeText,
              item.text
            ),
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
      val rootItem =
        TreeItemComponent.withKey(db.currentRootOfView.toString)(Props(snap, db.currentRootOfView))

      def handleKey(e: ReactKeyboardEvent): Callback = {
        def plainKey: CallbackOption[Unit] = // CallbackOption will stop if a key isn't matched
          CallbackOption.keyCodeSwitch(e) {
            case KeyCode.Delete | KeyCode.Backspace => snap.modState(_.deleteItem())
            case KeyCode.Enter                      => snap.modState(_.addSibling().startEditing())
            case KeyCode.Space                      => snap.modState(_.toggleType())
            case KeyCode.Up                         => snap.modState(_.select(Before))
            case KeyCode.Down                       => snap.modState(_.select(Next))
            case KeyCode.Left                       => snap.modState(_.collapseOrJumpUp())
            case KeyCode.Right                      => snap.modState(_.expandOrSelectChild())
            case KeyCode.W                          => snap.modState(_.moveUp())
            case KeyCode.S                          => snap.modState(_.moveDown())
            case KeyCode.A                          => snap.modState(_.moveLeft())
            case KeyCode.D                          => snap.modState(_.moveRight())
            case KeyCode.P                          => snap.modState(_.toggleProject())
            case KeyCode.Tab | KeyCode.F2           => snap.modState(_.startEditing())
            case KeyCode.Escape                     => snap.modState(x => x.zoomInto(ROOTID))
          }

        def shiftKey: CallbackOption[Unit] =
          CallbackOption.keyCodeSwitch(e, shiftKey = true) {
            case KeyCode.Enter => snap.modState(_.addChild().startEditing())
          }

        def ctrlKey: CallbackOption[Unit] =
          CallbackOption.keyCodeSwitch(e, ctrlKey = true) {
            case KeyCode.I     => snap.modState(x => x.addFromPlainText(x.selectedItem.text))
            case KeyCode.Enter => snap.modState(x => x.zoomInto(x.selected))
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
        CSS.noOutline,
        CSS.columns,
        ^.tabIndex := 0, // needs to be focusable to receive key presses
        ^.onKeyDown ==> handleKey,
        <.div(
          ^.className := "column col-5 col-xl-6 col-ml-auto",
          ^.paddingRight := "2em",
          ^.paddingLeft := "1em",
          ManualComponent(db.instructions)
        ),
        <.div(
          ^.className := "column col-5 col-xl-6 col-mr-auto",
          ^.paddingLeft := "2em",
          <.div(<.h5("Beispielbaum"),
                <.div(CSS.treeDiv, Breadcrumbs.BreadcrumbsComponent(snap), rootItem))
        )
      ).ref(mainDivRef = _)
    }
  }

  private val MainComponent = ScalaComponent
    .builder[Unit]("TreeNote")
    .initialState(SimpleDatabase.simpleDatabase)
    .renderBackend[MainBackend]
    .componentDidMount(_.backend.focus)
    // The mainDiv needs to have focus to capture keys. So when the user is not editing: focus it
    .componentDidUpdate(x => x.backend.focus.when(x.currentState.editing.isEmpty).void)
    .build

  def apply() = MainComponent()
}
