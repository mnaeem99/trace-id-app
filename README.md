# Tracing Soap Application

## Overview

Tracing SOAP Application is a Spring Boot project that provides SOAP endpoints with OpenTelemetry instrumentation for enhanced logging and tracing. This project serves as a template for building SOAP services with modern observability practices.

## Requirements

The fully fledged server uses the following:

* Work with JVM 17
* Language: Kotlin
* Build Tool: Gradle
* Framework: SpringBoot, OpenTelemetry, SLF4J
* Editor: IntelliJ
* API: SOAP

## Dependencies
There are a number of third-party dependencies used in the project. Browse the Gradle build.gradle.kts file for details of libraries and versions used.


The application exposes health endpoint (http://localhost:8080/api).

## Setup

To check out the project and build it from source, do the following:

git clone https://github.com/mnaeem99/trace-id-app.git

## Building and deploying the application

### Building the application

The project uses [Gradle](https://gradle.org) as a build tool. It already contains
`./gradlew` wrapper script, so there's no need to install gradle.

To build the project execute the following command:

```bash
  ./gradlew build
```

### Running the application

Create the image of the application by executing the following command:

```bash
  ./gradlew assemble
```

