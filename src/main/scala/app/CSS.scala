package app

import CssSettings._

object CSS extends StyleSheet.Inline {

  import dsl._

  val visible = style(display.block)
  val invisible = style(display.none)
  val selected = style(backgroundColor.orange)
  val pointer = style(cursor.pointer)
  val maximize = style(
    height(100 %%),
    margin.`0`,
  )

  style(
    unsafeRoot("html")(maximize),
    unsafeRoot("body")(maximize),
    unsafeRoot("#root")(maximize),
    unsafeRoot("li:hover")(backgroundColor.lightgrey),
  )
}
