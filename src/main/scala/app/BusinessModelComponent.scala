package app

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object BusinessModelComponent {

  val component = ScalaComponent.static("BusinessModel")(
    <.div(
      CSS.mediumCenteredColumn,
      <.ol(
        <.li(
          "Es gibt eine kostenlose, eingeschränkte Version von TreeNote. Die volle Version kostet 5 € pro Anwender pro Monat."),
        <.li(
          "Unternehmen können TreeNote auf ihren eigenen Servern installieren. Das kostet 1000 €."),
        <.li("Wir bieten die Dienstleistung an, TreeNote an die Arbeitsabläufe des Unternehmens anzupassen.")
      ),
      <.p("Die Webanwendung ist aus technischer Sicht skalierbar. Damit kann auch die Anzahl der Kunden / das Einkommen uneingeschränkt wachsen."),
      <.h4("Konkurrenz"),
      <.p(
        "Im Herzen ist TreeNote ein Outliner. Zur Zeit existieren drei kollaborative Outliner: ",
        <.a("Workflowy", ^.href := "https://workflowy.com/"),
        ", ",
        <.a("Moo.do", ^.href := "https://www.moo.do/"),
        " und ",
        <.a("Dynalist", ^.href := "https://dynalist.io/"),
        ". Diese Unternehmen haben jeweils weniger als zehn Mitarbeiter."
      ),
      <.p("Das erklärte Ziel dieser Outliner ist Selbstmanagement."),
      <.p("TreeNote deckt selbstverständlich deren Standardfunktionen wie z.B. Suche, Zoom und Formatierungsmöglichkeiten ab."),
      <.p("Die konkurrierenden Outliner haben zwei Probleme:"),
      <.div(
        CSS.columns,
        <.div(
          CSS.col6,
          Util.card(
            <.div(
              <.p("Workflowy und Dynalist synchronisieren sich alle 10 Sekunden mit dem Server. Bei Moo.do kommt eine Änderung nach rund 2 Sekunden bei anderen Anwendern an."),
              <.p(
                CSS.zeroMargin,
                "Bei allen drei Outlinern gewinnt bei einem Bearbeitungskonflikt eine Seite, die Änderungen der anderen Seite gehen ohne Hinweis verloren. Das ist besonders fatal beim Umstrukturieren von Einträgen."
              )
            ),
            Some("Datenverlust bei Bearbeitungskonflikten")
          )
        ),
        <.div(
          CSS.col6,
          Util.card(
            <.div(
              <.p("Moo.do speichert die Nutzerdaten unverschlüsselt bei Google, Workflowy speichert sie unverschlüsselt bei Amazon. Damit sind die persönliche Informationen der Anwender sowie ggf. Geschäftsgeheimnisse ungeschützt."),
              <.span(
                "Die Unternehmensberatung Corporate Trust titelt: \"Eine der wichtigsten Erkenntnisse aus den Snowden-Dokumenten für Deutschland ist, dass unsere Wirtschaft erklärtes Spionageziel der NSA ist.\" ["),
              <.a("1",
                  ^.href := "https://www.welt.de/wirtschaft/article162217929/So-spionieren-Geheimdienste-deutsche-Firmen-aus.html"),
              <.span(", "),
              <.a("2", ^.href := "https://www.corporate-trust.de/de/portfolio-items/nsa-report"),
              <.span("]"),
              <.p(
                CSS.marginPTop,
                ^.marginBottom := "0px",
                "Deshalb sollten gerade deutsche Unternehmen das Thema IT-Sicherheit ernst nehmen.")
            ),
            Some("Keine Datensicherheit")
          )
        )
      ),
      <.div(CSS.marginPTop),
      <.p(^.dangerouslySetInnerHtml := "Dank der beiden technischen Innovationen <b>Echtzeit-Kollaboration</b> und <b>Ende-zu-Ende-Verschlüsselung</b> ist TreeNote den anderen Outlinern voraus."),
      Util.line,
      <.p(
        "Unsere Zielgruppe sind allerdings Unternehmen für Projektmanagement. Konkurrenten in diesem Bereich sind beispielsweise ",
        <.a("Jira", ^.href := "https://de.atlassian.com/software/jira"),
        ", ",
        <.a("Trello", ^.href := "https://trello.com/"),
        " und ",
        <.a("Asana", ^.href := "https://asana.com/de/"),
        ". Diese Unternehmen haben jeweils 50 bis 300 Mitarbeiter."
      ),
      <.ul(
        <.li("Auch diese Konkurrenten legen keinen Wert auf Datensicherheit."),
        <.li("Deren Software ist sehr starr, es gibt feste Vorgaben, Denk- und Bedienweisen."),
        <.li("Das Planen findet außerhalb der Software statt und das Ergebnis anschließend umständlich in die Software übertragen.")
      ),
      <.p("Unsere Innovation ist, die Vorteile von Outlinern für das Projektmanagement von Unternehmen nutzbar zu machen:"),
      <.ul(
        <.li("Outliner sind einfacher bedienbar als klassische Projektmanagement-Software."),
        <.li(
          "Trotzdem sind Outliner flexibler und mächtiger. Es lassen sich beispielsweise beliebig Unteraufgaben erstellen."),
        <.li("Dank Echtzeit-Kollaboration kann direkt in TreeNote geplant werden."),
        <.li("Wissen wird nicht verstreut in externen Tools abgelegt, sondern ist bei den zugehörigen Aufgaben zu finden.")
      ),
      <.p("Weiterhin haben wir Vorlagen. Mit diesen integrieren wir populäre Funktionen von Projektmanagement-Software (z.B. Trello-Spalten) und Funktionen zur Erstellung von Dokumentationen."),
      Util.line,
      <.p("Warum hat die Software der Konkurrenz noch Bearbeitungskonflikte und ist unverschlüsselt? Kann die Konkurrenz nachziehen?"),
      <.p("Die Technologien für Echtzeit-Kollaboration und Verschlüsselung hängen zusammen und sind erst durch neuere Forschung an sogenannten CRDTs (Conflict-free replicated data types) möglich. Die Konkurrenz nutzt ältere Technologien. Dass sie auf diese neue Technologie umsteigt ist aus zwei Gründen unwahrscheinlich:"),
      <.ol(
        <.li(
          "Sie müssten ihr gesamtes Backend und das Datenmodell wegwerfen und neu schreiben. Weil das Datenmodell auch im Frontend genutzt wird, ist das sehr viel Aufwand."),
        <.li("Im Jahr 2016 wurde der erste CRDT veröffentlicht, der auch mit Bäumen funktioniert. Allerdings unterstützte er noch nicht die Verschieben-Operation. Einen Algorithmus, der den CRDT um das Verschieben von Einträgen erweitert, hat Jan in seiner Masterarbeit entwickelt. Die Konkurrenz müsste also erst einmal an grundlegenden Algorithmen forschen - die wir bereits haben.")
      )
    ))
}
