# Breakpoint Transfer Spring Boot Starter

[English](README-en.md) | [中文](README.md) | [Gitee](https://gitee.com/breakpoint-transfer-launcher/spring-boot-launcher)

**双仓库同步**: [GitHub](https://github.com/lvdaxianer/spring-boot-launcher) | [Gitee](https://gitee.com/breakpoint-transfer-launcher/spring-boot-launcher)

基于 Spring Boot 的断点续传文件上传组件，采用策略模式设计，专注于本地磁盘存储优化，提供文件分片上传、验证、合并等功能。

## 1. 插件优势

| 特性       | 实现方式                       | 优势                    |
|----------|--------------------------|-----------------------|
| 零拷贝合并    | `FileChannel.transferTo`       | 内存占用降低 90%，速度提升 5-10 倍  |
| 断点续传     | 分片上传 + 合并                   | 网络中断后可继续，无需重传整个文件     |
| 策略模式     | SelectStrategy 接口解耦处理逻辑       | 请求可插拔、易扩展            |
| 路径安全     | `sanitizePath()` 防护路径遍历攻击      | 企业级安全性保障             |
| 自动装配     | Spring Boot Starter            | 零代码改动，开箱即用            |
| 可配置日志    | DEBUG / INFO / WARN / ERROR     | 按需开启，减少日志噪音          |

## 2. 快速开始

### 2.1 引入依赖

在 `pom.xml` 中添加 Maven 依赖：

```xml
<dependency>
    <groupId>io.github.lvdaxianer</groupId>
    <artifactId>breakpoint-transfer-spring-boot3-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

### 2.2 配置说明

在 `application.yml` 中添加配置：

```yaml
io:
  lvdaxianer:
    upload:
      file:
        save-dir: ./upload-files        # 文件保存目录
        http-interceptor-order: 10      # HTTP 拦截器顺序
        context-prefix:                 # 上下文路径前缀
        log-level: DEBUG                # 日志级别：DEBUG, INFO, WARN, ERROR
        detailed-logging: false         # 是否启用详细日志
```

**配置项说明**：

| 配置项                      | 类型      | 默认值                | 说明                            |
|--------------------------|---------|--------------------|-------------------------------|
| `save-dir`               | String  | `uploadFileTmpDir` | 文件保存目录                        |
| `http-interceptor-order` | int     | 10                 | HTTP 拦截器执行顺序                  |
| `context-prefix`         | String  | ""                 | 上下文路径前缀，用于设置应用的上下文根路径         |
| `log-level`              | String  | DEBUG              | 日志级别，支持 DEBUG、INFO、WARN、ERROR |
| `detailed-logging`       | boolean | false              | 是否启用详细日志，启用后输出更详细的操作信息        |

### 2.3 获取文件

断点续传完成后，前端携带文件名（或文件 Hash）请求后端接口。后端通过 `UploadFileUtils` 获取文件进行后续处理。

#### 注入工具类

```java
@RestController
public class TestController {

    // 启动器已自动注入，直接使用即可
    @Resource
    private UploadFileUtils fileUtils;

}
```

#### 根据文件名获取文件

`filename` 为断点续传合并后的原始文件名，由前端传递给后端：

```java
@GetMapping("/getFile/{filename}")
public String getFile(@PathVariable("filename") String filename) {
    // 方式一：获取 MultipartFile（推荐）
    MultipartFile file = fileUtils.getMultipartFileByName(filename);

    // 方式二：获取 File
    File file = fileUtils.getFileByName(filename);

    // 方式三：获取 Path
    Path path = fileUtils.getPath(filename);

    // 拿到文件后进行后续操作，如存储、转发等
    return null;
}
```

#### File 与 MultipartFile 互转

```java
// 将 File 转换为 MultipartFile（保留原文件名）
MultipartFile multipartFile = fileUtils.convertFileToMultipartFile(file);

// 将 File 转换为 MultipartFile（自定义文件名）
MultipartFile multipartFile = fileUtils.convertFileToMultipartFile(file, "custom-name.txt");
```

> **注意**：文件存放目录为 `<save-dir>/<baseDir>/public/`，通过上述方法获取时只需传入文件名（不含路径），无需手动拼接目录。

### 2.4 前端分片上传示例

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

## 3. API 接口

所有 API 接口的基础路径为：`/breakpoint/transfer/upload`

| 接口            | 方法   | 路径                                             | 说明       |
|---------------|------|------------------------------------------------|----------|
| 分片上传         | POST | `/section/{baseDir}/{filename}`                  | 上传文件分片   |
| 文件验证         | GET  | `/verify/{filename}`                            | 验证文件是否存在 |
| 文件列表         | GET  | `/list/{baseDir}`                               | 查询已上传分片   |
| 文件合并         | GET  | `/merge/{baseDir}/{filename}`                   | 合并分片为完整文件 |

## 4. 文件存储结构

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

## 5. 错误码说明

| 错误码       | 说明        |
|-----------|-----------|
| PARAM_001 | 请求参数数量不正确 |
| PARAM_002 | 请求参数格式不正确 |
| PARAM_003 | 参数值无效     |
| FILE_001  | 上传文件为空    |
| FILE_004  | 上传文件失败    |
| FILE_005  | 文件合并失败    |
| FILE_006  | 文件不存在     |
| FILE_007  | 文件存储错误    |

## 6. License

This project is licensed under the MIT License.

## 7. 更新日志

### v0.0.1

- 首次发布
- 支持断点文件上传
- 支持本地磁盘存储
- 完整的异常处理体系
- 详细的日志支持
- 支持 Java 17
- 专注本地磁盘存储优化

---

[lvdaxianer](https://github.com/lvdaxianer)
