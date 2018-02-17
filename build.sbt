enablePlugins(ScalaJSBundlerPlugin)

name := "TreeNote"

scalaVersion := "2.12.4"

scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.2",
  "com.github.japgolly.scalajs-react" %%% "core" % "1.1.1",
  "com.github.japgolly.scalajs-react" %%% "extra" % "1.1.1",
  "com.github.japgolly.scalacss" %%% "core" % "0.5.5",
  "com.github.japgolly.scalacss" %%% "ext-react" % "0.5.5",
  "com.softwaremill.quicklens" %%% "quicklens" % "1.4.11",
  "com.lihaoyi" %%% "pprint" % "0.5.3",
  "io.circe" %%% "circe-core" % "0.9.1",
  "io.circe" %%% "circe-generic" % "0.9.1",
  "io.circe" %%% "circe-parser" % "0.9.1"
)

npmDependencies in Compile ++= Seq(
  "react" -> "15.6.1",
  "react-dom" -> "15.6.1"
)

// improve the performance of the npm bundling process
webpackBundlingMode := BundlingMode.LibraryOnly()
emitSourceMaps := false