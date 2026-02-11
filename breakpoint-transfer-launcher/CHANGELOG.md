# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.0.2] - 2026-02-11

### Added
- 添加 Maven Central 发布配置
- 添加源码和 JavaDoc 插件支持
- 添加 GitHub Actions 自动化发布工作流
- 添加自动生成更新日志支持

### Changed
- Java 版本从 21 降级到 17，提升兼容性
- 默认日志级别改为 DEBUG，便于调试
- 优化日志输出，根据日志级别动态控制调试信息

### Removed
- 移除 MinIO 依赖及相关实现代码
- 移除 `enabled-type` 配置项，简化配置流程

### Fixed
- 修复部分代码注释冗余问题
- 统一代码注释风格

## [0.0.1] - 2025-01-01

### Added
- 首次发布断点续传 Spring Boot Starter
- 支持大文件分片上传功能
- 支持本地磁盘存储
- 支持断点续传验证接口
- 支持文件合并功能
- 支持文件列表查询
- 完整的异常处理体系
- 详细的日志支持
- 路径遍历攻击防护

### Changed
- 优化文件合并性能，使用零拷贝技术
- 优化分片文件上传性能

### Fixed
- 修复多个安全问题
- 修复路径验证逻辑

[Unreleased]: https://github.com/lvdaxianer/spring-boot-launcher/compare/breakpoint-transfer-launcher/0.0.2...main
[0.0.2]: https://github.com/lvdaxianer/spring-boot-launcher/releases/tag/breakpoint-transfer-launcher/0.0.2
[0.0.1]: https://github.com/lvdaxianer/spring-boot-launcher/releases/tag/breakpoint-transfer-launcher/0.0.1

---

**双仓库同步**:
- [GitHub](https://github.com/lvdaxianer/spring-boot-launcher)
- [Gitee](https://gitee.com/breakpoint-transfer-launcher/spring-boot-launcher)
