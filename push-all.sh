#!/bin/bash

# Breakpoint Transfer Spring Boot Starter
# 同时推送到 GitHub 和 Gitee

BRANCH=${1:-main}
MESSAGE=${2:-$(date +"%Y-%m-%d %H:%M:%S")}

echo "🚀 推送到 GitHub 和 Gitee..."
echo "📌 分支: $BRANCH"

# 推送到 GitHub
echo ""
echo "▶️ 推送到 GitHub..."
git push origin $BRANCH

# 推送到 Gitee
echo ""
echo "▶️ 推送到 Gitee..."
git push gitee $BRANCH

echo ""
echo "✅ 推送完成！"
echo ""
echo "📍 GitHub: https://github.com/lvdaxianer/spring-boot-launcher"
echo "📍 Gitee: https://gitee.com/breakpoint-transfer-launcher/spring-boot-launcher"
