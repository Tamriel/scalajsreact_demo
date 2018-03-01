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
  val startUrl = "/secret/"
  val baseUrl = dom.window.location.hostname match {
    // When developing, the url IntelliJ opens is something like
    // `http://localhost:63342/TreeNote/index-dev.html?_ijt=nr2tnh5eia2r6oeqhffh3k21q4/`.
    // It is 78 chars long, so to get the baseURL, we need to cut anything after 78 chars:
    case "localhost" => BaseUrl.fromWindowUrl(s => s.take(78) + startUrl)
    case _           => BaseUrl.fromWindowOrigin / startUrl
  }

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (trimSlashes
      | staticRoute(root, Prototype) ~> render(MainComponent())
      | staticRoute("features", Features) ~> render(FeaturesComponent.component())
      | staticRoute("business-model", BusinessModel) ~> render(BusinessModelComponent.component()))
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
             <.a(CSS.pointer,
                 CSS.active.when(target == props.selectedPage),
                 props.c.setOnClick(target),
                 name))

      <.div(CSS.bigCenteredColumn,
            <.ul(^.cls := "tab tab-block",
                 nav("Prototyp", Prototype),
                 nav("Features", Features),
                 nav("Business Model", BusinessModel)))
    }
    .build

  def main(args: Array[String]): Unit = {
    CSS.addToDocument()
    val router = Router(baseUrl, routerConfig)
    router().renderIntoDOM(dom.document.getElementById("root"))
  }
}
