package app

import CssSettings._

object CSS extends StyleSheet.Inline {

  import dsl._
  style(
    unsafeRoot(".editing")(
      display.none
    )
  )
}
