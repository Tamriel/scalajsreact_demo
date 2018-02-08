# TreeNote Client

- IntelliJ: Import, check 'Use sbt shell for build and import'
- Compile by executing `sbt > ~fastOptJS::webpack` in a command prompt or `~fastOptJS::webpack` in the 'sbt shell' of IntelliJ
- Drag'n'drop the file `index-dev.html` to your browser


# Development

- Format your code with [Scalafmt](http://scalameta.org/scalafmt/):
	- Open `Settings > Plugins`
    - Open `Browse repositories`
    - Search for `scalafmt`
    - Restart IntelliJ
    - In the scalafmt plugin settings, activate the `Format on save` setting
- You may want to use the IntelliJ plugin [Rainbow Brackets](https://plugins.jetbrains.com/plugin/10080-rainbow-brackets)
