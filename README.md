# N26 Code​ ​Challenge

How to run?

`> ./mvnw spring-boot:run`

Run tests: 

`> ./mvnw test integration-test`

## Test REST API Endpoints:

### POST /transactions

Get Epoch timestamp from https://www.epochconverter.com/

`curl -i -H "Content-Type: application/json" -X POST -d '{"amount":25.50,"timestamp":1524056549}' http://localhost:8080/transactions`

### GET /statistics

`curl -i -X GET  http://localhost:8080/statistics`

## Things to consider:

1. Lazy vs Eager Statistics calculation
    
    If our application is **write intensive**, ie more transactions coming in and less calls to view statistics then
we can calculate statistics lazily (current implementation).
But, if our application receives transactions less frequently and there is continuous monitoring on statistics then
we can consider pre-calculating statistics ie whenever a transaction is created or periodically.

2. Transaction validations?
    * Is it allowed to receive a transaction with future timestamp?
    * Transaction amount should be greater than zero

3. Archiving old transactions

    Currently we are storing all transaction data in memory. So, we should consider removing the old data after certain interval.
We can use Spring's Scheduler support to clean up old data after a fixed interval. 
If we are using any RDBMS to store transactions we can move old data to archive tables.

4. Make transaction allowed window, 60 seconds, configurable in application.properties.