enablePlugins(ScalaJSBundlerPlugin)

name := "TreeNote"

scalaVersion := "2.12.4"

scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.2",
  "com.github.japgolly.scalajs-react" %%% "core" % "1.1.1",
  "com.github.japgolly.scalajs-react" %%% "ext-monocle" % "1.1.1",
  "com.github.julien-truffaut" %%% "monocle-core" % "1.4.0",
  "com.github.julien-truffaut" %%% "monocle-macro" % "1.4.0",
)

// for monocle
addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

npmDependencies in Compile ++= Seq(
  "react" -> "15.6.1",
  "react-dom" -> "15.6.1"
)

