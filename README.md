[![Build Status](https://travis-ci.com/victuxbb/SystemDesigns-TinyUrlKGS.svg?branch=master)](https://travis-ci.com/victuxbb/SystemDesigns-TinyUrlKGS)
# SystemDesigns TinyUrl KGS Service

Key Generation Service (KGS) that generates random six letter strings beforehand and stores them in a database

## Building jar file

To generate the app.jar file it's necessary to have docker installed in your machine

```
./gradlew build
```
This task will up the redis docker database, execute all tests and build the app.jar inside build/lib folder

 
## Run

To run KGS through Gradle you can execute the task but before remember to have docker dependencies up and running so:

```
docker-compose up -d
./gradlew bootRun
```

In bootstrap KGS will generate all possible keys of length 3 (not six, for save time and space) and store them in redis.


## API

You can get keys doing POST request to localhost:8080/keys_request endpoint with body:

```
{
	"quantity": 10
}

```
And you will get as response:

```
[
    "tQiR",
    "sbGH",
    "sUs9",
    "s6U2",
    "tty1",
    "tA62",
    "ttP6",
    "tdoy",
    "sygH",
    "sSDW"
]

```

## Dockerfile

You can build and image from KGS or use it from another project with 
```
docker run --network=host victuxbb/tinyurlkgs 
```

Keep in mind that KGS is pointing to a REDIS in localhost machine, this is enought for this POC.


