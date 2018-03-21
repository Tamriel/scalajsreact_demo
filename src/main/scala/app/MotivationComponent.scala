package app

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object MotivationComponent {

  val component = ScalaComponent.static("Motivation")(
    <.div(
      CSS.bigCenteredColumn,
      <.p(
        "TreeNote ist eine Webanwendung für das Projekt- und Wissensmanagement in kleineren und mittleren Unternehmen. Dabei können mehrere Anwender in Echtzeit gemeinsam an einem Dokument arbeiten und Informationen strukturieren. Die Bedienung ist einfach und effizient und dank innovativer Verschlüsselung sind die Daten geschützt."),
      Util.line,
      <.p(" „Projekt- und Wissensmanagement im Wandel“ analysiert die Marktlage wie folgt:"),
      <.ul(
        <.li("Gute Kollaboration im Team ist ein zentraler Erfolgsfaktor in Projekten."),
        <.li("Moderne Tools erleichtern den Informationsfluss im Team."),
        <.li("Die steigenden Informationsmengen sind nur mit Software zu bewältigen. ")
      ),
      <.p(
        "Mit TreeNote fällt der Schritt in die Digitalisierung leicht: Es kombiniert Wissensmanagement mit Aufgabenverwaltung und macht beides durch dieselbe Oberfläche einfach zugreifbar.")
    )
  )
}
