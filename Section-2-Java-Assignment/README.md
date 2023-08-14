# Table of contents
1. How to use?
2. Project Overview
3. Design Patterns used
4. Authenticating and Authorizing Users 
5. Handling Errors, Exceptions and Logging
6. Performance Optimizations
7. Future Improvements
8. Appendix: Initial Draft

# TODO: 
Search And Filter(DONE)
Auth and Auth  (DONE)
Error Handling (Actl this alr done just gotta add more error handlers)
Testing 
Performance Optimization (Low prio maybe use cache)

# How to use? 
Prerequisites: You must have docker engine installed and docker daemon running. 
FYI: I am using macbook pro m2, which uses a ARM64 architecture, in the unlikely event you can not build the image correctly,
you might want to try building the docker image for x86 chipsets. 

1. Navigate to root of Section-2-Java-Assignment on CLI.
2. You should see `docker-compose.yml`. 
3. Optionally, if you would like to seed the database on initialization, i have included it in `Section-2-Java-Assignment/src/main/resources/application.properties`. By default `seed.database=true` is enabled by default, only turn this off if you would not want to seed the DB with mock values. Note that this seed occurs each time the containers spin up. 
4. in the root dir of section 2, run `docker-compose build --no-cache `
5. then run `docker-compose up` 
6. you are good to go! Endpoints should be up and running on localhost:8080 


7. After the containers have initialized and spun up, navigate to http://localhost:8080/swagger-ui/#/ on your browser, you should see OpenAPI Swagger frontend for API documentation. 
8. More info on how to use the APIs: getAllBooks, getAllUsers, loginUser, registerUser are "public" APIs and dont need you to login to obtain a valid JWT. 
9. First you should register a new user, click on registerUser,click on Try it Out, type in username and password of your choosing, leave userRole as is. Then click on the blue Execute button, you should see a response 200 and your username and password hash in the response body.
10. Great! Now that you have registered as a user in our onlinebookstore, you need to log yourself in. Click on loginUser, enter your username and password as before and click on Execute button below. You should see response 200 and in the response body contains a string for your JWT auth token.
11. Copy the JWT auth token to your clipboard and scroll up to find a green Authorize button on the UI. 
12. You should see a textbox "Value", type `Bearer <INSERT_YOUR_JWT_TOKEN_HERE>` without the `<>` brackets. Click on Authorize, you are now logged in and can use the rest of the "private" APIs as Swagger automatically inserts the Bearer token in the header for you for API authorization. 
13. Without a valid Bearer token, you will not be able to use the rest of the APIs and will receive a error 403. This nicely suits the bonus requirement of authorizing logged in users to perform certain actions. 
14. You can also view auto generated javadocs at `/src/docs/`



# Project Overview
The Online Bookstore backend is meticulously structured to ensure modularity, maintainability, and a clear separation of concerns. Let's break down the main components:

Packages:
Model (entity): This package hosts the core data structures and entities which represent the main objects we're working with - in this case, books. It describes the attributes a book might have, such as title, author, price, etc.

Service: Services play a crucial role in our application. They hold the business logic and act as a bridge between the Controllers and the Repositories. By ensuring that the logic stays in the service layer, the application remains modular and easier to maintain and test. If we ever needed to change how a certain feature works, we'd look into the Service layer.

Controller: Controllers act as gatekeepers. They intercept the API requests, process the data, and hand it over to the service layer. After the service layer processes the data, the controller takes charge again to send the response back to the client. Controllers in our project are meticulously designed to be lean, ensuring that they only handle the request and response, without being burdened with business logic.

Repository: This is where the data access logic resides. Using Spring Data JPA, we've abstracted most of the complex DB operations, ensuring that our application remains flexible. If there's a need to switch databases in the future or change how data fetch operations work, this is the package you'd dive into.

Adoption Reasoning: This layered approach, often referred to as the multi-tier architecture, allows for clear separation of responsibilities. Each component or layer has a distinct role to play, ensuring that changes in one part of the application have minimal ripple effects on others. Moreover, it makes testing easier as each component can be tested in isolation.

Project Navigation:
For adding, retrieving, updating, and deleting books, look into the Controller package.
For business rules and logic, dive into the Service layer.
For data access and storage, the Repository package is where you'd want to be.
For data structure and representation, look no further than the Model (entity) package.

# Design Patterns used
   The application employs several design patterns ensuring scalability, maintainability, and separation of concerns:

MVC (Model-View-Controller) for a clear separation between application logic, data, and UI (in this case, the API responses).
Repository Pattern: Ensuring a clear separation between the business logic and data access logic.
DTO (Data Transfer Objects) to abstract the internal workings and provide clients with the necessary data without exposing the entire data model.
# Authenticating and Authorizing Users
   JWT (JSON Web Tokens) is employed for both authentication and authorization. Upon successful login, users receive a JWT, which needs to be sent in subsequent requests' headers to access protected endpoints. This ensures security by allowing only authenticated and authorized users to access certain functionalities.

# Handling Errors, Exceptions and Logging
   Exception Handling: A centralized exception handling mechanism using @ControllerAdvice ensures that all exceptions are caught and processed uniformly, providing meaningful error messages to the client.
   Logging: Efficient logging mechanisms are in place using Logback, ensuring that all important events, errors, and information are logged for debugging and monitoring purposes. 
   
# Performance Optimizations
   Database Indexing: Postgres DB indexes have been set up on frequently queried columns to accelerate data retrieval times.
   Query Optimization: JPA and Hibernate are used judiciously, ensuring that only necessary data is fetched and unnecessary joins are avoided.
   Containerization: Using Docker ensures that the application environment is consistent across different stages of development, thereby avoiding performance discrepancies.

# Future Improvements
   Caching: Introducing caching mechanisms like Redis to further enhance data retrieval times for frequently accessed endpoints.
   Integration with Frontend: A frontend application for a more visual interaction with the online bookstore.
   Extended Logging and Monitoring: Implementing tools like ELK (Elasticsearch, Logstash, Kibana) stack for advanced logging and monitoring.

# Design Pattern used
Certainly. Let's address the structures in the context of the design patterns provided in the prompt.
In our solution, one could argue that the Singleton Pattern is implicitly employed through Spring's default behavior of beans. The components such as the BookService and BookController are treated as singletons by the Spring framework. This ensures that there's only one instance of these beans throughout the application, promoting efficient resource use.
The Factory Pattern can be seen in how Spring manages the instantiation of beans. While we haven't explicitly coded a factory, the Spring framework underneath leverages this pattern when creating instances of our services, repositories, and controllers. The @Autowired annotation and the Spring container handle the responsibility of creating the right object, abstracting away the instantiation details from the developer.
While we haven't directly used the Observer or Strategy patterns in the provided code, one could envision potential applications. For instance, an Observer pattern could be employed to notify other parts of the system when a new book is added, or the quantity changes, keeping various components in sync. The Strategy pattern could be introduced in the future if we decide to have multiple algorithms or strategies to calculate prices, promotions, or discounts for books.
Regarding the Behavior pattern, it's not a conventional design pattern like the others mentioned. However, in the broader sense, behavioral patterns like Command, Iterator, or Visitor could be integrated into our system. For instance, if we had a series of operations to perform on each book (like a set of validations or transformations), the Command pattern would be appropriate.
Finally, the Application, ErrorController, and Maven builds, while embodying best practices, are more about the architecture and tooling than specific design patterns mentioned in the prompt. They ensure modularity, maintainability, and a robust application lifecycle but don't directly map to the mentioned patterns.
TLDR;
In our solution, the Singleton Pattern is present via Spring's default bean behavior, ensuring single instances of BookService and BookController. The Factory Pattern is indirectly utilized by Spring when instantiating our services, repositories, and controllers, with the @Autowired annotation abstracting object creation.
Though we didn't directly implement the Observer or Strategy patterns, they have potential applications. An Observer pattern could notify system parts of book changes, and the Strategy pattern might offer varied book pricing algorithms in the future.
Lastly, while tools like the Application, ErrorController, and Maven builds represent architectural best practices, they aren't directly aligned with the specific design patterns listed, instead ensuring modularity and robust application management.

# Failed Requests (Errored Out, Caught and Handled Gracedully)
Failed Request #1. 
`curl -v -X POST http://localhost:8080/api/books \
-H 'Content-Type: application/json' \
-d '{
"title": "Example Book Title",
"author": "Example Author Name",
"isbn": "978-3-16-148410-0",
"price": 19.99,
"quantityInStock": 100
}'`
Custom Error Message: {"status":"BAD_REQUEST","message":"Database error. Possible constraint violation."}
Custom Error Logs:
section-2-java-assignment-onlinebookstore-1  | 2023-08-12T12:14:07.800Z ERROR 1 --- [nio-8080-exec-1] o.h.engine.jdbc.spi.SqlExceptionHelper   : ERROR: null value in column "title" of relation "books" violates not-null constraint
section-2-java-assignment-onlinebookstore-1  |   Detail: Failing row contains (4, null, null, null, null, null).
section-2-java-assignment-onlinebookstore-1  | 2023-08-12T12:14:07.807Z ERROR 1 --- [nio-8080-exec-1] c.o.controller.CustomErrorController     : Database constraint violation: could not execute statement [ERROR: null value in column "title" of relation "books" violates not-null constraint
section-2-java-assignment-onlinebookstore-1  |   Detail: Failing row contains (4, null, null, null, null, null).] [insert into books (author,isbn,price,quantity,title) values (?,?,?,?,?)]; SQL [insert into books (author,isbn,price,quantity,title) values (?,?,?,?,?)]; constraint [title" of relation "books]

Failed Request #2. 
![img.png](img.png)
Custom Error Message: {
"status": "UNSUPPORTED_MEDIA_TYPE",
"message": "Unsupported media type."
}
Custom Error Logs:
section-2-java-assignment-onlinebookstore-1  | 2023-08-12T12:20:44.035Z ERROR 1 --- [io-8080-exec-10] c.o.controller.CustomErrorController     : Unsupported media type error: Content-Type 'multipart/form-data;boundary=--------------------------134852517067505583880478;charset=UTF-8' is not supported


# High Level Approach
- Functional Requirements:
  - Microservice that does simple HTTP actions, GET, PUT, UPDATE, DELETE
  - Need to decide on ORM: How Java Classes map to the DB Schema
  - Need to decide on the DB Schema 
  - Need to standup a mock DB for app to talk to
  - Need to decide on Java framework to use here
  - Design Pattern to use here. (Singleton, Factory, Observer, Strategy etc) and pros and cons
  - Decide if need a simple webserver to talk to backend

- Additional Requirements:
  - Search and Filter: Search by title, author, ISBN? AND filter by price, avail etc. 
    - If search and filter, prolly need a webserver with a search bar and filter with checkboxes and scrollers
    - Typical search API with a /search?queryString=author pattern will be needed
  - Authenticate and Authorize: Registration and login. Only authenticated users can UPDATE inventory and make purchases?
    - Few ways can tackle this: 
    - Most man mode way: create a table for users, on user registration, take in user login and pw as text,
    - On calling the Registration API, a random seed is generated based on timestamp, concat the user pw with the seed (salt) and SHA256 it
    - Write the hashed pw and random seed and user login to DB (Plaintext pw is never stored in DB.)
    - On calling the Login API, retrieve the random seed, and hashed pw based on user login, take the user provided pw, concat with random seed, sha256 it
    - Compare this SHA256 string with the correct one stored in DB. If the two strings are equal, user is authenticated, return a user token. 
    - Once user token is generated, the user token needs to be supplied to the UPDATE books API in the header of the HTTP request. 
    - Other ways can be considered using AWS Cognito or using IDP (Identity provider), Approach uses SAML and Auth0. 
  - Error handling and logging: 
    - Rely on springboot exception handlers like @ControllerAdvice
    - Use logging library like Log4j to log exceptions and events
  - Improve Performance of DB:
    - Index important fields in postgres where queries hit the most
    - Consider using cache like redis or memcached 

- Deployment Considerations:
  - Docker Approach:
    - Docker Image that contains the Java codebase in a standalone monolithic Service
    - Docker Image that contains the Postgres container that will serve as the database
    - Possibly also need another Docker image if need a simple frontend
    - docker-compose for the networking needed between two docker containers on localhost
  - AWS Approach (To be attempted if there is enough time?) :
    - Each functionality is its own Serverless lambda, Add book, remove book, Update qty, Retrieve qty, List all books
    - Each lambda is its own endpoint on API gateway with the respective HTTP action, GET, PUT, UPDATE, DELETE
    - Postgres DB to be stood up on RDS