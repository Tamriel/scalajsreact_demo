# TreeNote Client

# Install
- Install [SBT](https://www.scala-sbt.org/download.html)
- Install NPM, e.g. with `sudo apt install npm`
- Install a Java JDK, e.g. with `sudo apt-get install openjdk-8-jdk`
- Install 'IntelliJ Ultimate' (needed for the Scala-Plugin)

# Setup IntelliJ
- Install the Scala-Plugin
- 'Import Project' or 'File' -> 'New' -> 'Project from existing sources'
- Check 'Import project from external model': sbt
- Behind 'Project JDK' select 'New' and enter the path to your JDK, e.g. `/usr/lib/jvm/java-8-openjdk`
- Format your code with [Scalafmt](http://scalameta.org/scalafmt/):
	- Open `File -> Settings -> Plugins`
    - Open `Browse repositories`
    - Search for `scalafmt`
    - Restart IntelliJ
    - In the settings, search for `scalafmt`. There, activate the `Format on save` setting
- You may want to use the IntelliJ plugin [Rainbow Brackets](https://plugins.jetbrains.com/plugin/10080-rainbow-brackets)

# Run
- In a command prompt: Compile by executing `sbt` and then `~fastOptJS::webpack`. It keeps compiling on changes.
- In IntelliJ: Rightlick the file `index-dev.html` and select 'Open in browser'

# Documentation
## Routes
The app shall be deployed to a folder called `secret`, so the base url will be `www.treenote.org/secret`.

## React
`<div().ref(mainDivRef = _)` sets a reference ([doc](https://github.com/japgolly/scalajs-react/blob/master/doc/REFS.md)).

## CSS
- CSS basics: The `margin` of an element is the space outside of the element to the next element, whereas `padding` is added space inside the element.
- `col-5 col-xl-6` results in a width of 5, but a width of 6 on screens < 1280px
- `col-ml-auto` means 'margin-left: auto' and results in right alignment

### Icons
- We use [Fontello](http://fontello.com/) to supply just the icons we use in form of a font, from a range if icon packs:
- On Fontello, click the settings symbol -> Import and upload the file `res/ss/config.json`. It defines the icons used in TreeNote.
- Add icons, download the new font, replace `res/font/`, `res/css/config.json` and `res/css/fontello-icons.css`.
- To change the size if icons, change the font size, e.g. in `CSS.scala` define `val myIcon = style(addClassName("icon-arrow"), fontSize(20 px))`
