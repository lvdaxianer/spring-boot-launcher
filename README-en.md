# Spring Boot Launcher

[English](README-en.md) | [中文](README.md)

## 1. Introduction

**Spring Boot Launcher** is a file upload solution suite based on Spring Boot. The project uses modular design, containing a breakpoint-resume core component and its test application, focused on solving the problem of efficient and stable large file uploads.

## 2. Project Modules

This project consists of the following two modules:

| Module | Description |
|--------|-------------|
| [breakpoint-transfer-launcher](./breakpoint-transfer-launcher/README.md) | Breakpoint resume file upload core component, supporting local disk and MinIO storage |
| [breakpoint-transfer-launcher-test](./breakpoint-transfer-launcher-test/README.md) | Test application built on the core component, demonstrating component integration |

### 2.1 breakpoint-transfer-launcher (Core Module)

**Breakpoint Transfer Spring Boot Starter** is a breakpoint resume file upload component based on Spring Boot. This component uses strategy pattern design, supports local disk storage and MinIO object storage, providing complete file chunk upload, verification, merge and other functions.

**Core Features:**
- Breakpoint resume support: Large file chunk upload, can continue after interruption
- High-performance file merge: Using zero-copy technology (FileChannel.transferTo) for efficient file merging
- Flexible storage strategy: Support local disk and MinIO object storage
- Complete exception system: Unified error code management for easy troubleshooting
- Rich logging support: Configurable log levels and detailed log output

**Detailed Documentation:** [breakpoint-transfer-launcher/README.md](./breakpoint-transfer-launcher/README.md)

### 2.2 breakpoint-transfer-launcher (Test Module)

**Breakpoint Transfer File Upload Test Application** is a complete Spring Boot application built on the core component, used to verify and demonstrate breakpoint resume functionality. This module provides a runnable application example to help developers quickly understand component integration and usage.

**Features:**
- Integrated breakpoint resume core component
- Provides complete RESTful API interfaces
- Supports file chunk upload, verification, merge and other operations
- Includes frontend example code

**Detailed Documentation:** [breakpoint-transfer-launcher-test/README.md](./breakpoint-transfer-launcher-test/README.md) (To be written)

## 3. Tech Stack

| Technology | Version | Description |
|------------|---------|-------------|
| Spring Boot | 3.4.5 | Application framework |
| Java | 21 | Development language |
| Apache Commons Lang3 | 3.14.0 | Utility library |
| Apache Commons IO | 2.15.1 | File operation library |
| Fastjson2 | 2.0.43 | JSON serialization |
| MinIO Client | 8.5.7 | MinIO client |
| Lombok | - | Code simplification |
| SLF4J | 2.0.9 | Logging framework |

## 4. Quick Start

### 4.1 Requirements

- JDK 21 or higher
- Maven 3.6+

### 4.2 Build Project

```bash
# Clone project
git clone https://github.com/lvdaxianer/spring-boot-launcher.git
cd spring-boot-launcher

# Build all modules
mvn clean install
```

### 4.3 Run Test Application

```bash
cd breakpoint-transfer-launcher-test
mvn spring-boot:run
```

After starting the test application, visit `http://localhost:8080` to use the file upload function.

## 5. License

This project is licensed under the MIT License.

## 6. Author

[lvdaxianer](https://github.com/lvdaxianer)

## 7. Contributing

Issues and pull requests are welcome.
