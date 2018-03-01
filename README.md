# TreeNote Client

# Import in IntelliJ
- 'File' -> 'New' -> 'Project from existing sources'
- Check 'Import project from external model': sbt

# Run
- In a command prompt: Compile by executing `sbt` and then `~fastOptJS::webpack`. It keeps compiling on changes.
- In IntelliJ: Rightlick the file `index-dev.html` and select 'Open in browser'

# Develop
- Format your code with [Scalafmt](http://scalameta.org/scalafmt/):
	- Open `Settings > Plugins`
    - Open `Browse repositories`
    - Search for `scalafmt`
    - Restart IntelliJ
    - In the scalafmt plugin settings, activate the `Format on save` setting
- You may want to use the IntelliJ plugin [Rainbow Brackets](https://plugins.jetbrains.com/plugin/10080-rainbow-brackets)

# Routes
The app shall be deployed to a folder called `secret`, so the base url will be `www.treenote.org/secret`.

# React
`<div().ref(mainDivRef = _)` sets a reference ([doc](https://github.com/japgolly/scalajs-react/blob/master/doc/REFS.md)).

# CSS
- `col-5 col-xl-6` results in a width of 5, but a width of 6 on screens < 1280px
- `col-ml-auto` means 'margin-left: auto' and results in right alignment