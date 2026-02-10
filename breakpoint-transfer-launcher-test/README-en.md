# Breakpoint Transfer File Upload Test Application

[English](README-en.md) | [中文](README.md)

## 1. Introduction

**Breakpoint Transfer File Upload Test Application** is a Spring Boot-based test application used to verify and demonstrate the functionality of the breakpoint resume core component. This module provides a runnable application example with complete RESTful API interfaces and frontend integration examples.

## 2. Features

- Integrated breakpoint resume core component `breakpoint-transfer-spring-boot3-starter`
- Provides complete RESTful API interfaces supporting file chunk upload, verification, merge
- Includes frontend chunk upload implementation example
- Based on Spring Boot 3.4.5, supports Java 21

## 3. Tech Stack

| Technology | Version | Description |
|------------|---------|-------------|
| Spring Boot | 3.4.5 | Application framework |
| Java | 21 | Development language |
| breakpoint-transfer-spring-boot3-starter | 0.0.1 | Breakpoint resume core component |

## 4. Quick Start

### 4.1 Requirements

- JDK 21 or higher
- Maven 3.6+

### 4.2 Run Application

```bash
# Build in project root
mvn clean install

# Enter test module directory
cd breakpoint-transfer-launcher-test

# Start application
mvn spring-boot:run
```

After starting the application, visit `http://localhost:8080` to use the file upload function.

## 5. API Interfaces

This test application inherits all API interfaces from the breakpoint resume core component. Please refer to [breakpoint-transfer-launcher/README.md](../breakpoint-transfer-launcher/README.md) for API details.

## 6. License

This project is licensed under the MIT License.

## 7. Author

[lvdaxianer](https://github.com/lvdaxianer)
