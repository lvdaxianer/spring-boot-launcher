# Breakpoint Transfer File Upload Test Application

[English](README-en.md) | [中文](README.md)

## 1. 项目简介

**Breakpoint Transfer File Upload Test Application** 是一个基于 Spring Boot 的测试应用，用于验证和演示断点续传核心组件的功能。该模块提供了一个可直接运行的应用示例，包含了完整的 RESTful API 接口和前端集成示例。

## 2. 功能特点

- 集成断点续传核心组件 `breakpoint-transfer-spring-boot3-starter`
- 提供完整的 RESTful API 接口，支持文件分片上传、验证、合并
- 包含前端分片上传实现示例
- 基于 Spring Boot 3.4.5，支持 Java 21

## 3. 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.4.5 | 应用框架 |
| Java | 21 | 开发语言 |
| breakpoint-transfer-spring-boot3-starter | 0.0.1 | 断点续传核心组件 |

## 4. 快速开始

### 4.1 环境要求

- JDK 21 或更高版本
- Maven 3.6+

### 4.2 运行应用

```bash
# 在项目根目录构建
mvn clean install

# 进入测试模块目录
cd breakpoint-transfer-launcher-test

# 启动应用
mvn spring-boot:run
```

应用启动后，访问 `http://localhost:8080` 即可使用文件上传功能。

## 5. API 接口

本测试应用继承了断点续传核心组件的所有 API 接口，具体请参考 [breakpoint-transfer-launcher/README.md](../breakpoint-transfer-launcher/README.md) 中的 API 说明。

## 6. License

本项目基于 MIT License 开源。

## 7. 作者

[lvdaxianer](https://github.com/lvdaxianer)
