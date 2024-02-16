# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Design

[![Sequence Diagram]](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAOLAC2KF1hKd+hYsFIAZCAHMkablV78GQ0gCUUkgM5gowAhGmVZIPrgGNhzACLbgAQRCG1azABMrAI2BqUMJ68yZxUBAArtgwAMRowFQAnjAq6praSLrhAO4AFkhgfIiopAC0AHzk+pQAXDAA2gAKAPJkACoAujAA9EGeUAA6aADeHZSRnAA0MLgOqdBOoyjswEgIAL6YMpQwRXGqSBpaOno8hhVQWxqUABQDUEMoo+Nqk1DTMLPzCACUmPHbiXsyvOvFSxgGx2FAOCriFBgACqnQunWuHyBIPsagB3lcFTIAFERNi4A0YJdrjAAGaBdhEzqYZG2VHrDZfHZJXR-Q4wNBBBAIT4nH7JfYGLwbWmg8EwEDHbQoWHnYkcG5jDz3KZIqx0sFojY+CoASQAcjilIT5SMlRMpjM5gsYAaGrUqbQmfzWfp-iL1WK1BVJShpdYgmB0vDBgq1cCNQ50TrbYbscbHVcFaNgIH0g0IABrdCx+0wVNBmme+n5Rl83YCtkoCoF9NZ9C8hIV10HYUbVZQGtpjPZtArUqwUvFXLoMAVABMAAZJz1erWe+hlugnH4AsFQmFoMJITAxJI0GlMtlMCOCsUOxUavVmm1PA4BbPTYq7g8nMsO+i91IqxUEBIpCGSZmi+qqYF+gruoCxaahCUKylAgGIkWEZetGmLkLi+ImgiybmiqjxkhSibISimoMsU4E-vm3b1n2lFuoY5ElNQ5TUUGC59h+Q7wMgo4TtOs7zrRS5oCu-iBCE4QBCgOZ7sEzBhBkWQ5LxZ7MTQnZVOYmENNiLStHeagPpENG9v2LGDhR-4Qeyf7iPJZxCb2Hz0a2TEXraaBUMAyAuNkHGOaZ6AfFxGynmOMBTpOmDLquEkbscTi7t8rAKmiilHipeTMO2A6Xiw2I3q04gKj0TkNlxxRsJwVHIBo1VgoF7G0R8DVVuioqorBYANSI3xNXWzkkZGWrFDGOJ4gSMAlZwaLkhAlKXA1aJIKSbGDTmAC860cc4rhMZ1MHTQqfUaMNqHcW1DHVsdnCnVgV1udxHkzSg93mRpTHhfxM59K990iWJa6SWE2BBFA2AIF4cBStkqWcIeyknqpOXnnlVR1I0+mvWVQVoLOr36gq74Duij1Cj6sMoA1A0caMhNhpg5OQTAh3isAThODT5VoPTCpE5w4akVG2roQaRqEjzfOcALXh2g6S1pTAe0HdB4qvbq5jnSWGzM+yGta3rbZoxZEIKprH1rNx32RQJf3m+YgNxeu4TRCg3IQKkMAAFIQFI8NeGErgIKAmbI9l6IeTU0JFTjJnNb2s7hXAEB-lA0soJrJMWWTCpUQAVn7aDc3jozJ6n0AZ5rrV59dHVq96ZJSFzCpnAbQsjWhWKYVNr0wAAYkotQALK3WRq1j5rMDbQbKvcWzjd96SwSidrZGXbXrZmwjy9BKvRv1yhXX5pz1Qh27UAZiXCfoGXKMp2nVfmB3F1jWLcYJgbKalzx2UP9AuYHSvTPsAC+aI54eiPkdSGoC1gcycCgJKkAx5rxFrrTeFMxjnzgZzRBMBkGvSZhg90uVTZYNgbAeBeCCEKktoOMKKMfqzhgRfE+CCkEQDHk7cSLswgBFATxP0lDsCQ0ICyA8GUkbhUjujSo2k8S6X0j4Ohh9haNxAFDPAyJ3CeDOC-Esb8KjyMKtiRMagM7mJ2rRRwPhVZQPFH5YAOivAaKEYg1BWpSEaQqI45xEpNGJRUdbRhttfq9F8R4FxATEFOyAA)

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
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
