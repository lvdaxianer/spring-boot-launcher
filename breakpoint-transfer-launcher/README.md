# Breakpoint Transfer Spring Boot Starter

[English](README.md) | [中文](README_CN.md)

## 1. 项目简介

**Breakpoint Transfer Spring Boot Starter** 是一个基于 Spring Boot 的断点续传文件上传组件。该组件采用策略模式设计，支持本地磁盘存储和 MinIO 对象存储两种存储方式，提供完整的文件分片上传、验证、合并等功能。

主要特点包括：
- 断点续传支持：大文件分片上传，中断后可继续上传
- 高性能文件合并：使用零拷贝技术（FileChannel.transferTo）高效合并文件
- 灵活存储策略：支持本地磁盘和 MinIO 对象存储
- 完善的异常体系：统一的错误码管理，便于问题排查
- 丰富的日志支持：可配置的日志级别和详细日志输出

## 2. 技术栈

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

## 3. 项目结构

```
src/main/java/io/lvdaxianer/github/breakpoint/transfer/
├── api/                          # API 接口层
│   ├── UploadFileUtils.java      # 文件工具接口
│   └── impl/UploadFileUtilsImpl.java
├── configuration/                # 自动配置类
│   ├── BreakpointTransferAutoConfigure.java
│   └── BreakpointLogAutoConfiguration.java
├── entity/                       # 实体类
│   ├── UploadFileProperties.java     # 配置属性
│   └── UploadFileFullProperties.java # 完整属性
├── exception/                    # 异常处理
│   ├── BaseException.java
│   ├── ErrorCode.java            # 错误码枚举
│   ├── FileMergeException.java
│   ├── FileUploadException.java
│   ├── ParamCountException.java
│   ├── ParamErrorException.java
│   └── UploadFileNotFoundException.java
├── extend/                       # 扩展接口及实现
│   ├── FileOperate.java          # 文件操作接口
│   └── impl/
│       ├── DiskFileOperateImpl.java   # 磁盘存储实现
│       └── MinoFileOperateImpl.java   # MinIO 存储实现
├── interceptor/                  # HTTP 拦截器
│   ├── HttpRequestInterceptor.java
│   └── InterceptorConfig.java
├── lifecycle/                    # 生命周期
│   └── ContainerRefreshDone.java
├── strategy/                     # 策略模式实现
│   ├── SelectStrategy.java       # 策略接口
│   ├── SelectStrategyFactory.java
│   ├── AbstractSelectStrategyFactory.java
│   ├── DefaultSelectStrategyFactory.java
│   ├── RequestMatchEntity.java
│   └── impl/
│       ├── SectionStrategyImpl.java    # 分片上传策略
│       ├── VerifyStrategyImpl.java     # 文件验证策略
│       ├── ListStrategyImpl.java       # 文件列表策略
│       ├── MergeStrategyImpl.java      # 文件合并策略
│       └── adapter/
│           └── DefaultSelectStrategyAdapter.java
└── utils/                       # 工具类
    ├── CommonUtils.java          # 通用工具
    ├── Constants.java            # 常量定义
    ├── FileUtils.java            # 文件操作
    ├── RequestEnum.java          # 请求方法枚举
    └── result/ResponseEntity.java # 响应实体
```

## 4. 功能特性

### 4.1 文件分片上传

将大文件分割成多个小片段进行上传，支持断点续传功能。每个分片文件独立上传，即使某个分片上传失败，也只需重传该分片而无需重新上传整个文件。

**特点**：
- 支持任意大小的文件分片
- 分片文件自动按序号排序
- 使用 Spring 的 `transferTo` 方法优化文件写入性能

### 4.2 文件验证

在上传前验证文件是否已存在，避免重复上传相同文件。通过文件验证接口可以快速判断服务端是否已有该文件。

### 4.3 文件合并

将所有上传成功的分片文件合并成完整的原始文件。使用零拷贝技术（`FileChannel.transferTo`）实现高效的文件合并，内存占用低，性能优异。

### 4.4 文件列表查询

查询指定目录下已上传的分片文件数量和总大小，用于前端展示上传进度和状态。

## 5. API 接口

所有 API 接口的基础路径为：`/breakpoint/transfer/upload`

### 5.1 分片上传

```http
POST /breakpoint/transfer/upload/section/{baseDir}/{filename}
Content-Type: multipart/form-data

Request Body: file (文件分片)
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| baseDir | String | 是 | 文件存放目录名称 |
| filename | String | 是 | 文件名，格式：`原始文件名-序号` |

**响应示例**：

```json
{
  "success": true,
  "code": "200",
  "message": null,
  "data": true
}
```

### 5.2 文件验证

```http
GET /breakpoint/transfer/upload/verify/{filename}
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| filename | String | 是 | 要验证的文件名 |

**响应示例**：

```json
{
  "success": true,
  "code": "200",
  "message": null,
  "data": true
}
```

### 5.3 文件列表

```http
GET /breakpoint/transfer/upload/list/{baseDir}
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| baseDir | String | 是 | 目录名称 |

**响应示例**：

```json
{
  "success": true,
  "code": "200",
  "message": null,
  "data": [3, 1048576]
}
```

`data` 为数组，包含两个元素：
- 第一个元素：分片文件数量
- 第二个元素：分片文件总大小（字节）

### 5.4 文件合并

```http
GET /breakpoint/transfer/upload/merge/{baseDir}/{filename}
```

**参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| baseDir | String | 是 | 文件存放目录名称 |
| filename | String | 是 | 原始文件名 |

**响应示例**：

```json
{
  "success": true,
  "code": "200",
  "message": null,
  "data": true
}
```

## 6. 快速开始

### 6.1 Maven 依赖

```xml
<dependency>
    <groupId>io.github.lvdaxianer</groupId>
    <artifactId>breakpoint-transfer-spring-boot3-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

### 6.2 配置属性

在 `application.yml` 或 `application.properties` 中添加配置：

**YAML 配置方式**：

```yaml
io:
  lvdaxianer:
    upload:
      file:
        enabled-type: disk              # 存储类型：disk 或 mino
        save-dir: ./upload-files        # 文件保存目录
        http-interceptor-order: 10      # HTTP 拦截器顺序
        context-prefix:                 # 上下文路径前缀
        log-level: INFO                 # 日志级别：DEBUG, INFO, WARN, ERROR
        detailed-logging: false        # 是否启用详细日志
```

**Properties 配置方式**：

```properties
io.lvdaxianer.upload.file.enabled-type=disk
io.lvdaxianer.upload.file.save-dir=./upload-files
io.lvdaxianer.upload.file.http-interceptor-order=10
io.lvdaxianer.upload.file.context-prefix=
io.lvdaxianer.upload.file.log-level=INFO
io.lvdaxianer.upload.file.detailed-logging=false
```

### 6.3 配置说明

| 配置项 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `enabled-type` | String | - | 存储类型，必填项：`disk`（本地磁盘）或 `mino`（MinIO） |
| `save-dir` | String | `uploadFileTmpDir` | 文件保存目录（仅 `disk` 模式有效） |
| `http-interceptor-order` | int | 10 | HTTP 拦截器执行顺序 |
| `context-prefix` | String | "" | 上下文路径前缀，用于设置应用的上下文根路径 |
| `log-level` | String | INFO | 日志级别，支持 DEBUG、INFO、WARN、ERROR |
| `detailed-logging` | boolean | false | 是否启用详细日志，启用后输出更详细的操作信息 |

## 7. 使用示例

### 7.1 前端分片上传实现

以下是一个简化的前端分片上传实现示例：

```javascript
class BreakpointUpload {
    constructor(file, chunkSize = 5 * 1024 * 1024) {
        this.file = file;
        this.chunkSize = chunkSize;
        this.chunks = Math.ceil(file.size / chunkSize);
    }

    async upload(baseDir) {
        for (let i = 0; i < this.chunks; i++) {
            const formData = new FormData();
            const chunk = this.getChunk(i);
            const filename = `${this.file.name}-${i}`;
            
            formData.append('file', chunk);
            
            const response = await fetch(
                `/breakpoint/transfer/upload/section/${baseDir}/${filename}`,
                { method: 'POST', body: formData }
            );
            
            const result = await response.json();
            if (!result.success) {
                throw new Error(`分片 ${i} 上传失败`);
            }
            
            // 更新上传进度
            this.updateProgress((i + 1) / this.chunks * 100);
        }

        // 合并文件
        const mergeResponse = await fetch(
            `/breakpoint/transfer/upload/merge/${baseDir}/${this.file.name}`,
            { method: 'GET' }
        );
        
        return mergeResponse.json();
    }

    getChunk(index) {
        const start = index * this.chunkSize;
        const end = Math.min(start + this.chunkSize, this.file.size);
        return this.file.slice(start, end);
    }

    updateProgress(percent) {
        console.log(`上传进度: ${percent.toFixed(2)}%`);
    }
}

// 使用示例
const upload = new BreakpointUpload(selectedFile);
upload.upload('my-directory').then(result => {
    console.log('上传完成:', result);
});
```

### 7.2 后端集成

确保 Spring Boot 应用已正确配置，组件会自动扫描并注册相关 Bean。无需额外编写配置类，只需添加依赖并配置相应属性即可。

## 8. 文件存储结构

使用本地磁盘存储时，文件将按以下结构组织：

```
<save-dir>/
├── baseDir/
│   ├── tmp/                     # 临时分片文件目录
│   │   └── <baseDir>/
│   │       ├── filename-0
│   │       ├── filename-1
│   │       └── ...
│   ├── public/                  # 合并后的公共文件目录
│   │   └── filename
│   └── convert/                 # 转换文件目录
│       └── ...
```

## 9. 错误码说明

| 错误码 | 说明 |
|--------|------|
| PARAM_001 | 请求参数数量不正确 |
| PARAM_002 | 请求参数格式不正确 |
| PARAM_003 | 参数值无效 |
| FILE_001 | 上传文件为空 |
| FILE_004 | 文件上传失败 |
| FILE_005 | 文件合并失败 |
| FILE_006 | 文件不存在 |
| FILE_007 | 文件存储错误 |

## 10. 扩展开发

### 10.1 添加新的存储策略

1. 实现 `FileOperate` 接口
2. 使用 `@ConditionalOnProperty` 注解注册为 Spring Bean
3. 在配置类中指定 `enabled-type` 为自定义值

### 10.2 添加新的请求策略

1. 实现 `SelectStrategy` 接口
2. 在 `DefaultSelectStrategyFactory` 中注册新策略

## 11. 日志配置

组件支持以下日志级别：
- **DEBUG**：详细的调试信息，包括请求参数、处理步骤
- **INFO**：常规运行信息，包括关键操作记录
- **WARN**：警告信息，包括参数异常等情况
- **ERROR**：错误信息，包括异常堆栈

建议在生产环境使用 `INFO` 级别，在开发调试时使用 `DEBUG` 级别。

## 12. 性能优化

### 12.1 文件合并优化

组件使用 Java NIO 的 `FileChannel.transferTo` 方法进行文件合并，该方法利用操作系统底层的零拷贝特性，相比传统 IO 具有以下优势：
- 内存占用更低：无需将数据加载到用户空间
- 复制速度更快：直接在内核空间完成数据传输
- CPU 消耗更少：减少了用户态和内核态之间的数据拷贝

### 12.2 分片大小建议

建议分片大小设置在 1MB-10MB 之间：
- 分片过小：增加 HTTP 请求次数，网络开销增大
- 分片过大：内存占用增加，重传成本提高

## 13. 注意事项

1. **存储类型选择**：`enabled-type` 为必填配置，必须指定 `disk` 或 `mino`
2. **路径安全**：组件已内置路径遍历攻击防护，不支持绝对路径
3. **并发安全**：多线程上传同一文件时，分片文件会相互覆盖，需确保唯一标识
4. **目录清理**：文件合并成功后，临时分片文件会自动删除

## 14. License

This project is licensed under the MIT License.

## 15. Author

[lvdaxianer](https://github.com/lvdaxianer)

## 16. Contributing

Issues and pull requests are welcome.

## 17. Changelog

### v0.0.1
- Initial release
- Support breakpoint file upload
- Support local disk storage
- Support MinIO storage (placeholder)
- Complete exception handling system
- Detailed logging support
