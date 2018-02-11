package app

import CssSettings._

object CSS extends StyleSheet.Inline {

  import dsl._

  val visible = style(display.block)
  val invisible = style(display.none)
  val selected = style(backgroundColor(orange))
  val mainDiv = style(
    height(100 %%),
    backgroundColor(lightgrey)
  )

  style(
    unsafeRoot("html")(
      height(100 %%),
      margin.`0`,
      padding.`0`,
    ),
    unsafeRoot("body")(
      height(100 %%),
      margin.`0`,
      padding.`0`,
    ),
    unsafeRoot("#root")(
      height(100 %%)
    )
  )
}
