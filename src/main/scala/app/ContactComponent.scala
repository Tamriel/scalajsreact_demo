package app

import app.Util._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object ContactComponent {

  val component = ScalaComponent.static("BusinessModel")(
    <.div(
      CSS.smallCenteredColumn,
      greySpan("Schreibe uns eine E-Mail an "),
      <.a(^.href := "mailto:kontakt@treenote.org", "kontakt@treenote.org"),
      greySpan(CSS.smallTopMargin, " oder nutze folgendes Formular:"),
      <.form(
        ^.method := "POST",
        ^.action := "https://formcarry.com/s/SyJ2tnSdM",
        <.input(CSS.smallTopMargin,
                ^.cls := "form-input",
                ^.name := "email",
                ^.`type` := "email",
                ^.placeholder := "E-Mail"),
        <.textarea(CSS.smallTopMargin,
                   ^.cls := "form-input",
                   ^.rows := 10,
                   ^.name := "message",
                   ^.placeholder := "Nachricht"),
        <.button(CSS.smallTopMargin, ^.cls := "btn", "Absenden")
      )
    ))
}
