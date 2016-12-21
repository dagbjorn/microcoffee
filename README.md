 :construction: Under construction...

# microcoffee - The &micro;Coffee Shop

## Acknowledgements
The &micro;Coffee Shop application is based on the coffee shop application developed during Trisha Gee's fabulous talk, "HTML5, Angular.js, Groovy, Java, MongoDB all together - what could possibly go wrong?", given at QCon London 2014. A few differences should be noted however, microcoffee uses a microservice architecture, runs on Docker and is developed in Spring Boot instead of Dropwizard in Trisha's version.

## The application
The application is made up by four microservices, each running in its own Docker container. Each microservice, apart from the database, is implemented by a Spring Boot application.

### microcoffee-database
Contains the MongoDB database. The microservice is based on the [tutum/mongodb](https://hub.docker.com/r/tutum/mongodb/) image on DockerHub.

The database installation uses a Docker volume, mongodbdata, for data storage. This volume needs to be created before starting the container.

### microcoffee-location
Contains the Location REST service for locating the nearest coffee shop. Coffee shop locations are based on offline data from [OpenStreetMap](https://www.openstreetmap.org) loaded into the database.

### microcoffee-order
Contains the Menu and Order REST services.

### microcoffee-gui
Contains the application GUI written in AngularJS. Nothing fancy, but will load the coffee shop menu from which your coffee order may be selected and submitted. The user may also locate the nearest coffee shop and show it on Google Maps.

## Development platform
The microcoffee application is developed on Windows 10. Tested on Docker 1.12.2 running on Oracle VM VirtualBox 5.1.8.

Tip: Install [Docker Toolbox](https://github.com/docker/toolbox/releases) to get all necessary tools.

## Building microcoffee
Clone the project from GitHub, https://github.com/dagbjorn/microcoffee.git, or download the zip file and unzip it.

Use Maven to build each microservice in turn. (Spring Boot applications only.)

In microcoffee-location, microcoffee-order and microcoffee-gui, run:

    mvn clean package docker:build

For testing individual projects outside Docker, run:

    mvn spring-boot:run

## Application and environment properties
Environment-specific properties are defined in the following files:

Project | Production | Integration testing
------- | ---------- | -----------------
microcoffee-location | application.properties | application-test.properties
microcoffee-order | application.properties | application-test.properties
microcoffee-gui | env.js | n/a


