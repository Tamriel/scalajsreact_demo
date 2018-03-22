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
      <.div(CSS.marginPTop),
      <.span("CSS Framework:"),
      <.ul(<.li(<.a(^.href := "https://picturepan2.github.io/spectre/", "Spectre.css"))),
      <.span("Fonts:"),
      <.ul(<.li(<.a(^.href := "https://fontawesome.com/", "Font Awesome")),
           <.li(<.a(^.href := "https://mfglabs.github.io/mfglabs-iconset/", "MFG Labs"))),
      <.span("Icons:"),
      <.ul(
        <.li(<.a(^.href := "https://www.flaticon.com/authors/pixel-buddha", "Pixel Buddha")),
        <.li(<.a(^.href := "https://www.flaticon.com/authors/pixel-perfect", "Pixel perfect")),
        <.li(<.a(^.href := "http://www.freepik.com", "Freepik"))
      )
    )
  )
}
