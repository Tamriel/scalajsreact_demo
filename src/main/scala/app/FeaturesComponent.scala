package app

import app.Util._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object FeaturesComponent {

  val component = ScalaComponent.static("Features")(
    <.div(
      CSS.bigCenteredColumn,
      <.h6("Gamtlösung für die Bearbeitung und Gestaltung von Projekten"),
      p("Bearbeitung von Inhalten findet in Baumstrukturen statt und ist damit von Grund auf strukturiert und systematisch"),
      <.img(^.src := "res/features/feature_1.png")
    ))
}
