package app

import app.BeforeNext.{Before, Next}
import app.DataModel.ItemType.{DoneTask, Note, Task}
import app.DataModel.{ItemId, ROOTID}
import app.Util._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra._
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
                awesomeUl(comp(ins.upDown), comp(ins.right), comp(ins.left), comp(ins.zoom))),
          <.div(topMarginH6("Bearbeiten"),
                awesomeUl(comp(ins.tabEdit),
                          comp(ins.clickEdit),
                          comp(ins.create),
                          comp(ins.createTask))),
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
    .componentDidMount(x => x.backend.focusInput(x.props) >> x.backend.scrollInView(x.props))
    // do the same when the user starts editing (which is equal to "do it, unless he was editing before")
    .componentDidUpdate(x =>
      (x.backend.focusInput(x.currentProps) >> x.backend.scrollInView(x.currentProps))
        .unless_(x.prevProps.stateSnap.value.isEditing(x.currentProps.itemId)))
    .build

  class TreeItemBackend($ : BackendScope[Props, Unit]) {
    private val inputRef = Ref[html.TextArea]
    private val rowRef = Ref[html.Element]

    /** Focus the input and sets the cursor position to the end of the text of the edited item.*/
    def focusInput(props: Props): Callback = {
      val db = props.stateSnap.value
      if (db.isEditing(props.itemId)) {
        val textLength = db.getItem(props.itemId).text.length
//        inputRef.foreach(_.setSelectionRange(textLength, textLength)) >>
        inputRef.foreach(_.focus())
      } else Callback.empty
    }

    /** Scroll to selection if it's outside the viewport. */
    def scrollInView(props: Props): Callback = {
      val db = props.stateSnap.value
      if (db.selected == props.itemId && rowRef != null) {
        val rect = rowRef.unsafeGet.getBoundingClientRect()
        val isInViewport = rect.top.toInt >= 0 && rect.bottom.toInt <= dom.window.innerHeight
        if (!isInViewport) {
          db.lastSelectDirection match {
            case (Before) => // top of the element will be aligned to the top of the visible area
              rowRef.foreach(_.scrollIntoView(true))
            case Next => //  the bottom of the element will be aligned to the bottom of the visible area
              rowRef.foreach(_.scrollIntoView(false))
          }
        } else Callback.empty
      } else Callback.empty
    }

    def render(props: Props): VdomElement = {
      val db = props.stateSnap.value
      val item = db.tree.items(props.itemId)

      val children = item.childrenIds.toVdomArray(childId =>
        TreeItemComponent.withKey(childId.toString)(props.copy(itemId = childId)))

      if (item.id == db.currentRootId) {
        <.ul(CSS.maximize, children) // the root item is invisible, just show its children
      } else {
        def mod(fn: SimpleDatabase => SimpleDatabase): Callback = props.stateSnap.modState(fn)

        def updateText(e: ReactEventFromInput) =
          mod(_.setText(item, e.target.value))

        def editFieldKeyDown(e: ReactKeyboardEvent): Callback =
          CallbackOption.keyCodeSwitch(e) {
            case KeyCode.Up   => mod(_.selectAndEdit(Before)) >> e.preventDefaultCB
            case KeyCode.Down => mod(_.selectAndEdit(Next)) >> e.preventDefaultCB
            case KeyCode.Tab  => mod(_.toggleExpanded(item)) >> e.preventDefaultCB
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
          else <.i(CSS.blankExpandIcon)

        val taskIcon =
          if (item.itemType != Note)
            <.i(
              CSS.taskIcon,
              CSS.taskcheckedIconFix.when(item.itemType == DoneTask),
              CSS.pointer,
              CSS.hoverBlack.unless(item.itemType == DoneTask),
              CSS.lightGrey.when(item.itemType == DoneTask),
              CSS.checkedCheckBox.when(item.itemType == DoneTask),
              CSS.checkBox.when(item.itemType == Task),
              ^.onClick ==> toggleType
            )
          else <.i(CSS.blankTaskIcon)

        <.div(
          <.div(
            CSS.row,
            if (db.selected == item.id) CSS.selected else CSS.hoverVeryBrightViolet,
            ^.onClick --> mod(_.select(item).startEditing(item)),
//            ^.onClick --> mod(_.select(item)),
            expandIcon,
            taskIcon,
//            <.span(
//              CSS.lightGrey.when(item.itemType == DoneTask),
//              CSS.centerVertically,
//              CSS.invisible.when(editing),
//              CSS.semiBold.when(item.isProject),
//              item.text
//            ),
            <.textarea(
              CSS.centerVertically,
              CSS.input,
              CSS.selected.when(db.selected == item.id),
              CSS.semiBold.when(item.isProject),
              ^.value := item.text,
              ^.onChange ==> updateText,
              ^.onKeyDown ==> editFieldKeyDown,
              ^.onBlur --> mod(_.completeEdit()) // onBlur is called when clicking outside the textarea / input
            ).withRef(inputRef)
          ).withRef(rowRef),
          <.ul(CSS.ulMargins, children).when(item.expanded)
        )
      }
    }
  }

  class MainBackend($ : BackendScope[Unit, SimpleDatabase]) {
    private val mainDivRef = Ref[html.Element]

    def focus: Callback = {
      val x = dom.window.pageXOffset.toInt // Disable scrolling when focusing
      val y = dom.window.pageYOffset.toInt
      mainDivRef.foreach(_.focus()) >> Callback(dom.window.scrollTo(x, y))
    }

    def render(db: SimpleDatabase): VdomElement = {
      val snap = StateSnapshot(db).setStateVia($)
      val rootItem =
        TreeItemComponent.withKey(db.currentRootId.toString)(Props(snap, db.currentRootId))

      def handleKey(e: ReactKeyboardEvent): Callback = {
        def plainKey: CallbackOption[Unit] = // CallbackOption will stop if a key isn't matched
          CallbackOption.keyCodeSwitch(e) {
//            case KeyCode.Delete | KeyCode.Backspace => snap.modState(_.deleteItem())
            case KeyCode.Enter => snap.modState(_.addSibling().startEditing())
//            case KeyCode.Space                      => snap.modState(_.toggleType())
//            case KeyCode.Up                         => snap.modState(_.selectAndEdit(Before))
//            case KeyCode.Down                       => snap.modState(_.selectAndEdit(Next))
//            case KeyCode.Left                       => snap.modState(_.collapseOrJumpUp())
//            case KeyCode.Right                      => snap.modState(_.expandOrSelectChild())
//            case KeyCode.P                          => snap.modState(_.toggleProject())
            case KeyCode.Escape => snap.modState(x => x.zoomInto(ROOTID))
          }

        def altKey: CallbackOption[Unit] =
          CallbackOption.keyCodeSwitch(e, altKey = true) {
            case KeyCode.Up    => snap.modState(_.moveUp())
            case KeyCode.Down  => snap.modState(_.moveDown())
            case KeyCode.Left  => snap.modState(_.moveLeft())
            case KeyCode.Right => snap.modState(_.moveRight())
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

        (plainKey orElse altKey orElse shiftKey orElse ctrlKey) >> e.preventDefaultCB
      }

      <.div(
        CSS.bigCenteredColumn,
        <.div(
          CSS.columns,
          CSS.noOutline,
          ^.tabIndex := 0, // needs to be focusable to receive key presses
          ^.onKeyDown ==> handleKey,
          <.div(
            CSS.leftAlignCol,
            CSS.col6,
            <.div(<.h5("Beispielbaum"),
                  <.div(CSS.treeDiv, Breadcrumbs.BreadcrumbsComponent(snap), rootItem))
          )
        ).withRef(mainDivRef)
      )
    }
  }

  private val MainComponent = ScalaComponent
    .builder[Unit]("TreeNote")
    .initialState(SimpleDatabase.simpleDatabase)
    .renderBackend[MainBackend]
//    .componentDidMount(_.backend.focus)
    // The mainDiv needs to have focus to capture keys. So when the user is not editing: focus it
//    .componentDidUpdate(x => x.backend.focus.when(x.currentState.editing.isEmpty).void)
    .build

  def apply() = MainComponent()
}
