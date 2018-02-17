package app

import CssSettings._

object CSS extends StyleSheet.Inline {

  import dsl._

  val veryLightGrey = style(color(c"#d9d9d9"))
  val checkSquare = style(addClassName("fa-li far fa-check-square"))
  val square = style(addClassName("fa-li far fa-square"))
  val angleRight = style(addClassName("fas fa-angle-right fa-lg fa-fw"))
  val angleDown = style(addClassName("fas fa-angle-down fa-lg fa-fw"))
  val blankIcon = style(addClassName("fas fa-lg fa-fw"))
  val fontAwesomeUl = style(addClassName("fa-ul"))
  val input = style(addClassNames("form-input"))
  val visible = style(display.block)
  val invisible = style(display.none)
  val selected = style(backgroundColor.orange)
  val pointer = style(cursor.pointer)
  val hover = style(&.hover(backgroundColor.lightgrey))
  val maximize = style(
    height(100 %%),
    margin.`0`,
  )
  val container = style(addClassName("container"))
  val columns = style(addClassName("columns"))
  val leftColumm = style(addClassName("column col-5"))
  val rightColumm = style(addClassName("column col-7"))

  style(
    unsafeRoot("html")(maximize),
    unsafeRoot("body")(maximize, overflowY.scroll), // always show the scrollbar
    unsafeRoot("#root")(maximize)
  )
}
