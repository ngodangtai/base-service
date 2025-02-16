## Tips&Tricks
mvn dependency:tree -Dverbose -Dincludes=javax.servlet
## Introductions
At first, We want to create a "Service Base" is the base for other services in microservices architecture.
The service will have a database and basic connections.
We develop microservices, we need to follow the following best practices:
1. Use separate data storage for each microservice
2. Keep code at a similar level of maturity
3. Separate build for each microservice
4. Assign each microservice with a single responsibility
5. Design stateless services
6. Adopt domain-driven design
7. Design API compatible with micro frontend
8. Deploy into containers
9. Orchestrating microservices

Included:

    restful api with sercurity JWT

    consumer Kafka, RabbitMQ, Redis

    database Oracle (JPA, custom SQL), S3 Storage

    index ElasticSearch

    cache Redis (ehcache, hash Tables, custom object)

    distributed lock (redis)

    notification SMS, email, inbox

    publisher Kafka, RabbitMQ, Redis

## set profile
-Dspring.profiles.active=default
=======
## Run
mvn spring-boot:run -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector
java -jar target/service-base-0.1.2-SNAPSHOT.jar --Log4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector --AsyncLogger.WaitStrategy=busyspin

## Step deploy
Git: https://github.com/ngodangtai/service-base.git
branch: release/V1.0
Database: SQL
Configs:
RabbitMQ
Vhost
{code}
Kafka
{code}




