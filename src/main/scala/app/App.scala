package app

import org.scalajs.dom.document

object App {
  def main(args: Array[String]): Unit =
    MainComponent().renderIntoDOM(document.getElementById("root"))
}
