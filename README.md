# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Design

[Server Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly&shrinkToFit=true#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5xDAaTgALdvYoALIoAIyY9lAQAK7YMADEaMBUljAASij2SKoWckgQaAkA7r5IYGKIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD00QZQADpoAN4ARKOUSQC2KDPNMzAzADQbuOpF0Byr61sbKIvASAhHGwC+mMJNMHWs7FyUrbPzUEsraxvbM12qn2UEOfxOMzOFyu4LubE43FgzweolaUEy2XKUAAFBksjlKBkAI7RNRgACU90aoie9Vk8iUKnUrUCYAAqmNsV8fpT6YplGpVLSjDpmgAxJCcGAcyh8mA6VLc4DLTB8xmCp7I6kqVpoaIIBBUkQqYVqgXMkDouQoGU4r583nadXqYXGZoKDgcaVjPlGqg055mpmqZqWlDWhTRMC+bHAKO+B2qp3moXPN0er2R6O+lEm6rPeFvFrpDEEqAZVT6rCFxGa+oPd4wT5jH7rVrHAFx6O9CAAa3QbY2tz9lDr8GQ5laACYnE5Js2FsqVjB2-8Nl3fD3+2hB8c7ugOKZIjE4vFoAFDAAZCBZQrxEplCoTmr1xqNzo9AbDAzqfJoedzC2S7XBCQIgmCe4jki9Q1o2C7fMB4IAmBBwgXCry1lqxooK0CA3pK2LXrexKkjklK5oYgbJsGLIoOynJKssjoMimrqihKUq2nKCowIxYhBhq+avthzRcdofoBnS1GCqGVrlFmMYbomAkummooZjACk5tqlEFhhjZEZKFZVpgsFIlhTQfIBi7LLua4zBuW4DiuQ4zHcDbmfUlSTjAM5ztM1kIbZLkduu8ZOTuIXDgeR5RLECSRCg6AwERMTMPepTlJg3kvg01DvtIACil6Fb0hWDEMxiTI5fboFBwpma0NXbqZ+nQXl-o6jAeH2GlhE3mlJFkuROmmtJzIwE0SAAGaWApsbxsp42pvUboAOJ0Zpi3iSpqYWaiW3ZuJFFjSxNFGCg3DyfGC3drVaDMfywZsa00iXXRhjNXVJ1CS8CLvKlUYlpWCDVm1Y4eR87lvp545VGA06zvO+6cLFJ4JOiXqXpiMBrUuQoZY+2XPswFnvmtpUVfYS7VeF931Xp-3Fl9aCtUzwoUbhmJ48sqi3Zu90jdhp1PTJk2UDNc03Szj3OitIqtBtzBaTty0Qzp7rxjIx2jVRZ1i6yPMoNjOT8xFsusWpiubUbJtYLt6siTA1PLHbEl5ozRYskubtmY7lmzC7KAAJLSKsMChFOADMAAsAIPuUtqtmsEI6AgoC9kniEzBCQcAHJLinNwwP00P5bDOUI75SMBUHofh5Hsfx5lNpAbZKcAmnGdZ+3OcAvnhc58XpdmKjERxae2DRFA2DcPAcmGEbxQt8T8MczDrQfn0VM00kdPbvOGwD736xl48nuIs0UxH0uBcnzA6Hs-tXVhtaRtm-d2zHygQudZRUn6wmlNWa80ZZJkAfLN0uNNoq3kOA0Wqln44UOgmHWws9YIJDDAYAnp34sy-rfJcFtnpWw0kbeBct-aoiNqHd2ukYJtWaDQ6QbMiz+zgjfZY9dWiNxjg-BmzxK6I38tfGYdcw48OjnwlGh5x7o3iJYS6eEigwAAFIQElLjJcCQu4gF7Kvcw69y6bw6GyL8Qwg60zugfaYOx06KKgHACAeEoBHAAOosGDmVIYAAhS8Cg4AAGlc5Lm4RHKR-CPINUYU2OxwAHFOJce4zx3i-EBOCf3UJEjwmx34X7JBrQABWGi0B4P3ugbYldEnQAIVw6Qv9JLawga0YBUtowf23MQwSq1RRKxQb6B2v1Ob9LQX-EWctWjTUlBwd+4iumqR6a0DiXpyGDIKUbbS6CAGYNomADo9jKDVKgBQy2izoHMCOSckhBSYD7PiYc5x0A6HjJTE1T0dyHE9nfl8UOlSSZHNqSHepVzukKw0h80ckAtEqh+hfRsbJsBaHKNCsQ+SybFimGfUcv0hHVxETItG8V4iRHieOcMsAFDYFnoQPIBRl5E0rkYyy7QiolTKhVYwDMGFMyhqwzCwk-6tBANwPA2IGl5m2RMmAwryV8j0AYMVIKFkKzJXgJpuh9D8TVkMjWMA9QGmeb9RqqrYDGVBny7F6LeVRJxSTYRyNR6yMwEAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
