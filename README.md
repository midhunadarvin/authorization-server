
## AFCAS Authorization Server - Spring Boot

A Java Spring Boot / PostgreSQL port of "A fairly capable authorization sub system"

Based on : https://www.codeproject.com/Articles/30380/A-Fairly-Capable-Authorization-Sub-System-with-Row

### Prerequisite

All the scripts in the `sql` folder must be run in the postgres database where we will be persisting the authorization permissions.

Gradle : https://gradle.org/install/

### Build

To build a JAR file with all the dependencies :

``gradle build``


### Run

The JAR build file will be created at `app/build/libs/authorization-framework-<version>.jar`

```java -jar authorization-framework-<version>.jar```

### API Documentation

This spring boot project uses swagger and OpenAPI 3 for documentation

Documentation can be seen at this URL after running the server.

http://localhost:8080/swagger-ui/index.html