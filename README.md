## Jobcoin Mixer

### How to Use

This mixer application allows coins from an input address to be mixed into a set of target addresses 
(specified at input).

The application takes the source and target addresses as input parameters as follows:

```
java -jar target/mixer-0.0.1-SNAPSHOT.jar --source=d[source-address] --addresses=[targets]
- where [targets] = a comma-delimited set of target addresses
```

The application will print out the set of target addresses & associated coin totals at the end 
(or any relevant error message). The address balances may also be verified via the online UI tool

### Design/Components

The application is coded in Java and Spring Boot. _(Spring Boot is really too heavyweight for this problem
 but it's something I was able to get up & running quickly.)_
 
The basic flow is as follows:

- The application entry-point is a main class, MixerApplication.java
- This class interacts with a service class, MixerService.java to execute mixing services:
    - mixCoins()
    - getMixedCoins()
- The service class interacts with an Http Client component, HttpClient.java, to interact with 
the Gemini REST services to query address/transaction info and to perform coin transfers.
    
### Assumptions

- Send transactions dont happen instantly - you'll need to poll afterwards to verify when complete
(I didnt fully implement this polling logic - but added place-holders in a few places)
- Based on template examples, I'm assuming we dont need to include sending to/from the "house" address
(But - adding the extra steps to transfer coins to the House address would be relatively straightforward)
- Currently, the mixer will mix all coins from the source address (might be better to take as 
parameter a number of coins from the source address to mix)
               
### How to Build/Deploy

- To build:
    ./mvnw clean package
    Requires Maven (I'm using v 3.3.1), Java JRE (version 1.8.0 or higher)
    
- To run:
    java -jar target/mixer-0.0.1-SNAPSHOT.jar --source=donal --addresses=alpha,beta,gamma    
    
### Comments

I ran out of time, so just implemented one trivial unit test (really should have more 
unit/integration tests)      



## Button Service

### Specification

https://www.usebutton.com/developers/partner-engineer-coding-challenge/

### Overview

A simple REST-based service & corresponding client website to allow the following:

* User authentication via Facebook (OAuth)
* A search page to allow the user to search:
   * All posts
   * Posts for a particular date (in the format mm/dd/yyyy)
   * Posts containing a matching hash tag (in the format "hashtag" or "#hashtag")
   
The REST service is built in Java, using:
* Spring Social framework to handle redirect to the Facebook login dialog, and the resulting redirect
* JAX-RS framework to provide REST end-points for User and Post resources
* The restfb framework to allow querying of the Facebook Graph API for user profile & post data

The web client is built using AngularJS

### Pre-requisites 

You will need the Java JDK (v 1.8) and Maven (v3.3.0 or greater) to build and run the application

## To build & start the service

```
# Start the service
$ mvn jetty:run
```
This will start a local web server instance, running on ``localhost:8080``

### Unit Tests

There are unit tests to verify the REST service functionality, located in ``src\main\test``

Run the tests as follows:

```
$ mvn test
```

### Caveats & Assumptions

I was unable to get the posts for the friends of the authenticated Facebook user. Far as I can tell (from the FB Graph API) this data is no longer available through the API -- so I have skipped that part of the assignment.

I also was not able to query the Graph API using a hashtag as a query value, so instead, I retrieved all posts & iterated through the list to get the matching posts. 

I believe that the Facebook App I created through the FB dashboard is a test app, so I need to add facebook users as testers through the dashboard in order for them to be able to use this test application. Please contact me if you dont seem to be able to successfully use the application with your Facebook ID. _(I'm not too familiar with this Facebook app configuration.)_ 



                           