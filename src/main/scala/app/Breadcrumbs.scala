package app

import app.MainComponent.Props
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object Breadcrumbs {

  private val CrumbComponent = ScalaComponent
    .builder[Props]("Crumb")
    .render_P { props =>
      val db = props.stateSnap.value
      val item = db.getItem(props.itemId)
      val isRoot = item.id == db.currentRootId

      def zoom() =
        // the current root crumb shall not be clickable, since it is already zoomed into it
        if (!isRoot)
          props.stateSnap.modState(_.zoomInto(item.id))
        else
          Callback()

      <.span(CSS.crumb,
             CSS.grey.unless(isRoot),
             CSS.hoverDark.unless(isRoot),
             CSS.pointer.unless(isRoot),
             item.text,
             ^.onClick --> zoom())
    }
    .build

  val BreadcrumbsComponent = ScalaComponent
    .builder[StateSnapshot[SimpleDatabase]]("Breadcrumbs")
    .render_P { snap =>
      val db = snap.value
      val parentIds = db.parentIds(db.currentRoot)
      val separatedCrumbs = parentIds
        .flatMap(id =>
          List(<.i(CSS.angleRight, CSS.crumbSeparator),
               CrumbComponent.withKey(id.toString)(Props(snap, id)).vdomElement))
        .tail
      <.div(CSS.crumbBar, separatedCrumbs.toTagMod, <.hr(CSS.line))
    }
    .build
}
