# BookShop Web App

## Main Technologies:

1. Spring Framework
2. Thymeleaf
3. PostgreSQL
4. OAuth2
5. JWT
6. SMTP
7. smsc.ru API

## Build and Run
1. Go to the project folder and build a jar
```
mvn clean build
```
2. Provide values for env variables and run
```
java -DAPP_PORT= -DDB_PASSWORD= -DDB_PORT= -DDB_USERNAME= -DDB_NAME= -DEMAIL_USERNAME= -DEMAIL_PASSWORD= -DGOOGLEBOOKS_KEY= -DGOOGLECLIENT_ID= -DGOOGLECLIENT_SECRET= -DYOOKASSA_KEY=
 -jar target/MyBookShopApp-0.0.1-SNAPSHOT.jar
```
