# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Design

[Server Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAOLAC2KF1hKd+hYsFIAZCAHMkablV78GQ0gCUUkgM5gowAhGmVZIPrgGNhzACLbgAQRCG1azABMrAI2BqUMJ68yZxUBAArtgwAMRowFQAnjAq6praSLrhAO4AFkhgfIiopAC0AHzk+pQAXDAA2gAKAPJkACoAujAA9EGeUAA6aADeHZSRnAA0MLgOqdBOoyjswEgIAL6YMpQwRXGqSBpaOno8hhVQWxqUABQDUEMoo+Nqk1DTMLPzCACUmPHbiXsyvOvFSxgGx2FAOCriFBgACqnQunWuHyBIPsagB3lcFTIAFERNi4A0YJdrjAAGaBdhEzqYZG2VHrDZfHZJXR-Q4wNBBBAIT4nH7JfYGLwbWmg8EwEDHbQoWHnYkcG5jDz3KZIqx0sFojY+CoASQAcjilIT5SMlRMpjM5gsYAaGrUqbQmfzWfp-iL1WK1BVJShpdYgmB0vDBgq1cCNQ50TrbYbscbHVcFaNgIH0g0IABrdCx+0wVNBmme+n5Rl83YCtkoCoF9NZ9C8hIV10HYUbVZQGtpjPZtArUqwUvFXLoMAVABMAAZJz1erWe+hlugnH4AsFQmFoMJITAxJI0GlMtlMCOCsUOxUavVmm1PA4BbPTYq7g8nMsO+i91IqxUEBIpCGSZmi+qqYF+gruoCxaahCUKylAgGIkWEZetGmLkLi+ImgiybmiqjxkhSibISimpoXqcYJvO9YHnaDq1iRkZahs4E-vm3Y0WB-4QYYDLngOXZBgufYfkO8DIKOE7TrO1G9kuaArv4gQhOEAQoDme7BMwYQZFkOQSWeJTUOUVTmJhDTYi0rR3moD6RBxvb9sZg7FKxbrsn+4haWcsnoB8bmtgCoqohUNBQEgpLRAGQY+Q5fmMahRQxhQCAoOA7FCTRhEQJS0XpM4rh8cFMEZekMBhRFqArsVUZDgFQqCWVFWklVXH7lWQXQeKTgoKl2R5bFmW9uGpG1cUMZmXiFmlcJsalQVfG7txbE2QKzwKSgK71e67YCTAq0pMum1OWFi2nmOMC9AdB5HW+MBTpOmDLquykbscTi7t8rAKmiOlHvpeTMLtzmXiw2I3q04gKj0vkiQO6JsJwbHIBoiNgoNdbDZgaMdUUNXeuVlARVFaYY8JI1MQCyW9WlzCw9luVpgtQ7441hPhS1x343xOPudWpXs5Vx284FHooSFMCQmAaMiN8ZM0RTiXjehOJ4gSks-QziZo2iEUzVlAC8+uOT4i2sxrnCyxoCUlhsIsNRbKBW1g9s7fxIOO87J1rGJ51STOfRQ5b3zyYpa4qWE2BBFA2CpfAUrZN9nCHnpJ4GUD7thZedSNFZQcoDDcVoLO+f6gq77w3bCpsb60po-LvajKXYbY9XfOdeLJXNSTMWw4rJaFNTqXpfT5I5TAeXM3jXUE7Wguc9VM8823rZs81rWu7xYujbPThOPXsNNwqZecB8Ntkdq6EGkahKH47J9eHR2ua1PUGd+K+e6uY5+1VXSN8xCBUX9W7-1FsDLOjtgGiQ2H7e60lA5APMKHF665wjRF6n+VIMAABSEApBJy8GEVwCBQCZjToDdEF4qjVGhBDfOhchroFnOdOAEA-xQCPpwL+FdnIIxXg7AAVngtAB8i6jBYWw6AnCUBfw+JvYUhRzbdwGn3H+WpB4qxpiPIuWtJ6mxZjPNeRMF5qOXqAh2c917C34ZBGA5sWoKXrp-cw-cL7KyxJhdW+cYAADElC1AALKOyjHrZxMAjbONfnYwxjsyTBAUqYsS8jAHJ1JPElc8j0TmxYHBToX8G7xW5pfDxatCTwS-r4-xQTLgwD1vTI2sMonm0uMAopf8uAAMTMAzJ28mI1j3tUEh6CoAZnri08w4j06sPYdI2RiT3GxhvpAiZxt0CTMBtM6AuYHT50GcAYZaImkxNjvstYwA96bRgJAR2iT2lsROcM-MFyPrXPziAjpYDM4mQeWc55VyIA3OgcOdO-tZw-NgOcnqLyAX52QUpVBYQAj7PEn6CF2BY6EBZAeP6qdzqUL2pUSa4NLJtB8N7Fy0T34ExAKlPAyJ3CeDOK4saGIKhEumpcNQ0iuWrLQI4fRvSvQVBcMCBlXgaWoq5kvMSVCRXADFRKWl71yVnRBXAgOvQ5UKolXgTayCgA)

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
