# Employee REST Service NoDB

## Introduction

The Employee REST Service NoDB is a Spring Boot application designed to manage employee data without using a database. This project leverages various modern technologies to provide a robust and efficient RESTful API service.

## Quick Summary

**Main Technologies:**
- Spring Boot 2.7.2
- Maven 3.8.5
- Java 17
- Lombok 1.18.22
- Cucumber 7.5.0
- Jackson 2.13.3
- Rest Assured 5.1.1
- WireMock 2.33.2
- Spring Cloud Contract WireMock 3.1.3
- Apache Commons Lang 3.12.0
- Validation API 2.0.1.Final
- Maven Compiler Plugin 3.10.1
- Maven Wrapper Plugin 3.1.1

## Technologies Used

### 1. Spring Boot
- **Version:** 2.7.2
- **Description:** Spring Boot simplifies the development of production-ready applications by providing a set of pre-configured tools and frameworks. It eliminates the need for extensive XML configuration, offering a fast way to develop Spring-based applications with embedded servers like Tomcat.

### 2. Maven
- **Version:** 3.8.5 (wrapper version)
- **Description:** Maven is a build automation tool used primarily for Java projects. It provides a uniform build system, project management, and comprehension features through its POM (Project Object Model) configuration.

### 3. Java
- **Version:** 17
- **Description:** The project is developed using Java 17, the latest long-term support (LTS) release, which brings numerous improvements in terms of performance, security, and stability.

### 4. Lombok
- **Version:** 1.18.22
- **Description:** Lombok is a Java library that helps reduce boilerplate code by generating commonly used code such as getters, setters, equals, hash, and toString methods at compile time.

### 5. Cucumber
- **Version:** 7.5.0
- **Description:** Cucumber is a testing tool that supports Behavior Driven Development (BDD). It allows the writing of tests in a human-readable format using Gherkin syntax.

### 6. Jackson
- **Version:** 2.13.3
- **Description:** Jackson is a high-performance JSON processor for Java. It is used for converting Java objects to JSON and vice versa.

### 7. Rest Assured
- **Version:** 5.1.1
- **Description:** Rest Assured is a Java library for testing and validating REST APIs. It simplifies the process of making HTTP requests and assertions on the responses.

### 8. WireMock
- **Version:** 2.33.2
- **Description:** WireMock is a flexible library for stubbing and mocking web services. It is used for simulating HTTP-based APIs during testing.

### 9. Spring Cloud Contract WireMock
- **Version:** 3.1.3
- **Description:** This module extends Spring Cloud Contract with WireMock, enabling the creation of stubbed web services based on contract definitions.

### 10. Apache Commons Lang
- **Version:** 3.12.0
- **Description:** Apache Commons Lang provides a host of helper utilities for the java.lang API, notably for String manipulation, number comparison, and object creation.

### 11. Validation API
- **Version:** 2.0.1.Final
- **Description:** The Validation API provides a standard way of performing bean validation in Java, supporting both method and field-level constraints.

### 12. Maven Compiler Plugin
- **Version:** 3.10.1
- **Description:** This plugin is used to compile the project’s source code using a specified version of the Java compiler.

### 13. Maven Wrapper Plugin
- **Version:** 3.1.1
- **Description:** The Maven Wrapper Plugin ensures that the project uses a consistent Maven version.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8.5 or higher

### Running the Application

1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/employee-rest-service-nodb.git
   cd employee-rest-service-nodb

2. Run the application using Maven:
   ```
   mvn clean install
   
3. Start the Spring Boot application:
   ```
   mvn spring-boot:run
   
4. Running Tests:
   ```
   mvn test
   