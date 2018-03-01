package app

import app.Util._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object BusinessModelComponent {

  val component = ScalaComponent.static("BusinessModel")(
    <.div(
      CSS.bigCenteredColumn,
      <.h6("BusinessModel blabla"),
      greySpan(
        "Bearbeitung von Inhalten findet in Baumstrukturen statt und ist damit von Grund auf strukturiert und systematisch")
    ))
}
