#!/bin/bash

# =============================================
# 数据库迁移执行脚本
# 从分散式架构迁移到统一身份管理架构
# =============================================

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 数据库配置
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-3306}"
DB_USER="${DB_USER:-root}"
DB_PASSWORD="${DB_PASSWORD:-123456}"

echo -e "${BLUE}🚀 === 数据库迁移脚本 ===${NC}"
echo ""

# 检查MySQL连接
echo -e "${BLUE}🔍 检查数据库连接...${NC}"
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "SELECT 1;" > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 数据库连接成功${NC}"
else
    echo -e "${RED}❌ 数据库连接失败，请检查配置${NC}"
    exit 1
fi

# 创建备份目录
BACKUP_DIR="backup/migration_$(date +%Y%m%d_%H%M%S)"
mkdir -p "$BACKUP_DIR"
echo -e "${YELLOW}📁 创建备份目录: $BACKUP_DIR${NC}"

# 备份现有数据库
echo -e "${BLUE}💾 备份现有数据库...${NC}"

# 检查数据库是否存在并备份
for db in admin_user admin_system admin_auth; do
    DB_EXISTS=$(mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "SHOW DATABASES LIKE '$db';" | grep "$db" || true)
    if [ ! -z "$DB_EXISTS" ]; then
        echo -e "${YELLOW}📦 备份数据库: $db${NC}"
        mysqldump -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" \
            --single-transaction --routines --triggers "$db" > "$BACKUP_DIR/${db}_backup.sql"
        echo -e "${GREEN}✅ $db 备份完成${NC}"
    else
        echo -e "${YELLOW}⚠️ 数据库 $db 不存在，跳过备份${NC}"
    fi
done

# 执行新数据库初始化
echo -e "${BLUE}🏗️ 创建新的统一身份管理数据库...${NC}"
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" < sql/admin_identity_schema.sql
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 数据库初始化完成${NC}"
else
    echo -e "${RED}❌ 数据库初始化失败${NC}"
    exit 1
fi

# 数据迁移 (如果有现有数据)
echo -e "${BLUE}🔄 执行数据迁移...${NC}"

# 检查是否有需要迁移的数据
USER_DATA_EXISTS=$(mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "SELECT COUNT(*) FROM admin_user.sys_user;" 2>/dev/null | tail -n 1 || echo "0")
SYSTEM_DATA_EXISTS=$(mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "SELECT COUNT(*) FROM admin_system.sys_role;" 2>/dev/null | tail -n 1 || echo "0")

if [ "$USER_DATA_EXISTS" -gt 0 ] || [ "$SYSTEM_DATA_EXISTS" -gt 0 ]; then
    echo -e "${YELLOW}📊 发现现有数据，开始迁移...${NC}"
    
    # 执行数据迁移
    mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" < sql/migration_guide.sql
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ 数据迁移完成${NC}"
    else
        echo -e "${RED}❌ 数据迁移失败${NC}"
        echo -e "${YELLOW}💡 请检查迁移脚本和数据库状态${NC}"
        exit 1
    fi
else
    echo -e "${YELLOW}⚠️ 未发现需要迁移的数据，使用默认数据${NC}"
fi

# 验证迁移结果
echo -e "${BLUE}🔍 验证迁移结果...${NC}"
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "
USE admin_identity;
SELECT '=== 迁移结果验证 ===' as message;
SELECT 
    '租户数量' as item,
    COUNT(*) as count
FROM sys_tenant
UNION ALL
SELECT 
    '用户数量' as item,
    COUNT(*) as count  
FROM sys_user
UNION ALL
SELECT 
    '角色数量' as item,
    COUNT(*) as count
FROM sys_role
UNION ALL
SELECT 
    '权限数量' as item, 
    COUNT(*) as count
FROM sys_permission;
"

# 更新Docker Compose中的数据库初始化脚本
echo -e "${BLUE}🔧 更新Docker配置...${NC}"
if [ -f "docker-compose.yml" ]; then
    # 更新docker-compose.yml中的初始化脚本
    sed -i.bak 's|./sql/init.sql:/docker-entrypoint-initdb.d/init.sql|./sql/admin_identity_schema.sql:/docker-entrypoint-initdb.d/admin_identity_schema.sql|g' docker-compose.yml
    echo -e "${GREEN}✅ Docker配置已更新${NC}"
fi

# 生成迁移报告
REPORT_FILE="$BACKUP_DIR/migration_report.txt"
cat > "$REPORT_FILE" << EOF
=== 数据库迁移报告 ===
迁移时间: $(date)
备份目录: $BACKUP_DIR

迁移完成项目:
✅ 创建 admin_identity 数据库
✅ 初始化统一身份管理表结构
✅ 迁移用户数据
✅ 迁移角色权限数据
✅ 更新Docker配置

下一步操作:
1. 启动新的 admin-identity 服务
2. 验证API接口功能
3. 更新前端调用地址
4. 执行完整功能测试

备注:
- 原数据库已备份到: $BACKUP_DIR
- 新数据库: admin_identity
- 服务端口: 8082
EOF

echo ""
echo -e "${GREEN}🎉 === 数据库迁移完成！ ===${NC}"
echo ""
echo -e "${BLUE}📋 迁移总结:${NC}"
echo -e "  • 备份目录: ${YELLOW}$BACKUP_DIR${NC}"
echo -e "  • 迁移报告: ${YELLOW}$REPORT_FILE${NC}"
echo -e "  • 新数据库: ${GREEN}admin_identity${NC}"
echo ""
echo -e "${BLUE}📝 下一步操作:${NC}"
echo -e "  1. ${YELLOW}启动服务${NC}: docker-compose up -d admin-identity"
echo -e "  2. ${YELLOW}验证接口${NC}: curl http://localhost:8082/actuator/health"
echo -e "  3. ${YELLOW}查看日志${NC}: docker logs admin-identity"
echo ""
echo -e "${GREEN}✨ 迁移成功完成！${NC}"
