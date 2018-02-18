package app

import CssSettings._

object CSS extends StyleSheet.Inline {
  import dsl._

  val backgroundGrey = c"#f8f9fa"
  val brightViolet = c"#e2e2ff"
  val veryBrightViolet = c"#f9f9ff"
  val veryLightGrey = style(color(c"#d9d9d9"))
  val checkSquare = style(addClassName("fa-li far fa-check-square"))
  val square = style(addClassName("fa-li far fa-square"))
  val angleRight = style(addClassName("fa-angle-right"))
  val angleDown = style(addClassName("fa-angle-down"))
  val icon = style(addClassName("fas fa-lg"), width(16 px), marginLeft(8 px))
  val marginTextToIcon = style(marginLeft(9.3 px))
  val input = style(marginLeft(6 px), addClassName("form-input"), width(94 %%), padding(2 px))
  val fontAwesomeUl = style(addClassName("fa-ul"))
  val row = style(
    borderRadius(4 px),
    height(47 px),
    // https://stackoverflow.com/a/13075912
    &.before(content := """""""", display.inlineBlock, height(100 %%), verticalAlign.middle)
  )
  val centerVertically = style(display.inlineBlock, verticalAlign.middle)
  val invisible = style(display.none)
  val selected = style(backgroundColor(brightViolet))
  val pointer = style(cursor.pointer)
  val hover = style(&.hover(backgroundColor(veryBrightViolet)))
  val ulMargins = style(marginTop.`0`, marginBottom.`0`, marginLeft(45 px))
  val maximize = style(height(100 %%), margin.`0`)
  val mainContainer = style(addClassName("container"), outline.none)
  val columns = style(addClassName("columns"))
  val topMargin = style(marginTop(32 px))

  style(
    unsafeRoot("kbd")(
      padding(3 px, 5 px),
      border(1 px, solid, c"#c6cbd1"),
      fontSize(17 px),
      backgroundColor(c"#fafbfc"),
      color(c"#444d56"),
      borderRadius(3 px),
      display.inlineBlock,
      margin(0 px, 0.1 em),
      lineHeight(13 px),
      verticalAlign.middle,
      boxShadow := "inset 0 -1px 0 #959da5"
    ),
    unsafeRoot("html")(maximize, style(fontSize(24 px))),
    unsafeRoot("body")(maximize, overflowY.scroll, backgroundColor(backgroundGrey)), // always show the scrollbar
    unsafeRoot("#root")(maximize)
  )
}
