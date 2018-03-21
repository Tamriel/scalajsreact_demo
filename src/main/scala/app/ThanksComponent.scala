package app

import app.Util._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object ThanksComponent {

  val component = ScalaComponent.static("Thanks")(
    <.div(
      CSS.smallCenteredColumn,
      <.span("Danke für deine Nachricht. Wir melden uns bald bei dir!")
    ))
}
