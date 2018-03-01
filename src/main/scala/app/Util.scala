package app

import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

import scala.collection.mutable
import scala.scalajs.js
import scalacss.ScalaCssReact._

case object Util {

  val greySpan = <.span(CSS.grey)
  val awesomeUl = <.ul(CSS.fontAwesomeUl)
  val topMarginH6 = <.h6(^.marginTop := "32 px")

  /** Can be used to generate pretty printed and colored console output in the Chrome browser.
    * Source: https://gist.github.com/lihaoyi/7e6c429bf0c18465479450c0a351e8f5 */
  def debug[T](value: sourcecode.Text[T], tag: String = "")(implicit path: sourcecode.Enclosing,
                                                            line: sourcecode.Line): Unit = {
    val titleIter = path.value.split(" |#|\\.").filter(!_.contains("$")).last
    val tagIter =
      if (tag == "") ""
      else " " + pprint.tokenize(tag).mkString

    val lineIter = ":" + pprint.tokenize(line.value).mkString

    val valName = value.source
    val item = pprint.tokenize(value.value).mkString

    val wholeStr =
      (titleIter + tagIter) +
        lineIter + " " + valName + "\t" + item

    val replaced = fansi.Str.ansiRegex.matcher(wholeStr).replaceAll("%c")
    val m = fansi.Str.ansiRegex.matcher(wholeStr)

    val styleList = mutable.Buffer.empty[js.Any]
    while (m.find()) {
      styleList.append(
        wholeStr.substring(m.start(), m.end) match {
          case Console.RED     => "color: red"
          case Console.GREEN   => "color: green"
          case Console.BLUE    => "color: blue"
          case Console.YELLOW  => "color: orange" // yellow is too bright
          case Console.MAGENTA => "color: magenta"
          case Console.CYAN    => "color: cyan"
          case _               => "color: black"
        }
      )
    }
    dom.console.log(replaced, styleList: _*)
  }
}
