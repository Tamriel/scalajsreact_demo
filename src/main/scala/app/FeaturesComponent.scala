package app

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object FeaturesComponent {

  private def feature(img: String, heading: String, expl: String, imgLeft: Boolean = true) = {
    val imgDiv = <.div(
      ^.cls := "column col-4 col-5-xl",
      if (imgLeft) CSS.leftAlignCol else CSS.rightAlignCol,
      <.img(if (imgLeft) CSS.floatRight else CSS.floatLeft, ^.src := "res/img/features/" + img)
    )
    val explDiv = <.div(CSS.grey,
                        ^.padding := "30px",
                        ^.cls := "column col-6 col-7-xl",
                        if (imgLeft) CSS.rightAlignCol else CSS.leftAlignCol,
                        <.h5(heading),
                        <.p(expl))
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
        "Methode: Brainstorming mit Bäumen",
        "Die Bearbeitung von Inhalten findet in Baumstrukturen statt und ist damit von Grund auf strukturiert und systematisch. Dies ermöglicht die intuitive Erstellung von Projektplänen, sowie die Struktierung und Dokumentation ihrer Abläufe."
      ),
      feature(
        "feature_1.png",
        "Methode: Kollaborativ schreiben",
        "Die Texte können von mehreren Nutzern gleichzeitig bearbeitet werden. Neben der Website kann TreeNote auch in Desktop- und Smartphone Apps genutzt werden. Mit ihnen sind die Daten auch offline verfügbar.",
        imgLeft = false
      )
    )
  )
}
