package app

import CssSettings._

object CSS extends StyleSheet.Inline {

  import dsl._

  val visible = style(display.block)
  val invisible = style(display.none)
}
