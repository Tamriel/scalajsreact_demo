package app

import org.scalajs.dom.document
import CssSettings._
import scalacss.ScalaCssReact._

object App {
  def main(args: Array[String]): Unit = {
    CSS.addToDocument()
    MainComponent().renderIntoDOM(document.getElementById("root"))
  }
}
