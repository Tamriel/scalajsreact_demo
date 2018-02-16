package app

import org.scalajs.dom
import scala.collection.mutable
import scala.scalajs.js

case object Util {
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
