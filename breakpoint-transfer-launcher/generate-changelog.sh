#!/bin/bash

# Breakpoint Transfer Spring Boot Starter
# 更新日志生成脚本

echo "========================================"
echo "  更新日志生成工具"
echo "========================================"
echo ""

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 检查 Maven 是否安装
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}错误: 未找到 Maven，请先安装 Maven${NC}"
    exit 1
fi

# 显示菜单
echo "请选择操作:"
echo "1. 生成 CHANGELOG.md"
echo "2. 查看最近提交"
echo "3. 查看版本列表"
echo "4. 手动添加更新条目"
echo "5. 退出"
echo ""

read -p "请输入选项 (1-5): " option

case $option in
    1)
        echo -e "${GREEN}生成更新日志...${NC}"
        mvn org.gitlab4j:git-changelog-maven-plugin:changelog
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}更新日志已生成: CHANGELOG.md${NC}"
        else
            echo -e "${RED}生成更新日志失败${NC}"
        fi
        ;;

    2)
        echo -e "${BLUE}最近10条提交记录:${NC}"
        echo ""
        git log --oneline -10 --pretty=format:"%h - %s (%an)"
        ;;

    3)
        echo -e "${BLUE}版本标签列表:${NC}"
        echo ""
        git tag -l | sort -V
        ;;

    4)
        echo -e "${YELLOW}手动添加更新条目${NC}"
        echo ""
        read -p "输入版本号 (如 0.0.3): " version
        read -p "输入变更类型 (Added/Changed/Deprecated/Removed/Fixed/Security): " change_type
        read -p "输入变更描述: " description

        if [ -z "$version" ] || [ -z "$change_type" ] || [ -z "$description" ]; then
            echo -e "${RED}错误: 所有字段都为必填${NC}"
            exit 1
        fi

        # 获取当前日期
        date=$(date +"%Y-%m-%d")

        # 创建临时文件
        temp_file=$(mktemp)

        # 读取原文件
        if [ -f "CHANGELOG.md" ]; then
            # 保存 Unreleased 部分
            awk '/^## \[Unreleased\]/,/^## \[[0-9]+\.[0-9]+\.[0-9]+\]/ {print}' CHANGELOG.md > "$temp_file"
        else
            echo "# Changelog" > "$temp_file"
            echo "" >> "$temp_file"
            echo "All notable changes to this project will be documented in this file." >> "$temp_file"
            echo "" >> "$temp_file"
            echo "## [Unreleased]" >> "$temp_file"
        fi

        # 如果 temp_file 不包含新的版本条目，添加它
        if ! grep -q "## \[$version\]" "$temp_file"; then
            # 在 Unreleased 部分之后插入新版本
            sed -i '' "/^## \[Unreleased\]/a\\
\\
## [$version] - $date\\
\\
### $change_type\\
- $description\\
" "$temp_file"
        else
            # 在已有版本下添加新条目
            sed -i '' "/^## \[$version\]/,/^## \[.*\]/ { /^## \[$version\]/!b; a\\
\\
### $change_type\\
- $description\\
}" "$temp_file"
        fi

        # 移动临时文件回原位置
        mv "$temp_file" CHANGELOG.md

        echo -e "${GREEN}已添加更新条目到 CHANGELOG.md${NC}"
        echo ""
        echo "更新内容:"
        echo "## [$version] - $date"
        echo ""
        echo "### $change_type"
        echo "- $description"
        ;;

    5)
        echo "退出"
        exit 0
        ;;

    *)
        echo -e "${RED}无效选项${NC}"
        exit 1
        ;;
esac

echo ""
echo "========================================"
