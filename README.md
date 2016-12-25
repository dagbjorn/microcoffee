# microcoffee - The &micro;Coffee Shop

## Acknowledgements
The &micro;Coffee Shop application is based on the coffee shop application developed during Trisha Gee's fabulous talk, "HTML5, Angular.js, Groovy, Java, MongoDB all together - what could possibly go wrong?", given at QCon London 2014. A few differences should be noted however, microcoffee uses a microservice architecture, runs on Docker and is developed in Spring Boot instead of Dropwizard as in Trisha's version.

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

:bulb: Install [Docker Toolbox](https://github.com/docker/toolbox/releases) to get all necessary tools.

## Building microcoffee
Clone the project from GitHub, https://github.com/dagbjorn/microcoffee.git, or download the zip file and unzip it.

Use Maven to build each microservice in turn. (Spring Boot applications only.)

In microcoffee-location, microcoffee-order and microcoffee-gui, run:

    mvn clean package docker:build

## Application and environment properties
Environment-specific properties are defined in the following files:

Project | Production | Integration testing
------- | ---------- | -----------------
microcoffee-location | application.properties | application-test.properties
microcoffee-order | application.properties | application-test.properties
microcoffee-gui | env.js | n/a

Environment-specific properties comprise:
* Database connection URL (for integration testing, separate properties are used).
* REST service URLs.

## Installation

### Create a Docker volume for the MongoDB database
Create a Docker volume named **mongodbdata** to be used by the MongoDB database.

    docker volume create --name mongodbdata

Verify:

    docker volume inspect mongodbdata

### Load data into the database collections
The microcoffee-database project is used to load coffee shop locations, *oslo-coffee-shops.xml*, and menu into a database called  **microcoffee**. This is accomplished by running the below Maven command. (We run it twice to also load the test database, **microcoffee-test**.)

    mvn gplus:execute -Ddbhost=192.168.99.100 -Ddbport=27017 -Ddbname=microcoffee -Dshopfile=oslo-coffee-shops.xml
    mvn gplus:execute -Ddbhost=192.168.99.100 -Ddbport=27017 -Ddbname=microcoffee-test -Dshopfile=oslo-coffee-shops.xml

## Run microcoffee
From microcoffee-gui, start all four microservices by running:

    docker-compose up

Each project contains it own docker-compose.yml which will run all downstream containers in addition to itself.

For testing individual projects outside Docker, run:

    mvn spring-boot:run

Depending on the project, you also have to start any downstream containers from docker-compose.

## Give microcoffee a spin
After microcoffee has started (it takes a while), navigate to the coffee shop to place your coffee order:

    http://192.168.99.100:8080/coffee.html

assuming the docker-machine IP 192.168.99.100. Check with:

    docker-machine ls

## REST services

### Location API

#### Get nearest coffee shop

**Syntax**

    GET /coffeeshop/nearest/{latitude}/{longitude}/{maxdistance}

Find the nearest coffee shop within *maxdistance* meters of the position given by the *latitude*/*longitude* coordinates.

**Response**

HTTP status | Description
----------- | -----------
200 | Coffee shop found. The name, location etc. is returned in JSON-formatted HTTP response body.
204 | No coffee shop found within specified distance from given position.

**Example**

Find the coffee shop closest to the Capgemini Skøyen office:

    GET http://192.168.99.100:8081/coffeeshop/nearest/59.920161/10.683517/200

Response:

    {
      "_id": {
        "timestamp": 1482086231,
        "machineIdentifier": 5422646,
        "processIdentifier": 19508,
        "counter": 9117700,
        "time": 1482086231000,
        "date": 1482086231000,
        "timeSecond": 1482086231
      },
      "openStreetMapId": "428063059",
      "location": {
        "coordinates": [
          10.6834023,
          59.920229
        ],
        "type": "Point"
      },
      "addr:city": "Oslo",
      "addr:country": "NO",
      "addr:housenumber": "22",
      "addr:postcode": "0278",
      "addr:street": "Karenslyst Allé",
      "amenity": "cafe",
      "cuisine": "coffee_shop",
      "name": "Kaffebrenneriet",
      "opening_hours": "Mo-Fr 07:00-18:00; Sa-Su 09:00-17:00",
      "operator": "Kaffebrenneriet",
      "phone": "+47 22561324",
      "website": "http://www.kaffebrenneriet.no/butikkene/butikkside/kaffebrenneriet_karenslyst_alle_22/"
    }

### Menu API

#### Get menu

**Syntax**

    GET /coffeeshop/menu

Get the coffee shop menu.

**Response**

HTTP status | Description
----------- | -----------
200 | Menu returned in JSON-formatted HTTP response body.

**Example**

    GET http://192.168.99.100:8082/coffeeshop/menu

Response:

    {
        "types": [
            {
                "_id": {
                    "timestamp": 1482086232,
                    "machineIdentifier": 5422646,
                    "processIdentifier": 19508,
                    "counter": 9117791,
                    "time": 1482086232000,
                    "date": 1482086232000,
                    "timeSecond": 1482086232
                },
                "name": "Americano",
                "family": "Coffee"
            },
            ..
        ],
        "sizes": [
            {
                "_id": {
                    "timestamp": 1482086232,
                    "machineIdentifier": 5422646,
                    "processIdentifier": 19508,
                    "counter": 9117795,
                    "time": 1482086232000,
                    "date": 1482086232000,
                    "timeSecond": 1482086232
                },
                "name": "Small"
            },
            ..
        ],
        "availableOptions": [
            {
                "_id": {
                    "timestamp": 1482086232,
                    "machineIdentifier": 5422646,
                    "processIdentifier": 19508,
                    "counter": 9117800,
                    "time": 1482086232000,
                    "date": 1482086232000,
                    "timeSecond": 1482086232
                },
                "name": "soy",
                "appliesTo": "milk"
            },
            ..
        ]
    }

### Order API

#### Place order

**Syntax**

    POST /coffeeshop/{coffeeShopId}/order

Place an order to the coffee shop with ID *coffeeShopId*. The order details are given in the JSON-formatted HTTP request body.

The returned Location header contains the URL of the created order.

**Response**

HTTP status | Description
----------- | -----------
201 | New order created.

**Example**

    http://192.168.99.100:8082/coffeeshop/1/order

    {
        "coffeeShopId": 1,
        "drinker": "Me again",
        "size": "Small",
        "type": {
            "name": "Americano",
            "family": "Coffee"
        },
        "selectedOptions": [
            "soy",
            "decaf"
        ]
    }

Response:

    {
        "id": "58333105410df8000122a59b",
        "coffeeShopId": 1,
        "drinker": "Me again",
        "size": "Small",
        "type": {
            "name": "Americano",
            "family": "Coffee"
        },
        "selectedOptions": [
            "soy",
            "decaf"
        ]
    }

#### Get order details

**Syntax**

    GET /coffeeshop//{coffeeShopId}/order/{orderId}

Read the details of the order of the given ID *orderId* from coffee shop with the given ID coffeeShopId.

**Response**

HTTP status | Description
----------- | -----------
200 | Order found and return in the JSON-formatted HTTP response body.
204 | Requested order ID is not found.

**Example**

    http://192.168.99.100:8082/coffeeshop/1/order/585fe5230d248f00011173ce

Response:

    {
        "id": "585fe5230d248f00011173ce",
        "coffeeShopId": 1,
        "drinker": "Me again",
        "size": "Small",
        "type": {
            "name": "Americano",
            "family": "Coffee"
        },
        "selectedOptions": [
            "soy",
            "decaf"
        ]
    }

## Download geodata from OpenStreetMap

:construction: Just some old notes for now...

Download geodata:
- Go to https://www.openstreetmap.org
- Search for Oslo
- Select a search result
- Adjust wanted size of map
- Click Export
- Click Overpass API (works better)
- Save to file: oslo.osm

osmfilter:
http://wiki.openstreetmap.org/wiki/Osmfilter
osmfilter is used to filter OpenStreetMap data files for specific tags.

Install dir for exe: C:\apps\utils

List of all Keys, sorted by Occurrence:
osmfilter oslo.osm --out-count

List of a Key's Values, sorted by Occurrence:
osmfilter oslo.osm --out-key=cuisine | sort /r

Get all coffee shops:
osmfilter oslo.osm --keep="all cuisine=coffee_shop" > oslo-coffee-shops.xml

