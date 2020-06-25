# Gradle project for http://demo.prestashop.com testing
Technologies used: Java 11/Selenide

## How to start tests from command line
1. Execute following command from command line: `gradle cleanTest test`

## How to start tests from IntelliJ IDEA
Prerequisite: Lombok plugin should be installed
1. Open build configuration
2. Choose Gradle
3. Choose prestashop project
4. Enter gradle tasks: `clean test`
5. Run tests
![Idea how to start tests steps](./readme_files/IdeaRunTests.png)
