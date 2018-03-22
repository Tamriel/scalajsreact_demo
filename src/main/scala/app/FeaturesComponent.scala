package app

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object FeaturesComponent {

  private val basePath = "res/img/features/"

  def pList(texts: List[String]): TagMod = texts.toTagMod(t => <.p(^.marginBottom := "20px", t))

  private def feature(source: String,
                      heading: String,
                      html: TagMod,
                      imgLeft: Boolean = true,
                      video: Boolean = false,
                      paddingTop: Int = 0) = {
    val imgDiv = <.div(
      ^.cls := "column col-5",
      ^.paddingTop := paddingTop + "px",
      if (imgLeft) CSS.leftAlignCol else CSS.rightAlignCol,
      if (video)
        <.video(if (imgLeft) CSS.floatRight else CSS.floatLeft,
                ^.loop := true,
                ^.autoPlay := true,
                <.source(^.src := basePath + source, ^.`type` := "video/mp4"))
      else
        <.img(if (imgLeft) CSS.floatRight else CSS.floatLeft, ^.src := basePath + source)
    )
    val explDiv = <.div(
      ^.padding := "30px",
      ^.cls := "column col-7",
      if (imgLeft) CSS.rightAlignCol else CSS.leftAlignCol,
      <.h5(heading),
      html
    )
    <.div(
      ^.padding := "20px",
      CSS.columns,
      if (imgLeft) imgDiv else explDiv,
      if (imgLeft) explDiv else imgDiv
    )
  }

  private def column(source: String, heading: String, html: TagMod) = <.div(
    ^.cls := "column col-6",
    ^.padding := "20px",
    <.h5(<.img(^.src := basePath + source, ^.marginRight := "10px"), heading),
    html
  )

  val component = ScalaComponent.static("Features")(
    <.div(
      CSS.mediumCenteredColumn,
      feature(
        "brainstorm.mp4",
        "Brainstorming mit Bäumen",
        <.p(
          "Die Bearbeitung von Inhalten findet in Baumstrukturen statt und ist damit von Grund auf strukturiert und systematisch. Dies ermöglicht die intuitive Erstellung von Projektplänen, sowie die Struktierung und Dokumentation ihrer Abläufe."),
        video = true
      ),
      feature(
        "collab_typing.mp4",
        "Kollaborativ schreiben",
        pList(
          List(
            "Die Texte können von mehreren Anwendern gleichzeitig bearbeitet werden.",
            "Neben der Website kann TreeNote auch in Desktop- und Smartphone Apps genutzt werden. Mit ihnen sind die Daten auch offline verfügbar.",
            "Einzelne Projekte können per Link mit Partnern oder Stakeholdern geteilt werden."
          )),
        imgLeft = false,
        video = true,
        paddingTop = 80
      ),
      feature(
        "aufbau.png",
        "Aufbau von TreeNote",
        <.p("Durch eine übergeordnete Struktur werden Projektmanagement und Organisation von Wissen verbunden. Es werden mehrere Projekte verwaltet, wobei in jedem Projekt Listen von Aufgaben, Meilensteinen und Zielen verwaltet werden.")
      ),
      feature(
        "zuweisen.png",
        "Anwendungsfall: Steuerung eines Projekts",
        <.p("Einträge können als Aufgabe deklariert und Personen zugewiesen werden."),
        imgLeft = false,
        paddingTop = 30
      ),
      feature(
        "filter.png",
        "",
        <.p("Nach der Anwendung von Filtern werden nur die eigenen Aufgaben angezeigt."),
        paddingTop = 30
      ),
      feature(
        "unteraufgaben.png",
        "",
        <.div(
          <.p(
            "Beim Ausführen einer Aufgabe notiert der Anwender Informationen, Erkenntnisse und Probleme. Dabei kann er beliebig Unterpunkte und Unteraufgaben erstellen. Das hat folgende Vorteile:"),
          <.ul(
            <.li("Entscheidungsprozesse sind nachvollziehbar."),
            <.li(
              "Das Festhalten aktueller Fragestellungen erleichtert die Koordination und Zusammenarbeit im Team."),
            <.li("Die Erstellung von Zwischen- und Abschlussberichten wird vereinfacht, weil die nötigen Informationen bereits vorhanden sind.")
          )
        ),
        imgLeft = false,
        paddingTop = 50
      ),
      Util.line,
      <.h4("Alleinstellungsmerkmale"),
      <.div(
        CSS.columns,
        <.div(^.cls := "column col-7",
              Util.card("Die Eingabe wiederkehrender Datenstrukturen",
                        imageSource = Some(basePath + "kontakte.png")),
        ),
        <.div(^.cls := "column col-7", <.img(^.src := basePath + "trello.png")),
        <.div(^.cls := "column col-7", <.img(^.src := basePath + "tabelle.png")),
        <.div(^.cls := "column col-7", <.img(^.src := basePath + "doc.png")),
        <.div(
          ^.cls := "column col-5",
          ^.padding := "20px",
          <.h5("1. Vorlagen"),
          <.div(
            <.p("Die Verwendung von Vorlagen ermöglicht:"),
            <.ul(
              <.li(
                "die übersichtliche Darstellung von Inhalten. Beispielsweise geben Spalten (wie bei Trello) eine Übersicht über den Projektfortschritt."),
              <.li("das Exportieren von Bäumen in Berichte und Dokumentationen.")
            )
          ),
        )
      ),
      <.div(
        CSS.columns,
        column(
          "team.png",
          "2. Echtzeit-Kollaboration",
          pList(List(
            "Beim gleichzeiten Bearbeiten können Anwender die Eingaben der anderen live mitverfolgen.",
            "Wir verwenden einen innovativen Algorithmus zur Auflösung von Bearbeitungskonflikten. Dadurch wird Datenverlust vorgebeugt."
          ))
        ),
        column(
          "padlock.png",
          "3. Ende-zu-Ende-Verschlüsselung",
          pList(List(
            "Durch die zugrundeliegende Architektur werden alle Daten bereits beim Anwender verschlüsselt. Die Server leiten also lediglich verschlüsselte Daten weiter und Dritten wird der Zugriff auf die Daten grundsätzlich verwehrt.",
            "Damit ist TreeNote besonders für Unternehmen geeignet, die ihre Kundendaten, ihr Wissen und ihre Geschäftsprozesse geschützt wissen wollen."
          ))
        )
      ),
      Util.line,
      <.p(^.paddingTop := "1px"),
      <.p(^.dangerouslySetInnerHtml := "Insgesamt sind die <b>Stärken</b> von TreeNote:"),
      <.ul(
        <.li("Einfach: Eine niedrige Einstiegshürde und eine effektive Bedienung der Anwendung."),
        <.li("Kollaborativ: In Echtzeit gemeinsam Schreiben und Strukturieren."),
        <.li(
          "Flexibel: Geeignet für kreative Planungsphasen, detaillierte Teilprojekte und die hierarische Ablage von Wissen.")
      ),
      <.p("Und bei all dem sind Ihre Daten natürlich bestens geschützt!")
    )
  )
}
