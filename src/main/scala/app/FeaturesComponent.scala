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
      CSS.grey,
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
      )
    )
  )
}
