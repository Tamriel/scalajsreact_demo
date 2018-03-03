package app

import CssSettings._

object CSS extends StyleSheet.Inline {
  import dsl._

  val backgroundGrey = rgb(248, 249, 250)
  val brightViolet = rgb(234, 234, 255)
  val veryBrightViolet = rgb(249, 249, 255)
  val veryLightGrey = style(color(rgb(217, 217, 217)))
  val lightGrey = style(color(rgb(175, 174, 174)))
  val grey = style(color(rgb(105, 105, 105)))
  // replace bullets with icons (doc: https://stackoverflow.com/a/25602272/1655547 )
  val iconUl = style(listStyleType := "none")
  val iconLi = style(position.relative, paddingLeft(33 px), &.before(position.absolute, left(0 px), top(2 px)))
  val checkBox = style(addClassName("icon-check-empty"), fontSize(23 px))
  val checkedCheckBox = style(addClassName("icon-check"), fontSize(23 px))
  val angleRight = style(addClassName("icon-right-open"))
  val angleDown = style(addClassName("icon-down-open"))
  // 'inlineBlock' aligns the text of items without expand arrow with the text of items with expand arrow
  val expandIcon = style(display.inlineBlock, width(18 px), marginLeft(8 px))
  val taskIcon = style(display.inlineBlock, width(26 px), paddingLeft(3 px))
  val marginBeforeText = style(marginLeft(9.2 px))
  val input =
    style(
      marginLeft(7 px),
      height(45 px).important,
      addClassName("form-input"),
      width(80 %%),
      paddingLeft(1 px),
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
                      borderColor(rgb(221, 221, 221)))
  val tab = style(addClassName("tab-item"))
  val active = style(addClassName("active"))
  val bigCenteredColumn = style(addClassName("col-10 col-xl-12 col-mx-auto"))
  val smallCenteredColumn = style(addClassName("col-6 col-mx-auto"))

  style(
    unsafeRoot("kbd")( // style for keyboard keys
      padding(3 px, 5 px),
      border(1 px, solid, rgb(198, 203, 209)),
      fontSize(17 px),
      backgroundColor(rgb(250, 251, 252)),
      color(rgb(68, 77, 86)),
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
