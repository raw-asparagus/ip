# Dusk - Task Management Application

A Java-based task management application that helps you organize and track your tasks efficiently.

## Prerequisites

- JDK 17
- IntelliJ IDEA (latest version recommended)

## Project Setup in IntelliJ IDEA

1. Open IntelliJ IDEA (if you're not in the welcome screen, click `File` > `Close Project` to close any existing project)
2. Open the project:
   1. Click `Open`
   2. Select the project directory
   3. Click `OK`
   4. Accept defaults for any additional prompts
3. Configure JDK:
   - Set project SDK to JDK 17 as explained [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk)
   - Set **Project language level** to `SDK default`

## Project Structure

- `src/main/java`: Contains all source code
   - Primary entry point: `dusk/Dusk.java`
- `src/test/java`: Contains test cases
   - Comprehensive unit tests for tasks, storage, and commands

## Building the Project

The project uses Gradle for build automation. Key details:
- Java version: 17
- JavaFX version: 17.0.10
- JUnit version: 5.10.2

To build the project:
```bash
./gradlew build
```

To create an executable JAR:
```bash
./gradlew shadowJar
```

## Running the Application

1. Locate `src/main/java/dusk/Dusk.java`
2. Right-click and select `Run dusk.Dusk.main()`
3. If configured correctly, you should see:
![User Interface](docs/Ui.png)

## Development Guidelines

- Keep Java source files within `src/main/java` folder structure
- Follow the test-driven development approach with comprehensive unit tests
- Maintain code quality standards using Checkstyle (version 10.2)

## Current Version

0.3.0