# Spring Boot Launcher

[English](README-en.md) | [中文](README.md)

## 1. 项目简介

**Spring Boot Launcher** 是一个基于 Spring Boot 的文件上传解决方案集合。该项目采用模块化设计，包含断点续传核心组件及其测试应用，专注于解决大文件高效、稳定上传的问题。

## 2. 项目模块

本项目由以下两个模块组成：

| 模块 | 说明 |
|------|------|
| [breakpoint-transfer-launcher](./breakpoint-transfer-launcher/README.md) | 断点续传文件上传核心组件，支持本地磁盘和 MinIO 存储 |
| [breakpoint-transfer-launcher-test](./breakpoint-transfer-launcher-test/README.md) | 基于核心组件的测试应用，演示组件的集成使用方式 |

### 2.1 breakpoint-transfer-launcher（核心模块）

**断点续传文件上传 Spring Boot Starter** 是一个基于 Spring Boot 的断点续传文件上传组件。该组件采用策略模式设计，支持本地磁盘存储和 MinIO 对象存储两种存储方式，提供完整的文件分片上传、验证、合并等功能。

**核心特性：**
- 断点续传支持：大文件分片上传，中断后可继续上传
- 高性能文件合并：使用零拷贝技术（FileChannel.transferTo）高效合并文件
- 灵活存储策略：支持本地磁盘和 MinIO 对象存储
- 完善的异常体系：统一的错误码管理，便于问题排查
- 丰富的日志支持：可配置的日志级别和详细日志输出

**详细文档：** [breakpoint-transfer-launcher/README.md](./breakpoint-transfer-launcher/README.md)

### 2.2 breakpoint-transfer-launcher-test（测试模块）

**断点续传文件上传测试应用** 是一个基于核心组件构建的完整 Spring Boot 应用，用于验证和演示断点续传功能。该模块提供了一个可直接运行的应用示例，帮助开发者快速理解组件的集成方式和使用方法。

**功能特点：**
- 集成断点续传核心组件
- 提供完整的 RESTful API 接口
- 支持文件分片上传、验证、合并等操作
- 包含前端示例代码

**详细文档：** [breakpoint-transfer-launcher-test/README.md](./breakpoint-transfer-launcher-test/README.md)（待编写）

## 3. 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.4.5 | 应用框架 |
| Java | 21 | 开发语言 |
| Apache Commons Lang3 | 3.14.0 | 工具库 |
| Apache Commons IO | 2.15.1 | 文件操作库 |
| Fastjson2 | 2.0.43 | JSON 序列化 |
| MinIO Client | 8.5.7 | MinIO 客户端 |
| Lombok | - | 代码简化 |
| SLF4J | 2.0.9 | 日志框架 |

## 4. 快速开始

### 4.1 环境要求

- JDK 21 或更高版本
- Maven 3.6+

### 4.2 构建项目

```bash
# 克隆项目
git clone https://github.com/lvdaxianer/spring-boot-launcher.git
cd spring-boot-launcher

# 构建所有模块
mvn clean install
```

### 4.3 运行测试应用

```bash
cd breakpoint-transfer-launcher-test
mvn spring-boot:run
```

测试应用启动后，访问 `http://localhost:8080` 即可使用文件上传功能。

## 5. License

本项目基于 MIT License 开源。

## 6. 作者

[lvdaxianer](https://github.com/lvdaxianer)

## 7. 贡献

欢迎提交 Issue 和 Pull Request。
