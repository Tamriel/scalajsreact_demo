package app

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object MotivationComponent {

  val component = ScalaComponent.static("Motivation")(
    <.div(
      CSS.mediumCenteredColumn,
      <.p(
        "TreeNote ist eine Webanwendung für das Projekt- und Wissensmanagement in kleineren und mittleren Unternehmen. Dabei können mehrere Anwender in Echtzeit gemeinsam an einem Dokument arbeiten und Informationen strukturieren. Die Bedienung ist einfach und effizient und dank innovativer Verschlüsselung sind die Daten geschützt."),
      Util.line,
      <.span("Das "),
      <.a("Paper",
          ^.href := "https://www.comindware.com/de/landing/techconsult-market-paper-mittelstand/"),
      <.span(" „Projekt- und Wissensmanagement im Wandel“ analysiert die Marktlage wie folgt:"),
      <.ul(
        <.li("Gute Kollaboration im Team ist ein zentraler Erfolgsfaktor in Projekten."),
        <.li("Moderne Tools erleichtern den Informationsfluss im Team."),
        <.li("Die steigenden Informationsmengen sind nur mit Software zu bewältigen.")
      ),
      <.p(
        "Mit TreeNote fällt der Schritt in die Digitalisierung leicht: Es kombiniert Wissensmanagement mit Aufgabenverwaltung und macht beides durch dieselbe Oberfläche einfach zugreifbar."),
      <.span("Auch die "),
      <.a("Unternehmensberater", ^.href := "https://hgn.io/unternehmen/woran-wir-glauben/"),
      <.span(
        " um Stefan Hagen, den meistgelesenen deutschsprachigen Projektmanagement Blogger, glauben an:"),
      <.div(
        CSS.marginPTop,
        CSS.columns,
        <.div(
          CSS.col6,
          <.div(
            ^.marginBottom := "16px",
            Util.card(
              "Flexibilität",
              <.div(
                <.p("\"Wir brauchen Tools, die wir einfach anpassen und verändern können. Starre IT Strukturen und komplexe Projekte widersprechen sich.\""),
                <.p(
                  CSS.zeroMargin,
                  "Im Herzen ist TreeNote ein Outliner. Mit einem Outliner kann man jede beliebige Form von Information frei abbilden. Damit ist TreeNote flexibler als jede andere Projektmanagesoftware."
                )
              )
            )
          ),
          Util.card(
            "Kollaboration",
            <.div(
              <.p("\"Hervorragende Leistungen entstehen ausschließlich in Teamwork – besonders in einem zunehmend komplexen und unsicheren Umfeld.\""),
              <.p(CSS.zeroMargin,
                  "Alle anderen Projekt- und Wissensmanagament-Tools sowie alle anderen Outliner haben keine Echtzeit-Kollaboration. Wir schon.")
            )
          )
        ),
        <.div(
          CSS.col6,
          Util.card(
            "Transparenz",
            <.div(
              <.p("\"Wir brauchen Tools, die wir einfach anpassen und verändern können. Starre IT Strukturen und komplexe Projekte widersprechen sich.\""),
              <.p("Die meisten Projektmanagement-Tools sind komplex und haben eine umständliche Bedienung, was die Hürde zur aktiven Mitarbeit erhöht. Die Bedienung von TreeNote hingegen ist einfach und effektiv."),
              <.p("Die meisten Projektmanagement-Tools bilden nur grobe Arbeitspakete ab. In TreeNote hingegen ist detailliert der aktuelle Stand sichtbar, weil man aktuelle Ergebnisse und Unteraufgaben auf einfachste Weise ergänzen kann.")
            )
          )
        )
      ),
      <.div(CSS.marginPTop),
      <.p("Essentiell für die Koordination im Team ist:"),
      <.ul(
        <.li("eine gemeinsam verwaltete Dateiablage (z.B. Dropbox)"),
        <.li("ein gemeinsamer Kalender (z.B. Google-Calendar)"),
        <.li("ein Team-Messenger (z.B. Slack)")
      ),
      <.p(
        "Ergänzend zu diesen Tools ist TreeNote gedacht für die zentrale Ablage von Informationen (Aufgaben und Texte) sowie für das gemeinsame Schreiben und Strukturieren in Echtzeit."),
      <.p(
        "Die meisten Projektmanagement-Tools sind lediglich zum Nachverfolgen von Aufgaben gedacht. Dahingegen unterstützt TreeNote auch initiale Kreativ- und Planungsphasen."),
      <.span("Laut Business-Magazin t3n sind die "),
      <.a(
        "verbreitetsten",
        ^.href := "http://pm-blog.com/2015/05/09/schoene-neue-arbeitswelt-projektmanagement-und-zusammenarbeit-2-0/#more-7314"),
      <.span(^.dangerouslySetInnerHtml :=
        " Projektmanagement-Tools <b>Trello</b> und <b>Jira</b>. Die Verwaltung von Aufgaben in TreeNote funktioniert genau wie bei diesen Tools, allerdings kann man in TreeNote deutlich einfacher und schneller Informationen und Unteraufgaben ergänzen."),
      <.div(CSS.marginPTop)
    )
  )
}
