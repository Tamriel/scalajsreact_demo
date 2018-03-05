package app

import app.Util._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object LicensesComponent {

  val component = ScalaComponent.static("Licenses")(
    <.div(
      CSS.smallCenteredColumn,
      <.h4("Lizenzen"),
      greySpan("Unter der SIL Open Font License:"),
      <.ul(<.li(<.a(^.href := "https://fontawesome.com/", "Font Awesome")),
           <.li(<.a(^.href := "https://mfglabs.github.io/mfglabs-iconset/", "MFG Labs")))
    )
  )
}
