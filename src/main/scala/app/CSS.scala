package app

import CssSettings._

object CSS extends StyleSheet.Inline {

  import dsl._

  val veryLightGrey = style(color(c"#d9d9d9"))
  val checkSquare = style(addClassName("fa-li far fa-check-square"))
  val square = style(addClassName("fa-li far fa-square"))
  val angleRight = style(addClassName("fas fa-angle-right fa-lg"))
  val angleDown = style(addClassName("fas fa-angle-down fa-lg"))
  val icon = style(width(16 px), marginLeft(8 px))
  val marginTextToIcon = style(marginLeft(9.3 px))
  val input = style(marginLeft(6 px), addClassName("form-input"), width(94 %%), padding(2 px))
  val fontAwesomeUl = style(addClassName("fa-ul"))
  val row = style(
    height(47 px),
    // https://stackoverflow.com/a/13075912
    &.before(content := """""""", display.inlineBlock, height(100 %%), verticalAlign.middle))
  val centerVertically = style(display.inlineBlock, verticalAlign.middle)
  val invisible = style(display.none)
  val selected = style(backgroundColor.orange)
  val pointer = style(cursor.pointer)
  val hover = style(&.hover(backgroundColor.lightgrey))
  val ulMargins = style(marginTop.`0`, marginBottom.`0`, marginLeft(45 px))
  val maximize = style(
    height(100 %%),
    margin.`0`,
  )
  val container = style(addClassName("container"))
  val columns = style(addClassName("columns"))
  val leftColumm = style(addClassName("column col-5"))
  val rightColumm = style(addClassName("column col-7"))

  style(
    unsafeRoot("html")(maximize, style(fontSize(24 px))),
    unsafeRoot("body")(maximize, overflowY.scroll), // always show the scrollbar
    unsafeRoot("#root")(maximize)
  )
}
