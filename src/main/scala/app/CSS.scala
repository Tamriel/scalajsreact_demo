package app

import CssSettings._

object CSS extends StyleSheet.Inline {
  import dsl._

  val backgroundGrey = c"#f8f9fa"
  val brightViolet = c"#e2e2ff"
  val veryBrightViolet = c"#f9f9ff"
  val veryLightGrey = style(color(c"#d9d9d9"))
  val grey = style(color(rgb(105, 105, 105)))
  // replace bullets with icons (doc: https://stackoverflow.com/a/25602272/1655547 )
  val iconUl = style(listStyleType := "none")
  val iconLi = style(position.relative, paddingLeft(33 px), &.before(position.absolute, left(0 px), top(2 px), fontSize(23 px)))
  val checkBox = style(addClassName("icon-check-empty"))
  val checkedCheckBox = style(addClassName("icon-check"))
  val angleRight = style(addClassName("icon-right-open"))
  val angleDown = style(addClassName("icon-down-open"))
  // 'inlineBlock' aligns the text of items without expand arrow with the text of items with expand arrow
  val icon = style(display.inlineBlock, width(16 px), marginLeft(8 px))
  val marginTextToIcon = style(marginLeft(9.3 px))
  val input =
    style(
      marginLeft(6 px),
      height(45 px).important,
      addClassName("form-input"),
      width(90 %%),
      paddingLeft(2 px),
      paddingRight(2 px),
      paddingBottom(2 px),
      paddingTop(9.8 px),
      resize.none
    )
  val row = style(
    borderRadius(4 px),
    height(47 px),
    // vertically center content in the row (doc: https://stackoverflow.com/a/13075912)
    &.before(content := """""""", display.inlineBlock, height(100 %%), verticalAlign.middle)
  )
  val centerVertically = style(display.inlineBlock, verticalAlign.middle)
  val invisible = style(display.none)
  val selected = style(backgroundColor(brightViolet))
  val pointer = style(cursor.pointer)
  val hover = style(&.hover(backgroundColor(veryBrightViolet)))
  val ulMargins = style(marginTop.`0`, marginBottom.`0`, marginLeft(45 px))
  val maximize = style(height(100 %%), margin.`0`)
  val noOutline = style(outline.none)
  val columns = style(addClassName("columns"))
  val smallTopMargin = style(marginTop(10 px))
  val treeDiv = style(marginTop(1 em),
                      backgroundColor.white,
                      borderRadius(4 px),
                      borderStyle(solid),
                      borderWidth(1 px),
                      borderColor(c"#ddd"))
  val tab = style(addClassName("tab-item"))
  val active = style(addClassName("active"))
  val bigCenteredColumn = style(addClassName("col-10 col-xl-12 col-mx-auto"))
  val smallCenteredColumn = style(addClassName("col-6 col-mx-auto"))

  style(
    unsafeRoot("kbd")( // style for keyboard keys
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
    unsafeRoot("body")(maximize, backgroundColor(backgroundGrey), overflowY.scroll), // always show the scrollbar
  )
}
