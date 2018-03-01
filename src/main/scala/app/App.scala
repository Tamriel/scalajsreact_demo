package app

import app.CssSettings._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

import scalacss.ScalaCssReact._

sealed trait Page
case object Prototype extends Page
case object Features extends Page
case object BusinessModel extends Page

object App {
  val baseUrl = dom.window.location.hostname match {
    case "localhost" | "127.0.0.1" | "0.0.0.0" => BaseUrl.fromWindowUrl(_.takeWhile(_ != '#'))
    case _                                     => BaseUrl.fromWindowOrigin / "secret/"
  }

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (trimSlashes
      | staticRoute(root, Prototype) ~> render(MainComponent())
      | staticRoute("#features", Features) ~> render(FeaturesComponent.component())
      | staticRoute("#business-model", BusinessModel) ~> render(BusinessModelComponent.component()))
      .notFound(redirectToPage(Prototype)(Redirect.Replace))
      .renderWith(layout)
      .setTitle(p => s"$p | TreeNote - Kollaboratives Wissens- und Projektmanagement")
  }

  def layout(c: RouterCtl[Page], r: Resolution[Page]) =
    <.div(
      <.div(
        CSS.columns,
        ^.paddingTop := "10px",
        ^.paddingBottom := "20px",
        <.div(^.className := "column col-5",
              <.img(^.src := "res/logo_violett_90px.png", ^.className := "float-right")),
        <.div(
          ^.className := "column col-4",
          <.h1("TreeNote",
               ^.fontSize := "1.6rem",
               ^.marginTop := ".05em",
               ^.marginBottom := ".2em"),
          <.h2("Kollaboratives Wissens- und Projektmanagement",
               ^.fontSize := ".9rem",
               ^.fontWeight := "400")
        )
      ),
      <.div(CSS.columns, ^.paddingBottom := "40px", navMenuComponent(MenuProps(c, r.page))),
      <.div(^.cls := "container", r.render())
    )

  case class MenuProps(c: RouterCtl[Page], selectedPage: Page)

  val navMenuComponent = ScalaComponent
    .builder[MenuProps]("Menu")
    .render_P { props =>
      def nav(name: String, target: Page) =
        <.li(CSS.tab,
             <.a(CSS.active.when(target == props.selectedPage),
                 ^.href := "",
                 props.c.setOnClick(target),
                 name))

      <.div(
        ^.cls := "col-10 col-xl-12 col-mx-auto",
        <.ul(^.cls := "tab tab-block",
             nav("Prototyp", Prototype),
             nav("Features", Features),
             nav("Business Model", BusinessModel))
      )
    }
    .build

  def main(args: Array[String]): Unit = {
    CSS.addToDocument()
    val router = Router(baseUrl, routerConfig)
    router().renderIntoDOM(dom.document.getElementById("root"))
  }
}
