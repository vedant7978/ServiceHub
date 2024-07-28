Table of Contents
=================

1. [ServiceHub](#servicehub)
2. [Features](#features)
3. [Pre-requisites](#pre-requisites)
4. [External Dependencies](#external-dependencies)
5. [Running Application](#running-application)
6. [Contributors](#contributors)
7. [Test Coverage](#test-coverage)

# ServiceHub

ServiceHub is a Java Spring Boot + ReactJS web application that empowers a community-driven online servicehub. 
We connect service providers to service requesters directly to each other by removing the middleman.

# Features

1. LandingPage
   * Entry point of the application where user decides to either login or to register.

2. Authentication
   * Login - Log in to the application with valid credentials.
   * Register - Register a user into application
   * Forgot password - Inputs an email for user that has forgot the password
   * Reset password - Reset password for specific user
   * Sign out - Sign out of the application

3. Dashboard
   * Provides list of all services offered by other users with all details about the service and feedbacks given to the service provider.
   User gets a option from here to request a service.

4. Manage Services
   * User can view, create, update, or delete a service(s) added by him/her.

5. Manage Contracts
   * Provides all the contracts that are pending to be accepted/rejected and shows history of all contracts a user had previously.
   
6. E-Signed contract document
   * Provides an e-signed document by the service provider where it showcases all details about the contract between two users.

7. Feedbacks
   * Users gets an option to provide feedback to the service provider/requester once a contracted is completed. They
   can view their own feedbacks in profile and others feedback when requesting a service or accepting/rejecting a contract.

8. Wishlist
   * Provides list of all wishlisted services. User can also request the service from this list.

9. Profile
   * Provides all details about the logged in user and gives option to update user data. It also showcases all feedbacks given to logged in user.

# Pre-requisites

For build and running the application locally the project requires:
- Java [21.0.3](https://www.oracle.com/java/technologies/javase/21-0-2-relnotes.html)
- Apache Maven [3.9.7](https://dlcdn.apache.org/maven/maven-3/3.9.7/binaries/apache-maven-3.9.7-bin.zip)
- MySQL [8.0.36](https://dev.mysql.com/downloads/installer/)
- Node.js [21.7.1](https://nodejs.org/dist/v21.7.1/node-v21.7.1-x64.msi)
- npm [10.3.0](https://www.npmjs.com/package/npm/v/10.3.0)

# External Dependencies

| Dependency Name                     | Version     | Dependency Description                                                                                                     |
|-------------------------------------|-------------|----------------------------------------------------------------------------------------------------------------------------|
| spring-boot-starter-web             | 3.3.2       | Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container. |
| spring-boot-starter-jdbc            | 3.3.2       | Starter for using JDBC with the HikariCP connection pool                                                                   |
| mysql-connector-j                   | 8.0.33      | MySQL Connector/J is a JDBC Type 4 driver that uses pure java to access MySQL databases                                    |
| spring-boot-starter-data-jpa        | 3.3.2       | Starter for using Spring Data JPA with Hibernate                                                                           |
| spring-boot-starter-test            | 3.3.2       | Starter for testing Spring Boot applications with libraries including JUnit, Hamcrest and Mockito                          |
| jjwt-api                            | 0.11.5      | JWT (JSON Web Token) implementation for Java                                                                               |
| jjwt-impl                           | 0.11.5      | JWT (JSON Web Token) implementation for Java                                                                               |
| jjwt-jackson                        | 0.11.5      | JWT (JSON Web Token) implementation for Java                                                                               |
| lombok                              | 1.18.34     | Lombok is a java library that annotations to simplify Java development by automating the generation of boilerplate code.   |
| Hibernate validator                 | 8.0.1.Final | Hibernate Validator Engine Relocation Artifact                                                                             |
| Model mapper                        | 3.1.1       | Model mapper to map dto to entity and vice versa                                                                           |
| Hibernate core                      | 6.4.1.Final | Hibernate's core ORM functionality                                                                                         |
| spring-boot-starter-security        | 3.3.2       | Starter for using Spring Security                                                                                          |
| spring-boot-starter-log4j2          | 3.3.2       | Starter for using Log4j2 for logging. An alternative to spring-boot-starter-logging                                        |
| springdoc-openapi-starter-webmvc-ui | 2.5.0       | SpringDoc OpenAPI Starter WebMVC UI                                                                                        |
| spring-boot-starter-mail            | 3.3.2       | Starter for using Spring Framework's MailSender, which is used for sending email                                           |
| axios                               | 1.7.2       | Axios is a promise based HTTP client for the browser and node.js                                                           |
| bootstrap                           | ^5.3.3      | Bootstrap is a free and open-source CSS framework directed at responsive, mobile-first front-end web development           |
| jspdf                               | ^2.5.1      | Creates PDF runtime                                                                                                        |
| jspdf-autotable                     | ^3.8.2      | Helps in formatting a table in the pdf                                                                                     |
| lodash                              | ^4.17.21    | Helps in debouncing the function calls                                                                                     |
| react                               | ^18.3.1     | A JavaScript library for building user interfaces                                                                          |
| react-bootstrap                     | ^2.10.2     | A complete re-implementation of the Bootstrap components using React which has no dependency on either Bootstrap or jQuery |
| react-dom                           | ^18.3.1     | A package that serves as the entry point of the DOM-related rendering paths                                                |
| react-icons                         | ^5.2.1      | A set of icons for React applications                                                                                      |
| react-rating                        | ^2.0.5      | A library for showing and providing rating in form of custom icons (star in this project)                                  |
| react-router-dom                    | ^6.23.1     | A library for routing in React applications                                                                                |
| react-scripts                       | ^5.0.1      | A set of scripts for building React applications                                                                           |
| web-vitals                          | ^2.1.4      | A library for measuring web vitals                                                                                         |

# Running Application
## Remotely
Prerequisite: Connect to dal wifi or use dal vpn. <br>
**URL**: http://csci5308-vm8.research.cs.dal.ca:3000/

## Locally
- Clone Repository to your local machine
```bash
    git clone https://git.cs.dal.ca/courses/2024-summer/csci-5308/group08.git
```
- Create a Database called “servicehub” using MySQL workbench
```sql
    create database servicehub;
    use servicehub;
```
- Edit the following variables in backend/src/main/resources/application.yml
```yaml
    spring:
    mail:
      host: smtp.gmail.com
      port: 587
      username: noreplyservicehub@gmail.com
      password: nrco fkda imwm hobq
      properties:
        mail:
          smtp:
            auth: true
            starttls:
              enable: true
    datasource:
      url: jdbc:mysql://localhost:3306/servicehub?useSSL=false&allowPublicKeyRetrieval=true
      username: <your_username>
      password: <your_password>
      driver-class-name: com.mysql.cj.jdbc.Driver
    application:
      name: ServiceHub
    jpa:
      show-sql: true
      hibernate:
        ddl-auto: update
      properties:
        hibernate:
          format_sql: true
          dialect: org.hibernate.dialect.MySQLDialect
    servlet:
      multipart:
        max-file-size: 10MB
        max-request-size: 10MB

    email:
      frontend-port: ${FRONTEND_PORT:3000}

    upload:
      path: backend/uploads

    springdoc:
      swagger-ui:
        operations-sorter: method
        enabled: true
```
- Edit the following variable in frontend/src/utils/Constants.js
```javascript
// export const BASE_URL = "http://csci5308-vm8.research.cs.dal.ca:8080";
// Comment above URL and uncomment this when running backend locally
export const BASE_URL = "http://localhost:8080";
```

- Open terminal, change the present working directory to the project’s backend
```bash
    cd backend/
```
- Check if correct version of Maven exists
```bash
    mvn -v
```
- Installing Maven
```bash
    mvn clean install
```
- Build the project using Maven
```bash
    mvn clean package
```
- Run the application using Maven
```bash
    mvn spring:boot run
```
Alternately, you can run directly from IDE from ServiceHubApplication.java class.

- Using your terminal change the present working directory to the project’s frontend
```bash
    cd ../frontend/
```
- Check if correct version of node is installed
```bash
    node -v
```
- Check if correct version of npm is installed
```bash
    npm install
```
- Starting the react application
```bash
    npm start
```

# Contributors
- [Vraj Shah](https://git.cs.dal.ca/vsshah) (B00979965)
- [Jems Patel](https://git.cs.dal.ca/jems) (B00984406)
- [Vedant Patel](https://git.cs.dal.ca/vedant) (B00984592)
- [Priyanka Ravichandran](https://git.cs.dal.ca/priyankar) (B00962216)

# Test Coverage
- [IntelliJ Code Coverage Screenshot](/assets/IntellijTestCoverageScreenshot.png)
