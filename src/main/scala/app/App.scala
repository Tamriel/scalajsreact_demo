package app

import app.CssSettings._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

import scalacss.ScalaCssReact._

sealed trait Page { def title: String }
case object Motivation extends Page { val title = "Motivation" }
case object Prototype extends Page { val title = "Prototyp" }
case object Features extends Page { val title = "Funktionen" }
case object BusinessModel extends Page { val title = "Geschäftsmodell" }
case object Contact extends Page { val title = "Mitmachen" }
case object Thanks extends Page { val title = "Danke" }
case object Licenses extends Page { val title = "Lizenzen" }

object App {
  val baseUrl = dom.window.location.hostname match {
    // When developing, the url IntelliJ opens is something like
    // `http://localhost:63342/TreeNote/index-dev.html?_ijt=nr2tnh5eia2r6oeqhffh3k21q4/`.
    // It is 78 chars long, so to get the baseURL, we need to cut anything after 78 chars:
    case "localhost" => BaseUrl.fromWindowUrl(s => s.take(78))
    case _           => BaseUrl.fromWindowOrigin
  }

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (trimSlashes
      | staticRoute(root, Motivation) ~> render(MainComponent()))
      .notFound(redirectToPage(Motivation)(Redirect.Replace))
      .renderWith(layout)
      .setTitle(p => "TreeNote - Kollaboratives Wissensmanagement")
  }

  def layout(c: RouterCtl[Page], r: Resolution[Page]) =
    <.div(<.div(CSS.grow, CSS.container, r.render()))

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

      <.div(
        CSS.bigCenteredColumn,
        <.ul(
          ^.cls := "tab tab-block",
          nav(Motivation.title, Motivation),
          nav(Features.title, Features),
          nav(Prototype.title, Prototype),
          nav(BusinessModel.title, BusinessModel),
          nav(Contact.title, Contact)
        )
      )
    }
    .build

  def main(args: Array[String]): Unit = {
    CSS.addToDocument()
    val router = Router(baseUrl, routerConfig)
    router().renderIntoDOM(dom.document.getElementById("root"))
  }
}
