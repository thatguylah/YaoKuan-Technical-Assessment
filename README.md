# Table of Contents

- [Table of Contents](#table-of-contents)
- [About me](#about-me)
- [Prerequisites:](#prerequisites)
- [How to use:](#how-to-use)
- [Project Structure:](#project-structure)

---

# About me

Hi there! Thanks for reviewing my takehome assessment! 

My name is Yao Kuan and im currently a Data Engineer in the DE team within the Government of Singapore Technology Agency. We work mainly on Whole-of-Government data platforms for internal users of all walks of life. 

In my day to day, i usually work on not just building data pipelines, but on building data platforms, which of course covers different domains from Security, Networking, DevOps, AI/ML and a little bit of Frontend engineering with a dominant Backend and Data engineering focus. 

For us, the name of the game is to move as much overhead as we can over to our cloud service provider, happy to chat more about how we achieved this! 

Last but not least, recently i have been contributing to an open source python SDK known as `python-telegram-bot`. Find my contribution here: https://github.com/thatguylah/python-telegram-bot

Enough about me though, lets get started! 

---
# Prerequisites:

Below is a list of key technologies used, but not to worry, to run this project, you really only need docker 🐳 Everything else has been taken care of for you!

- Docker (Must have)
- Code Editor: IntelliJ for IDE (Recommended for section 2)
- Java 17
- Apache Maven (build)
- For other Java related Dependencies check pom.xml (Key frameworks used is SpringBoot)
- For Postgres and other dependencies, this is managed in Docker.
- terraform


---
# How to use:

You can find the assignment questions in [assignment questions here](Assignment.md). For each Section, at the root of the section contains a dedicdated README.md for that section, you may head over there to find out more :)

[Section 1 - SQL Assignment](Section-1-SQL-Assignment/README.md)
[Section 2 - Java Assignment](Section-2-Java-Assignment/README.md)

I have also included a tree of the entire Project Structure below for your reference. 

--- 
# Project Structure:
```
.
├── Assignment.md
├── README.md
├── Section-1-SQL-Assignment
│   ├── README.md
│   ├── docker
│   │   ├── DDL.sql
│   │   └── Dockerfile
│   ├── queries
│   │   ├── Indexing.sql
│   │   ├── Partitioning.sql
│   │   ├── Queries.sql
│   │   └── Update.sql
│   └── terraform
│       └── main.tf
└── Section-2-Java-Assignment
    ├── HELP.md
    ├── README.md
    ├── SillyREADME.md
    ├── docker
    │   ├── app
    │   │   └── Dockerfile
    │   └── db
    │       ├── Dockerfile
    │       └── schema.sql
    ├── docker-compose.yml
    ├── img.png
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    └── src
        ├── main
        │   ├── java
        │   │   └── com
        │   │       └── onlinebookstore
        │   │           ├── OnlineBookstoreApplication.java
        │   │           ├── config
        │   │           │   ├── DBSeederRunner.java
        │   │           │   ├── SecurityConfig.java
        │   │           │   └── SwaggerConfig.java
        │   │           ├── controller
        │   │           │   ├── BookController.java
        │   │           │   ├── CustomErrorController.java
        │   │           │   └── UserController.java
        │   │           ├── model
        │   │           │   ├── dto
        │   │           │   │   ├── FilterBookDTO.java
        │   │           │   │   ├── SearchBookDTO.java
        │   │           │   │   ├── UserCredentialsDTO.java
        │   │           │   │   ├── UserLoginDTO.java
        │   │           │   │   └── UserRegistrationDTO.java
        │   │           │   └── entity
        │   │           │       ├── ApiError.java
        │   │           │       ├── Book.java
        │   │           │       ├── User.java
        │   │           │       └── UserRole.java
        │   │           ├── repository
        │   │           │   ├── BookRepository.java
        │   │           │   └── UserRepository.java
        │   │           ├── service
        │   │           │   ├── BookService.java
        │   │           │   ├── SearchService.java
        │   │           │   └── UserService.java
        │   │           └── util
        │   │               ├── DBSeederService.java
        │   │               ├── JwtRequestFilter.java
        │   │               └── JwtUtil.java
        │   └── resources
        │       ├── application-dev.properties
        │       └── application.properties
        └── test
            └── java
                └── com
                    └── onlinebookstore
                        ├── controller
                        │   └── BookControllerTest.java
                        ├── model
                        │   └── dto
                        │       ├── FilterBookDTO.java
                        │       └── SearchBookDTO.java
                        ├── repository
                        │   └── BookRepository.java
                        └── service
                            └── BookServiceTest.java
```