# GoGoLand
A INF219 project at University of Bergen

## Prerequisite
- Java version 8 or 9
- GoLang

## Installation Guide
To develop the application, you need to install Java with Gradle. You also need Golang with the packet gobot, which can be installed by: 
- ```$ go get -d -u gobot.io/x/gobot/... ```

### Developing Java-application
To Run the application in developer mode, you can either start the main method for ``` Game.java ```, or ``` gradle run ``` in console. If you want to build the application, you can use ``` gradle build ```. If a the Go-application is not running, the program will start it and close it when it's closed itself.

### Developing Go-application
To run the Go-application in developer mode, you cd to ``` .../GoGoLand/src/main/go/ ```, and use 
- ``` go build . ``` 
- ``` go.exe ```
If you want to build the Go-application, you only do step 1. If the Go-application is running when you start the Java-application, the Java-application will recognise the Go-application and use it. As long as the Go-application is running on port 9001.

### Downloading the game
The game will be available to download when the beta-version is finnished. It will be possible to download the server as a separate application, but also included in the game.
