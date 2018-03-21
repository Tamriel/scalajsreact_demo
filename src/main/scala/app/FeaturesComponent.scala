package app

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object FeaturesComponent {

  private val basePath = "res/img/features/"

  private def feature(source: String,
                      heading: String,
                      text: List[String],
                      imgLeft: Boolean = true,
                      video: Boolean = false,
                      paddingTop: Int = 0) = {
    val imgDiv = <.div(
      ^.cls := "column col-4 col-5-xl",
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
      ^.cls := "column col-6 col-7-xl",
      if (imgLeft) CSS.rightAlignCol else CSS.leftAlignCol,
      <.h5(heading),
      text.toTagMod(t => <.p(^.marginBottom := "20px", t))
    )
    <.div(
      ^.padding := "20px",
      CSS.columns,
      if (imgLeft) imgDiv else explDiv,
      if (imgLeft) explDiv else imgDiv
    )
  }

  val component = ScalaComponent.static("Features")(
    <.div(
      CSS.bigCenteredColumn,
      feature(
        "feature_1.png",
        "Brainstorming mit Bäumen",
        List(
          "Die Bearbeitung von Inhalten findet in Baumstrukturen statt und ist damit von Grund auf strukturiert und systematisch. Dies ermöglicht die intuitive Erstellung von Projektplänen, sowie die Struktierung und Dokumentation ihrer Abläufe.")
      ),
      feature(
        "collab_typing.mp4",
        "Kollaborativ schreiben",
        List(
          "Die Texte können von mehreren Anwendern gleichzeitig bearbeitet werden.",
          "Neben der Website kann TreeNote auch in Desktop- und Smartphone Apps genutzt werden. Mit ihnen sind die Daten auch offline verfügbar.",
          "Einzelne Projekte können per Link mit Partnern oder Stakeholdern geteilt werden."
        ),
        imgLeft = false,
        video = true,
        paddingTop = 80
      ),
      feature(
        "feature_1.png",
        "Aufbau von TreeNote",
        List(
          "Durch eine übergeordnete Struktur werden Projektmanagement und Organisation von Wissen verbunden. Es werden mehrere Projekte verwaltet, wobei in jedem Projekt Listen von Aufgaben, Meilensteinen und Zielen verwaltet werden.")
      ),
      feature(
        "feature_1.png",
        "Anwendungsfall: Planung eines Projekts",
        List("Einträge können als Aufgabe deklariert und Personen zugewiesen werden."),
        imgLeft = false
      ),
      feature(
        "feature_1.png",
        "Anwendungsfall: Steuerung eines Projekts",
        List("Nach der Anwendung von Filtern werden nur die eigenen Aufgaben angezeigt.")
      ),
      feature(
        "feature_1.png",
        "",
        List(
          "Beim Ausführen einer Aufgabe notiert der Anwender Informationen, Erkenntnisse und Probleme. Dabei kann er beliebig Unterpunkte und Unteraufgaben erstellen. Das hat folgende Vorteile:")
      ),
      Util.line,
      <.h4("Alleinstellungsmerkmale"),
      feature(
        "feature_1.png",
        "Vorlagen",
        List("Die Verwendung von Vorlagen ermöglicht")
      ),
      feature(
        "feature_1.png",
        "Echtzeit-Kollaboration",
        List(
          "Beim gleichzeiten Bearbeiten können Anwender die Eingaben der anderen live mitverfolgen.",
          "Wir verwenden einen innovativen Algorithmus zur Auflösung von Bearbeitungskonflikten. Dadurch wird Datenverlust vorgebeugt."
        ),
        imgLeft = false
      ),
      feature(
        "feature_1.png",
        "Ende-zu-Ende-Verschlüsselung",
        List(
          "Durch die zugrundeliegende Architektur werden alle Daten bereits beim Anwender verschlüsselt. Die Server leiten also lediglich verschlüsselte Daten weiter und Dritten wird der Zugriff auf die Daten grundsätzlich verwehrt.",
          "Damit ist TreeNote besonders für Unternehmen geeignet, die ihre Kundendaten, ihr Wissen und ihre Geschäftsprozesse geschützt wissen wollen."
        )
      ),
      Util.line,
      <.p("Insgesamt sind die Stärken von TreeNote:"),
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
