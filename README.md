README
====


1) Basic assumptions:  <br />
- MySql DB is used to save the data about rentals/returns of movies.  <br />
- There is an already prepared sql file (schema.sql) which initializes DB structure and some customers, movies. <br />
 This file is executed every time the applications starts. This means that the values from tables are reset everytime the application is restared,
 which is perfect for testing but in production it wouldn't work like that.  
- Each movie has only 1 copy.
- There are two operations : rent a movie, return a movie. They accept 1 or more movies.
- If any validation fails during operation, no rental/return is saved id DB.
- If an operation is a sucess, the rental or return of a movie is saved in DB and we assume that user automatically pays requested rent price/surcharges.
- For a successful rent operation, we save new row for each movie in 'rental' table.
- For a successful return operation, we update existing row in 'rental' table ('return_date', 'surcharges' columns are filled). Rows in 'rental' aren't removed to keep track of operations.
- Can't pay less when e.g. you rent a copy for 3 days but you give it back after 1 day.
- No support for administration functions - data provided by sql files. I know it would be great to add it but that would take another 10-20 hours to implement.
- Some constants are kept in constants.properties file so that they can be changed quickly (e.g. price for the rental) 
- Bonus points have no specific usage described in the requirements so they are just saved in DB.

2) Necessary tools:
- JDK 1.8
- Maven 3 (I'm using 3.5.0)
- Mysql database (I'm using version 5.7.20).
- Application code

3) How to run application:

- install all tools mentioned in point 2)
- set up MySql database. See application.properties for url, user and password set up(spring.datasource.url, spring.datasource.username, spring.datasource.password)
- create a new schema 'movies' with a MySql command:

```
 create database movies;
 ``` 
 
  (can be other name if you modify spring.datasource.url property)
- run from command line (also runs junit tests):
 ```
 mvn clean install
 ```
- run from command line:
```
java -jar target/videorental.jar
```

Now application should be running. There should be also Swagger tool running on url:

 [http://localhost:8081/rental/swagger-ui.html](http://localhost:8081/rental/swagger-ui.html)
 
(8081 port can be changed, see server.port in application.properties):
Swagger is very useful for quick REST api testing.


4) Other technical notes:
- there are 4 tables in DB: 'customer', 'movie', 'rental', 'bonus_point'
- application runs with Spring Boot running on Tomcat
- JpaRepository from Spring Data is used to provide easy access to basic CRUD oprerations on entities
- there are some logs generated for tracking requests and responses
- where it was possible, I was using the provided annotations for validation. For more detailed validations, I created validators.
- there are junit tests with Mockito to test the basic success/failure scenarios as well as corner cases 
- each table has 2 extra columns, for keeping track of DB changes: 'last_updated', 'data_insert'

5) Example of usage:

- rent operation

Request body:
```json

{
  "movieRentRequestDtoSet": [
    {
      "movieId": 1,
      "movieName": "Titanic",
      "nbOfDays": 5
    }, 
    {
      "movieId": 2,
      "movieName": "Spider man",
      "nbOfDays": 3
    }
  ]
}
```

Response body:

```json
{
  "rentResponse": [
    {
      "movieId": 1,
      "movieName": "Titanic",
      "nbOfDays": 5,
      "videoType": "NEW_RELEASE",
      "price": 200
    },
    {
      "movieId": 2,
      "movieName": "Spider man",
      "nbOfDays": 3,
      "videoType": "REGULAR",
      "price": 30
    }
  ],
  "errorMessage": "",
  "totalPrice": 230
}
```

- return operation

Request body:

```json
{
  "movieReturnRequestDtoSet": [
    {
      "movieId": 1,
      "movieName": "Titanic"
    },
{
      "movieId": 2,
      "movieName": "Spider man"
    }
  ]
}
```

Response body:

```json
{
  "returnResponse": [
    {
      "movieId": 1,
      "movieName": "Titanic",
      "videoType": "NEW_RELEASE",
      "nbOfDaysLate": 3,
      "surcharges": 120
    },
    {
      "movieId": 2,
      "movieName": "Spider man",
      "videoType": "REGULAR",
      "nbOfDaysLate": 2,
      "surcharges": 60
    }
  ],
  "errorMessage": "",
  "totalSurcharges": 0
}
```