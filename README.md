# Popular path finder [![Build Status](https://travis-ci.org/slamdev/popular-path-finder.svg?branch=master)](https://travis-ci.org/slamdev/popular-path-finder)

## Run

Prerequisites:
* docker should be installed

Run from root project directory:

```sh
docker build . -t popular-path-finder
docker run -v $(pwd)/src/test/resources/sampleInput.json:/file.json popular-path-finder 2 /file.json
```

## Test

Prerequisites:
* docker should be installed

Run from root project directory:

```sh
docker run --rm -v $(pwd):/home/gradle/project -w /home/gradle/project gradle:jdk8-alpine gradle test
```

## Used tools

* Java 8 and Gradle to build and run project
* FindBugs, Checkstyle and PMD to verify code quality during the build
* Junit 5 to run unit tests
* Google GSON to parse JSON
* Docker (split into build and run on order to reduce resulting image size)
* Travis CI for continuous integration

## Purpose

### Three page path

Given a dataset that represents a user's navigation of a website, find the top N most frequently visited paths.

### Data

The data comes from a web server's access logs where you typically get the following fields: timestamp, IP address, 
request string, response code, user agent and cookies. For brevity, we provide a dataset that has the user and page 
parsed out.

## Sample input

```json
[{"user":"U1","page":"/"},
{"user":"U1","page":"login"},
{"user":"U1","page":"subscriber"},
{"user":"U2","page":"/"},
{"user":"U2","page":"login"},
{"user":"U2","page":"subscriber"},
{"user":"U3","page":"/"},
{"user":"U3","page":"login"},
{"user":"U3","page":"product"},
{"user":"U1","page":"/"},
{"user":"U4","page":"/"},
{"user":"U4","page":"login"},
{"user":"U4","page":"product"},
{"user":"U5","page":"/"},
{"user":"U5","page":"login"},
{"user":"U5","page":"subscriber"}]
```

### Sample output

```json
[{"paths":["/","login","subscriber"],"frequency":3},
{"paths":["/","login","product"],"frequency":2},
{"paths":["login","subscriber","/"],"frequency":1}]
```
