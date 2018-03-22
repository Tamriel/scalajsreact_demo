enablePlugins(ScalaJSBundlerPlugin)

name := "TreeNote"

scalaVersion := "2.12.5"

scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.5",
  "com.github.japgolly.scalajs-react" %%% "core" % "1.2.0",
  "com.github.japgolly.scalajs-react" %%% "extra" % "1.2.0",
  "com.github.japgolly.scalacss" %%% "core" % "0.5.5",
  "com.github.japgolly.scalacss" %%% "ext-react" % "0.5.5",
  "com.softwaremill.quicklens" %%% "quicklens" % "1.4.11",
  "com.lihaoyi" %%% "pprint" % "0.5.3",
  "io.circe" %%% "circe-core" % "0.9.1",
  "io.circe" %%% "circe-generic" % "0.9.1",
  "io.circe" %%% "circe-parser" % "0.9.1"
)

npmDependencies in Compile ++= Seq(
  "react" -> "16.2.0",
  "react-dom" -> "16.2.0"
)

// improve the performance of the npm bundling process
webpackBundlingMode := BundlingMode.LibraryOnly()
emitSourceMaps := false