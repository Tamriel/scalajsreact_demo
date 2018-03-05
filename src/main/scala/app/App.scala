package app

import app.CssSettings._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

import scalacss.ScalaCssReact._

sealed trait Page { def title: String }
case object Prototype extends Page { val title = "Prototyp" }
case object Features extends Page { val title = "Funktionen" }
case object BusinessModel extends Page { val title = "Geschäftsmodell" }
case object Contact extends Page { val title = "Kontakt" }
case object Thanks extends Page { val title = "Danke" }
case object Licenses extends Page { val title = "Lizenzen" }

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
      | staticRoute("business-model", BusinessModel) ~> render(BusinessModelComponent.component())
      | staticRoute("contact", Contact) ~> render(ContactComponent.component())
      | staticRoute("thanks", Thanks) ~> render(ThanksComponent.component())
      | staticRoute("licenses", Licenses) ~> render(LicensesComponent.component()))
      .notFound(redirectToPage(Prototype)(Redirect.Replace))
      .renderWith(layout)
      .setTitle(p => p.title + " | TreeNote - Kollaboratives Wissens- und Projektmanagement")
  }

  def layout(c: RouterCtl[Page], r: Resolution[Page]) =
    <.div(
      CSS.mainContainer,
      LogoComponent.component(),
      <.div(CSS.dontShrink,
            CSS.columns,
            ^.paddingBottom := "40px",
            navMenuComponent(MenuProps(c, r.page))),
      <.div(CSS.grow, CSS.container, r.render()),
      <.div(CSS.dontShrink, CSS.columns, <.a(CSS.license, c.setOnClick(Licenses), "Lizenzen"))
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

      <.div(
        CSS.bigCenteredColumn,
        <.ul(^.cls := "tab tab-block",
             nav(Prototype.title, Prototype),
             nav(Features.title, Features),
             nav(BusinessModel.title, BusinessModel),
             nav(Contact.title, Contact))
      )
    }
    .build

  def main(args: Array[String]): Unit = {
    CSS.addToDocument()
    val router = Router(baseUrl, routerConfig)
    router().renderIntoDOM(dom.document.getElementById("root"))
  }
}
