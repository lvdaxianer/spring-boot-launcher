# Breakpoint Transfer Spring Boot Starter

[English](README-en.md) | [中文](README.md) | [Gitee](https://gitee.com/breakpoint-transfer-launcher/spring-boot-launcher)

**Dual Repository Sync**: [GitHub](https://github.com/lvdaxianer/spring-boot-launcher) | [Gitee](https://gitee.com/breakpoint-transfer-launcher/spring-boot-launcher)

A Spring Boot starter for breakpoint resume file uploads, designed with a strategy pattern, optimized for local disk storage, providing file chunked upload, verification, and merging.

## 1. Features

| Feature         | Implementation                       | Benefit                    |
|----------------|-------------------------------------|----------------------------|
| Zero-copy merge | `FileChannel.transferTo`             | 90% less memory, 5-10x faster |
| Breakpoint resume | Chunked upload + merge            | Resume after interruption, no full file re-upload |
| Strategy pattern | SelectStrategy interface decouples logic | Pluggable, extensible requests |
| Path security   | `sanitizePath()` prevents traversal attacks | Enterprise-grade security |
| Auto-config     | Spring Boot Starter                  | Zero config, ready out of the box |
| Configurable logging | DEBUG / INFO / WARN / ERROR      | Enable as needed, reduce noise |

## 2. Quick Start

### 2.1 Add Dependency

Add the Maven dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.lvdaxianer</groupId>
    <artifactId>breakpoint-transfer-spring-boot3-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

### 2.2 Configuration

Add configuration to `application.yml`:

```yaml
io:
  lvdaxianer:
    upload:
      file:
        save-dir: ./upload-files        # File save directory
        http-interceptor-order: 10     # HTTP interceptor order
        context-prefix:                # Context path prefix
        log-level: DEBUG               # Log level: DEBUG, INFO, WARN, ERROR
        detailed-logging: false        # Enable detailed logging
```

**Configuration Reference**:

| Property                   | Type     | Default              | Description                               |
|---------------------------|----------|---------------------|-------------------------------------------|
| `save-dir`                | String   | `uploadFileTmpDir`  | File save directory                       |
| `http-interceptor-order`  | int      | 10                  | HTTP interceptor execution order          |
| `context-prefix`          | String   | ""                  | Context path prefix for app root path      |
| `log-level`              | String   | DEBUG               | Log level: DEBUG, INFO, WARN, ERROR       |
| `detailed-logging`        | boolean  | false               | Enable detailed logging for more info      |

### 2.3 Retrieving Files

After the breakpoint upload completes, the frontend sends the filename (or file hash) to the backend. Use `UploadFileUtils` to retrieve the file for further processing.

#### Inject the Utility

```java
@RestController
public class TestController {

    // Auto-injected by the starter, use directly
    @Resource
    private UploadFileUtils fileUtils;

}
```

#### Retrieve File by Filename

`filename` is the original filename after merge, passed from the frontend:

```java
@GetMapping("/getFile/{filename}")
public String getFile(@PathVariable("filename") String filename) {
    // Option 1: Get MultipartFile (recommended)
    MultipartFile file = fileUtils.getMultipartFileByName(filename);

    // Option 2: Get File
    File file = fileUtils.getFileByName(filename);

    // Option 3: Get Path
    Path path = fileUtils.getPath(filename);

    // Process the file as needed: storage, forwarding, etc.
    return null;
}
```

#### Convert Between File and MultipartFile

```java
// Convert File to MultipartFile (keep original filename)
MultipartFile multipartFile = fileUtils.convertFileToMultipartFile(file);

// Convert File to MultipartFile (custom filename)
MultipartFile multipartFile = fileUtils.convertFileToMultipartFile(file, "custom-name.txt");
```

> **Note**: Files are stored in `<save-dir>/<baseDir>/public/`. When using the methods above, pass only the filename (without path) - no manual directory concatenation needed.

### 2.4 Frontend Chunked Upload Example

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
                throw new Error(`Chunk ${i} upload failed`);
            }

            // Update upload progress
            this.updateProgress((i + 1) / this.chunks * 100);
        }

        // Merge file
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
        console.log(`Upload progress: ${percent.toFixed(2)}%`);
    }
}

// Usage
const upload = new BreakpointUpload(selectedFile);
upload.upload('my-directory').then(result => {
    console.log('Upload complete:', result);
});
```

## 3. API Reference

Base path for all APIs: `/breakpoint/transfer/upload`

| Endpoint      | Method | Path                                      | Description                |
|---------------|--------|-------------------------------------------|---------------------------|
| Chunk upload  | POST   | `/section/{baseDir}/{filename}`            | Upload file chunk         |
| File verify   | GET    | `/verify/{filename}`                     | Verify file exists        |
| File list     | GET    | `/list/{baseDir}`                         | Query uploaded chunks     |
| File merge    | GET    | `/merge/{baseDir}/{filename}`             | Merge chunks into file    |

## 4. File Storage Structure

```
<save-dir>/
├── baseDir/
│   ├── tmp/                     # Temporary chunk directory
│   │   └── <baseDir>/
│   │       ├── filename-0
│   │       ├── filename-1
│   │       └── ...
│   ├── public/                 # Merged file directory
│   │   └── filename
│   └── convert/                # Converted file directory
│       └── ...
```

## 5. Error Codes

| Code        | Description                  |
|-------------|------------------------------|
| PARAM_001   | Incorrect parameter count    |
| PARAM_002   | Incorrect parameter format   |
| PARAM_003   | Invalid parameter value      |
| FILE_001    | Uploaded file is empty       |
| FILE_004    | File upload failed           |
| FILE_005    | File merge failed            |
| FILE_006    | File not found               |
| FILE_007    | File storage error           |

## 6. License

This project is licensed under the MIT License.

## 7. Changelog

### v0.0.1

- Initial release
- Support breakpoint file upload
- Support local disk storage
- Complete exception handling system
- Detailed logging support
- Support Java 17
- Optimized for local disk storage

---

[lvdaxianer](https://github.com/lvdaxianer)
