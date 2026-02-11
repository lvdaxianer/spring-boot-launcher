# 更新日志使用指南

本项目使用 **Keep a Changelog** 格式管理更新日志。

## 📁 文件说明

| 文件 | 说明 |
|------|------|
| `CHANGELOG.md` | 更新日志主文件 |
| `CHANGELOG_GUIDE.md` | 本指南 |
| `RELEASE_GUIDE.md` | GitHub Releases 发布指南 |
| `.gitmessage` | Git 提交信息模板 |
| `.github/workflows/release.yml` | GitHub Actions 工作流 |

## 🔄 自动 vs 手动更新

### 自动更新（推荐）

**Git Hook 自动更新**：
```bash
# 提交代码时自动更新 CHANGELOG.md
git commit -m "feat: 添加新功能"
# ✅ CHANGELOG.md 已自动更新
```

**GitHub Releases 自动生成**：
```bash
# 推送版本标签触发自动发布
git tag v0.0.3
git push origin v0.0.3
# ✅ 自动创建 GitHub Release
```

### 手动更新

**使用脚本**：
```bash
# 运行更新日志生成脚本
./generate-changelog.sh
```

**Maven 命令**：
```bash
# 生成 CHANGELOG.md
mvn org.gitlab4j:git-changelog-maven-plugin:changelog

# 带版本号生成
mvn org.gitlab4j:git-changelog-maven-plugin:changelog -Dchangelog.version=1.0.0
```

## 📝 提交规范

提交代码时请遵循 **Conventional Commits** 规范：

```
<type>: <subject>

<body>

<footer>
```

### Type 类型

| Type | 说明 | CHANGELOG 分类 | 示例 |
|------|------|---------------|------|
| `feat` | 新功能 | Added | `feat: 添加断点续传验证` |
| `fix` | 修复 bug | Fixed | `fix: 修复路径遍历问题` |
| `docs` | 文档更新 | Changed | `docs: 更新 README` |
| `style` | 代码格式 | Changed | `style: 格式化代码` |
| `refactor` | 重构 | Changed | `refactor: 重构日志输出` |
| `perf` | 性能优化 | Changed | `perf: 优化合并性能` |
| `test` | 测试相关 | Added | `test: 添加单元测试` |
| `chore` | 构建工具 | Changed | `chore: 更新插件版本` |
| `build` | 构建相关 | Changed | `build: 更新 Maven 配置` |
| `ci` | CI/CD | Changed | `ci: 更新 GitHub Actions` |

### 示例提交

```
feat: 添加自动生成更新日志功能

实现基于 Git 提交记录的自动更新日志生成，
支持 Keep a Changelog 格式。

Closes #123
```

### 版本号规范

版本号格式：`v主版本.次版本.修订版本`

```
v0.0.2 - 2026-02-11
```

## 📊 CHANGELOG 格式

```markdown
# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- 新功能描述

### Changed
- 变更描述

### Fixed
- 修复描述

## [0.0.2] - 2026-02-11

### Added
- 功能 A
- 功能 B

[Unreleased]: https://github.com/lvdaxianer/.../compare/v0.0.2...main
[0.0.2]: https://github.com/lvdaxianer/.../releases/tag/v0.0.2
[0.0.1]: https://github.com/lvdaxianer/.../releases/tag/v0.0.1
```

## 🚀 发布流程

### 完整发布流程

```bash
# 1. 开发阶段：按规范提交代码
git add .
git commit -m "feat: 添加新功能"

# 2. 版本准备：更新版本号
# 编辑 pom.xml，将 <version>0.0.1</version> 改为 <version>0.0.2</version>
vim pom.xml

# 3. 本地测试
mvn clean test

# 4. 生成/更新 CHANGELOG.md
./generate-changelog.sh
# 或等待 Git Hook 自动更新

# 5. 手动补充重要变更说明（如需要）
vim CHANGELOG.md

# 6. 提交版本更新
git add .
git commit -m "chore: 准备发布 v0.0.2"

# 7. 推送代码
git push origin main

# 8. 创建版本标签
git tag v0.0.2
git push origin v0.0.2

# 9. 验证 GitHub Release
# 访问 https://github.com/lvdaxianer/spring-boot-launcher/releases
```

### 跳过更新日志

```bash
# 提交时跳过更新日志记录
git commit -m "chore: 更新依赖 [skip changelog]"
```

## 🛠️ 工具使用

### generate-changelog.sh 脚本

```bash
# 查看菜单
./generate-changelog.sh

# 选项说明：
# 1. 生成 CHANGELOG.md
# 2. 查看最近提交
# 3. 查看版本列表
# 4. 手动添加更新条目
# 5. 退出
```

### Git Hook 自动更新

每次提交时会自动：
1. 解析提交消息类型
2. 更新 `CHANGELOG.md`
3. 显示更新结果

## 🔧 Git 配置

已配置：
- **提交模板**: `.gitmessage`
- **Git Hook**: `post-commit`
- **模板目录**: `~/.git-templates`

### 初始化新仓库

```bash
# 如果克隆到新目录，需要重新初始化模板
git config --global init.templateDir ~/.git-templates

# 重新克隆或初始化
git init
```

## 📈 版本发布检查清单

- [ ] 所有功能测试通过
- [ ] 代码符合规范
- [ ] 更新版本号（pom.xml）
- [ ] 提交符合规范
- [ ] CHANGELOG.md 已更新
- [ ] 创建版本标签
- [ ] 推送代码和标签
- [ ] 验证 GitHub Release
- [ ] 验证制品下载

## ❓ 常见问题

### Q: 提交后 CHANGELOG.md 没有更新？
A: 检查 `.git/hooks/post-commit` 是否有执行权限

```bash
chmod +x .git/hooks/post-commit
```

### Q: GitHub Release 没有自动创建？
A: 确保标签格式为 `v*.*.*`

```bash
# 正确
git tag v0.0.1

# 错误
git tag 0.0.1
git tag release-0.0.1
```

### Q: 如何查看历史版本？

```bash
# 查看标签
git tag -l

# 查看版本差异
git log v0.0.1...v0.0.2 --oneline

# 查看某个版本的内容
git show v0.0.1
```

### Q: 如何修改错误的提交消息？

```bash
# 修改最近一次提交
git commit --amend -m "feat: 正确的提交消息"
```

## 📚 参考资源

- [Keep a Changelog](https://keepachangelog.com/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)
- [GitHub Actions](https://docs.github.com/en/actions)
- [softprops/action-gh-release](https://github.com/softprops/action-gh-release)
