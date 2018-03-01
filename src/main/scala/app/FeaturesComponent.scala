package app

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object FeaturesComponent {

  val component = ScalaComponent.static("Features")(
    <.div(
      <.p("Features Text")
    ))
}
