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
```
    ./mvnw clean package  
```     

    _Requires Maven (I'm using v 3.3.1), Java JRE (version 1.8.0 or higher)_
    
- To run:
```
    java -jar target/mixer-0.0.1-SNAPSHOT.jar --source=donal --addresses=alpha,beta,gamma  
```   

### Comments

I ran out of time, so just implemented one trivial unit test (really should have more 
unit/integration tests)      


