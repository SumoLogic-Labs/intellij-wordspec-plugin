# Release process

The plugin is released to Sumo account in JetBrains marketplace under the name `Sumo Logic WordSpec`.

To do that:
- Create an IntelliJ account with sumologic email (if you haven't done that already)
- Ask someone from the DPT team to add you to the Sumo Logic Jetbrains organisation (https://plugins.jetbrains.com/organizations/SumoLogic) and as a colaborator of our plugin
- Create a personal token (My profile -> My tokens) and save it somewhere safe (1Password)
- Run `./gradlew publish -PintellijPublishToken=<YOUR TOKEN>` command.
