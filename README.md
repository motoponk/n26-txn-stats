# N26 Code​ ​Challenge

How to run?

`> ./mvnw spring-boot:run`

Run tests: 

`> ./mvnw test integration-test`

Test REST API Endpoints:

1) POST /transactions

Get Epoch timestamp from https://www.epochconverter.com/

`curl -i -H "Content-Type: application/json" -X POST -d '{"amount":25.50,"timestamp":1524056549}' http://localhost:8080/transactions`