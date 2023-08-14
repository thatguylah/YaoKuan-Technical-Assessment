# Table of contents
1. [How to use?](#item-one)
2. [Project Overview](#item-two)
3. [Design Patterns used](#item-three)
4. [Testing Strategy](#item-four)
5. [Authenticating and Authorizing Users](#item-five)
6. [Handling Errors, Exceptions and Logging](#item-six)
7. [Performance Optimizations](#item-seven)
8. [Future Improvements](#item-eight)
9. Appendix:
   1. Initial Draft
   2. Caught Failed Requests and Custom Error Handling
---

<a id="item-one"></a>
# How to use? 
Prerequisites: You must have docker engine installed and docker daemon running. 
FYI: I am using macbook pro m2, which uses a ARM64 architecture, in the unlikely event you can not build the image correctly,
you might want to try building the docker image for x86 chipsets. 

### Setup environment and initialization.
1. Navigate to root of `/Section-2-Java-Assignment` on CLI. (You should see `docker-compose.yml`. )
2. Optionally, if you would like to seed the database on initialization, i have included it in `Section-2-Java-Assignment/src/main/resources/application.properties`. By default `seed.database=true` is enabled by default, only turn this off if you would not want to seed the DB with mock values. Note that this seed occurs each time the containers spin up. 
3. in the root dir of section 2, run `docker-compose build --no-cache `. Please make sure you have internet access and turn off any VPN or firewall.
4. then run `docker-compose up` 
5. you are good to go! Endpoints should be up and running on localhost:8080 

### Playing with the onlinebookstore :)
1. After the containers have initialized and spun up, navigate to http://localhost:8080/swagger-ui/#/ on your browser, you should see OpenAPI Swagger frontend for API documentation. 
2. More info on how to use the APIs: getAllBooks, getAllUsers, loginUser, registerUser are "public" APIs and dont need you to login to obtain a valid JWT. 
3. First you should register a new user, click on registerUser,click on Try it Out, type in username and password of your choosing, leave userRole as is. Then click on the blue Execute button, you should see a response 200 and your username and password hash in the response body.
4. Great! Now that you have registered as a user in our onlinebookstore, you need to log yourself in. Click on loginUser, enter your username and password as before and click on Execute button below. You should see response 200 and in the response body contains a string for your JWT auth token.
5. Copy the JWT auth token to your clipboard and scroll up to find a green Authorize button on the UI. 
6. You should see a textbox "Value", type `Bearer <INSERT_YOUR_JWT_TOKEN_HERE>` without the `<>` brackets. Click on Authorize, you are now logged in and can use the rest of the "private" APIs as Swagger automatically inserts the Bearer token in the header for you for API authorization. 
7. Without a valid Bearer token, you will not be able to use the rest of the APIs and will receive a error 403. This nicely suits the bonus requirement of authorizing logged in users to perform certain actions. 
8. You can also view auto generated javadocs at `/src/docs/`


---

<a id="item-two"></a>
# Project Overview
The Online Bookstore backend is meticulously structured to ensure modularity, maintainability, and a clear separation of concerns. Let's break down the main components:

Packages:
**Model (entity**): This package hosts the core data structures and entities which represent the main objects we're working with - in this case, books. It describes the attributes a book might have, such as title, author, price, etc.

**Service**: Services play a crucial role in our application. They hold the business logic and act as a bridge between the Controllers and the Repositories. By ensuring that the logic stays in the service layer, the application remains modular and easier to maintain and test. If we ever needed to change how a certain feature works, we'd look into the Service layer.

**Controller**: Controllers act as gatekeepers. They intercept the API requests, process the data, and hand it over to the service layer. After the service layer processes the data, the controller takes charge again to send the response back to the client. Controllers in our project are meticulously designed to be lean, ensuring that they only handle the request and response, without being burdened with business logic.

**Repository**: This is where the data access logic resides. Using Spring Data JPA, we've abstracted most of the complex DB operations, ensuring that our application remains flexible. If there's a need to switch databases in the future or change how data fetch operations work, this is the package you'd dive into.

**Adoption Reasoning**: This layered approach, often referred to as the multi-tier architecture, allows for clear separation of responsibilities. Each component or layer has a distinct role to play, ensuring that changes in one part of the application have minimal ripple effects on others. Moreover, it makes testing easier as each component can be tested in isolation.

**Project Navigation:**
For adding, retrieving, updating, and deleting books, look into the Controller package.
For business rules and logic, dive into the Service layer.
For data access and storage, the Repository package is where you'd want to be.
For data structure and representation, look no further than the Model (entity) package.

**Documentation**: `Controller` APIs are annotated with @Api in Java, so that according to OpenAPI standards, our API documentation is generated with our code. On top of that, docstrings are used in `Service` layers so that any IDE or javadocs will compile documentation automatically for internal developer documentation.
We use OpenAPI standard for user facing documentation(as seen in swagger UI) and docstrings for javadocs for developer facing documentation. Both types form the basis of a comprehensive CI test suite.

#### References and Readings:
1. https://www.javaguides.net/2019/01/standard-project-structure-for-spring-boot-projects.html
2. https://medium.com/@gayankurukulasooriya/the-way-of-building-spring-boot-crud-application-e99913a49492

---

<a id="item-three"></a>
# Design Patterns used
The application employs several design patterns ensuring scalability, maintainability, and separation of concerns:

#### High Level Abstractions
MVC (Model-View-Controller) for a clear separation between application logic, data, and UI (in this case, the API responses).
Repository Pattern: Ensuring a clear separation between the business logic and data access logic.
DTO (Data Transfer Objects) to abstract the internal workings and provide clients with the necessary data without exposing the entire data model.

#### Low Level: Facade Pattern in BookService
The Facade Pattern provides a unified interface to a set of interfaces in a subsystem. This pattern defines a higher-level interface that makes the subsystem easier to use. In the context of the BookService, we've implemented this pattern by wrapping the intricate interactions and checks with the BookRepository into a more straightforward and client-friendly API.

#### How the Facade Pattern was Implemented:
1. Simplified Interface: Methods such as addBook, getBook, getAllBooks, etc., offer straightforward and expressive names which abstract away the underlying complexity. The clients need not be aware of JpaRepository or database interactions.

2. Error Handling: Instead of expecting clients to check for the existence of a book before operating on it, the service provides built-in checks. For instance, the removeBook method internally checks if the book exists before attempting deletion, providing a safety layer.

3. Chaining Operations: In the updateBookQuantity method, we chain several operations: finding the book, updating its quantity, and then saving it back. This chained operation is abstracted away from the client, offering a seamless experience.

4. Consistent Exception Handling: The nested BookNotFoundException provides a consistent way to handle errors related to the absence of a book. Instead of scattering error-handling logic everywhere, the service throws this exception, providing a clear message about the issue.

By wrapping the BookRepository operations within the BookService and offering this high-level API to clients, we've effectively implemented the Facade design pattern, providing ease of use and abstracting away complexities.

#### References and Readings:
1. https://refactoring.guru/design-patterns/facade

---

<a id="item-four"></a>
# Testing Strategy for `BookService` and `BookController`
Our testing approach was rooted in the principles of Unit Testing, ensuring that each function of the BookService was tested in isolation. Leveraging the JUnit framework, we composed test cases that specifically targeted the expected behavior of individual methods. To effectively mock the interactions BookService has with underlying components (like the database), we employed Mockito. This allowed us to simulate various scenarios, ranging from successful database fetches to unexpected exceptions, thus ensuring our service layer was robust and resilient.

This granular testing strategy not only guarantees the correctness of our code but also forms the basis for a robust Continuous Integration (CI) pipeline in the future. With these tests in place, any changes or additions to the codebase can be automatically validated, ensuring that regressions are caught early and the code quality is consistently maintained.

Note: Extra services such as User Auth, JWT Token validation, Database Read and Writes are out-of-scope for unit testing and should belong to a comprehensive E2E CI testsuite. Unit tests are designed to test individual components and modules in isolation.

To run the test suite:
```mvn test```
---

<a id="item-five"></a>
# Authenticating and Authorizing Users
Authentication and authorization are critical pillars of our application's security. Let's break down how we've tackled each:

#### JWT-Based Authentication
1. User Registration & Login: Upon registering or logging in, the application verifies the user credentials. For valid credentials, a JSON Web Token (JWT) is generated.

2. Token Structure: The JWT contains a payload that consists of the user's identity, roles, and a few other claims. It's signed using a secret key, ensuring that the token hasn't been tampered with when it's sent back to the server in future requests.

3. Token Expiry: The tokens are time-bound. After a certain period, they expire, prompting the user to log in again. This approach limits potential damage in case a token is accidentally leaked or stolen.

#### Authorization with JWT Role-Based Access: 
The JWT also contains claims about user roles. This role-based system is integrated into the Spring security context. For instance, certain API endpoints might be accessible only to users with an ADMIN role. Although all endpoints dont differentiate between USER and ADMIN for now, the application can easily change to for eg. only allow ADMIN roles to add or modify Book inventory. This can be easily injected into the `BookService` layer or perhaps in `SecurityConfig`.

#### Protecting Routes: 
Middleware is set up such that it intercepts incoming requests, extracts the JWT from the header, validates it, and then either grants or denies access based on the token's validity and claims. This is set up in `SecurityConfig`.

As an aside: it should be mentioned that SpringBoot comes with CSRF token validation as default, and this application disables CSRF tokens in favor of JWT for better security and more up to date security protocols. (It took me alot of effort to get this setup 😢)

#### Revocation & Refresh Tokens (For Future Improvements)
Planning on implementing a mechanism where users can revoke tokens. Along with this, introducing refresh tokens can allow users to obtain a new access token without logging in again, making the user experience smoother while maintaining security.

---

<a id="item-six"></a>
# Handling Errors, Exceptions and Logging
Ensuring a robust error handling mechanism is in place is paramount. We've made the system resilient and informative both for developers and users:

#### Custom Error Handling with CustomErrorController
Our application has a specialized error handling controller, CustomErrorController, which catches specific exceptions and translates them into more informative HTTP responses.

1. **Unsupported Media Type**: If a client sends a request with an unsupported media type (like sending XML when the server expects JSON), an HttpMediaTypeNotSupportedException is raised. The handler logs the error and informs the client with an UNSUPPORTED_MEDIA_TYPE HTTP status and a user-friendly error message.

2. **Data Integrity & Constraint Violations**: Database operations might fail due to constraint violations, like trying to insert a duplicate book title. In such cases, DataIntegrityViolationException is triggered. Our handler checks if this exception is due to a constraint violation and, if so, logs the error and sends a BAD_REQUEST status back to the client with an explanatory message.

3. **Invalid JSON Format**: Occasionally, clients might send improperly formatted JSON in their requests. When such malformed JSON is encountered, an HttpMessageNotReadableException is triggered. Depending on the cause, our handler identifies if it's due to an "Unexpected character" and accordingly sends a message about the invalid JSON format. For other causes, a generic "Invalid request body format" message is returned.

4. **General Exceptions**: For all other unanticipated exceptions, a general handler ensures that they don't crash the application. Instead, an INTERNAL_SERVER_ERROR status is sent to the client with a generic message. Importantly, these exceptions are also logged for further investigation by developers.

#### Centralized Logging
The logging throughout these handlers is executed via SLF4J with Logback, ensuring that developers have detailed logs to understand the context and specifics of any issues. The `ApiError` entity encapsulates the error details, ensuring that clients receive a structured and consistent error response.

These specialized handlers ensure that both users and developers have a clear understanding of any issues, making the application more robust and maintainable.

---

<a id="item-seven"></a>
# Performance Optimizations
The Online Bookstore backend is not just designed to work but to work efficiently and provide a swift and seamless experience to its users. Here's how:

**Database Indexing**: Ensuring that database queries are efficient is crucial. To enhance the speed of data retrievals, indexes have been applied on columns that are queried frequently. This minimizes the time taken for search operations and facilitates quicker fetch times, especially when the database grows in size. (Note that for our small scale DB, indexes were not created for our demo as it would just add unnecessary overhead.)

**Query Optimization**: Careful attention has been given to the way data fetch operations are structured. By using JPA and Hibernate effectively, only the necessary data is fetched. N+1 query issues are avoided, and unnecessary joins are minimized. This ensures that the data retrieval is as fast as possible and the database isn't burdened with redundant operations.

**Containerization with Docker**: Docker provides a consistent environment, ensuring that the application runs the same regardless of where Docker runs. This removes the age-old "but it works on my machine" issue. Moreover, Docker can easily be scaled up to handle increased loads, ensuring the application remains responsive even under stress.

---

<a id="item-eight"></a>
# Future Improvements

**Caching with Redis**: Introducing caching mechanisms using Redis can significantly enhance data retrieval times. Frequently accessed endpoints can have their results cached, which will reduce the need to continuously query the database for the same data. This not only ensures faster response times but also reduces the load on the database.

**Deployment on AWS:** I could expand on this topic for half a day easily, but ill keep it summarized here. We can consider changing our API endpoints to be fronted by API gateway and Lambda for a serverless architecture. If requirements are to keep a RDBMS, we can use RDS for postgres deployment. If we prefer to keep our current architecture but favor reliability, availability and disaster recovery, we could opt to shift to ECR and ECS running on fargate clusters, where much like our current docker-compose set up, we could run on AWS serverless container runtimes.

**Enhanced Logging and Monitoring with the ELK Stack:** For detailed insights into application performance, user behavior, and potential bottlenecks or issues, we're considering the ELK (Elasticsearch, Logstash, Kibana) stack. Implementing these tools will provide real-time logging and visualization, making it easier to monitor and debug the application's operations and health.

---
# Contributors
This project would not be possible without myself, Google and ChatGPT, credit where credit is due :)

<details>
<summary>Just for fun, i fed ChatGPT this prompt.</summary>

Here is my README.md that encompasses much of what we covered on this project with the relevant table of contents. One of your last tasks is to:

1. Expand on some points i have elaborated if you think it requires elaborating, condense certain topics if you think it contains too much redundant information.
2. Correct my grammar and english for where i might have made mistakes.
3. Correct any markdown or formatting issues (example, setting up links to link table of content to the relevant sections).
4. On top of that, if you think we can structure certain content in a different way, for eg. you prefer to use a collapsible section instead of section headers then please do so.
5. Inject some humor into this document!!!

You should prioritize your tasks in the order given above. Our goal is to be as concise as we can, but yet not shortchange ourselves and elaborate where we can to gain extra points from the reader. Prioritise readibility and user fatigue (you should not feel tired reading this document)

Remember that this is for a Data Engineer assessment for Autodesk, so try to contextualize as much as you can. Here is the README.md in the following message.

[You can find the response to the following prompt here.](SillyREADME.md)

</details>

---
<details>
<summary>Appendix A: High Level Approach (Initial Draft)</summary>

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

</details>
<details>
<summary>Appendix B: Failed Requests (Errored Out, Caught and Handled Gracefully)</summary>


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
</details>
