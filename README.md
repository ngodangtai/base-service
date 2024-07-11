## Tips&Tricks
mvn dependency:tree -Dverbose -Dincludes=javax.servlet
## Introductions
At first, We wanna create a "Service Base" is the base for other services in microservices architecture.
Service will has database and basic connections.

Included:

    restful api

    sercurity JWT

    consumer Kafka, RabbitMQ, Redis

    database Oracle (JPA, custom SQL), S3 Storage

    index ElasticSearch

    cache Redis (ehcache, hash Tables, custom object)

    distributed lock (local, redis)

    notification SMS, email, inbox

    publisher Kafka, RabbitMQ, Redis

## set profile
-Dspring.profiles.active=default
=======
## Run
mvn spring-boot:run -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector
java -jar target/service-base-0.1.2-SNAPSHOT.jar --Log4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector --AsyncLogger.WaitStrategy=busyspin

## Step deploy
Git: https://github.com/ngodangtai/base-service.git
branch: release/V1.0
Database: Nan
Configs:
RabbitMQ
Vhost
{code}
Kafka
{code}




