package app

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scalacss.ScalaCssReact._

object LogoComponent {

  val component = ScalaComponent.static("Logo")(
    <.div(
      CSS.columns,
      ^.paddingTop := "10px",
      ^.paddingBottom := "20px",
      <.div(^.className := "column col-5",
            <.img(^.src := "res/img/logo_violett_90px.png", ^.className := "float-right")),
      <.div(
        ^.className := "column col-4",
        <.h1("TreeNote", ^.fontSize := "1.6rem", ^.marginTop := ".05em", ^.marginBottom := ".2em"),
        <.h2("Kollaboratives Wissens- und Projektmanagement",
             ^.fontSize := ".9rem",
             ^.fontWeight := "400")
      )
    ))
}
