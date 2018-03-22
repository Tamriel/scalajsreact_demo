package app

import app.Util._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object ContactComponent {

  val component = ScalaComponent.static("Contact")(
    <.div(
      CSS.mediumCenteredColumn,
      <.p("Wir sind zwei Software-Entwickler: Jan Korte und Johannes Krauss. "),
      <.p("Selbstmanagement mit Outlinern hat uns in den letzten Jahren dabei geholfen, umfangreiche Projekte zu meistern. Dazu haben wir eine einfachere Version der vorgestellten Software genutzt. Für Projektarbeit in Teams hätten wir gerne kollaborative Funktionen und ein erweitertern Funktionsumfang gehabt, welcher helfen würde, auch komplexere Projekte zu planen, umzusetzen und zu dokumentieren."),
      <.p("Um unsere Ideen zu verwirklichen, bewerben wir und auf das Exist-Gründerstipendium."),
      <.p("Dafür suchen wir einen Menschen, der / die"),
      <.ul(
        <.li("Kenntnisse in Marketing oder Betriebswirtschaftslehre hat."),
        <.li("auch Spaß an dieser Arbeit hat."),
        <.li("möglichst ab jetzt (März 2018) Zeit hat.")
      ),
      <.p("Wir bieten:"),
      <.ul(
        <.li("Flache Hierarchien & lockeres Arbeiten."),
        <.li("Hohe Flexibilität.")
      ),
      <.div(^.padding := "14px"),
      <.span("Schreibe uns eine E-Mail an "),
      <.a(^.href := "mailto:kontakt@treenote.org", "kontakt@treenote.org"),
      <.span(CSS.smallTopMargin, " oder nutze folgendes Formular:"),
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
