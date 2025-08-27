#!/bin/bash

# =============================================
# 微服务架构优化验证脚本
# 验证所有优化工作是否完成并正常工作
# =============================================

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🔍 === 微服务架构优化验证 ===${NC}"
echo ""

# 验证结果统计
TOTAL_CHECKS=0
PASSED_CHECKS=0
FAILED_CHECKS=0

# 检查结果记录
check_result() {
    local check_name="$1"
    local result="$2"
    local message="$3"
    
    TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
    
    if [ "$result" == "PASS" ]; then
        PASSED_CHECKS=$((PASSED_CHECKS + 1))
        echo -e "  ${GREEN}✅ $check_name${NC}"
        [ ! -z "$message" ] && echo -e "     $message"
    else
        FAILED_CHECKS=$((FAILED_CHECKS + 1))
        echo -e "  ${RED}❌ $check_name${NC}"
        [ ! -z "$message" ] && echo -e "     ${RED}$message${NC}"
    fi
}

# =============================================
# 1. 项目结构验证
# =============================================

echo -e "${BLUE}📁 1. 项目结构验证${NC}"

# 检查新服务目录
if [ -d "admin-identity" ]; then
    check_result "admin-identity服务目录存在" "PASS" "统一身份管理服务目录已创建"
else
    check_result "admin-identity服务目录存在" "FAIL" "统一身份管理服务目录不存在"
fi

# 检查旧服务目录已删除
if [ ! -d "admin-user" ]; then
    check_result "admin-user服务已删除" "PASS" "旧用户服务目录已清理"
else
    check_result "admin-user服务已删除" "FAIL" "旧用户服务目录仍然存在"
fi

# 检查admin-system保留
if [ -d "admin-system" ]; then
    check_result "admin-system服务保留" "PASS" "系统配置服务目录保留"
else
    check_result "admin-system服务保留" "FAIL" "系统配置服务目录不存在"
fi

# 检查admin-identity核心文件
identity_files=(
    "admin-identity/pom.xml"
    "admin-identity/src/main/java/com/admin/identity/IdentityApplication.java"
    "admin-identity/src/main/java/com/admin/identity/domain/entity/User.java"
    "admin-identity/src/main/java/com/admin/identity/domain/entity/Tenant.java"
    "admin-identity/src/main/java/com/admin/identity/service/IdentityAggregateService.java"
    "admin-identity/src/main/resources/application.yml"
)

for file in "${identity_files[@]}"; do
    if [ -f "$file" ]; then
        check_result "核心文件: $(basename $file)" "PASS"
    else
        check_result "核心文件: $(basename $file)" "FAIL" "文件不存在: $file"
    fi
done

echo ""

# =============================================
# 2. 配置文件验证
# =============================================

echo -e "${BLUE}⚙️ 2. 配置文件验证${NC}"

# 检查主pom.xml模块配置
if grep -q "admin-identity" pom.xml && ! grep -q "admin-user" pom.xml; then
    check_result "主pom.xml模块配置" "PASS" "模块配置已更新"
else
    check_result "主pom.xml模块配置" "FAIL" "模块配置未正确更新"
fi

# 检查docker-compose.yml配置
if grep -q "admin-identity" docker-compose.yml && ! grep -q "admin-user" docker-compose.yml; then
    check_result "docker-compose.yml配置" "PASS" "Docker配置已更新"
else
    check_result "docker-compose.yml配置" "FAIL" "Docker配置未正确更新"
fi

# 检查启动脚本配置
if grep -q "admin-identity" start-all-services.sh && ! grep -q "admin-user" start-all-services.sh; then
    check_result "启动脚本配置" "PASS" "启动脚本已更新"
else
    check_result "启动脚本配置" "FAIL" "启动脚本未正确更新"
fi

echo ""

# =============================================
# 3. 数据库脚本验证
# =============================================

echo -e "${BLUE}🗄️ 3. 数据库脚本验证${NC}"

# 检查统一身份管理数据库脚本
if [ -f "sql/admin_identity_schema.sql" ]; then
    # 检查脚本内容
    if grep -q "sys_tenant" sql/admin_identity_schema.sql && 
       grep -q "sys_user" sql/admin_identity_schema.sql && 
       grep -q "sys_role" sql/admin_identity_schema.sql && 
       grep -q "sys_permission" sql/admin_identity_schema.sql; then
        check_result "统一身份管理数据库脚本" "PASS" "数据库脚本内容完整"
    else
        check_result "统一身份管理数据库脚本" "FAIL" "数据库脚本内容不完整"
    fi
else
    check_result "统一身份管理数据库脚本" "FAIL" "数据库脚本文件不存在"
fi

# 检查数据库迁移脚本
if [ -f "sql/migration_guide.sql" ]; then
    check_result "数据库迁移脚本" "PASS" "迁移脚本已创建"
else
    check_result "数据库迁移脚本" "FAIL" "迁移脚本不存在"
fi

# 检查迁移执行脚本
if [ -f "scripts/migrate_database.sh" ] && [ -x "scripts/migrate_database.sh" ]; then
    check_result "数据库迁移执行脚本" "PASS" "迁移执行脚本可用"
else
    check_result "数据库迁移执行脚本" "FAIL" "迁移执行脚本不可用"
fi

echo ""

# =============================================
# 4. 代码清理验证
# =============================================

echo -e "${BLUE}🧹 4. 代码清理验证${NC}"

# 检查admin-system中的权限相关代码是否已删除
permission_files=(
    "admin-system/src/main/java/com/admin/system/domain/entity/Role.java"
    "admin-system/src/main/java/com/admin/system/service/RoleService.java"
    "admin-system/src/main/java/com/admin/system/service/PermissionService.java"
    "admin-system/src/main/java/com/admin/system/controller/RoleController.java"
    "admin-system/src/main/java/com/admin/system/controller/PermissionController.java"
)

all_deleted=true
for file in "${permission_files[@]}"; do
    if [ -f "$file" ]; then
        all_deleted=false
        break
    fi
done

if $all_deleted; then
    check_result "admin-system权限代码清理" "PASS" "权限相关代码已删除"
else
    check_result "admin-system权限代码清理" "FAIL" "权限相关代码未完全删除"
fi

# 检查admin-system保留的功能
if [ -f "admin-system/src/main/java/com/admin/system/domain/entity/Menu.java" ] &&
   [ -f "admin-system/src/main/java/com/admin/system/service/MenuService.java" ]; then
    check_result "admin-system菜单功能保留" "PASS" "菜单管理功能已保留"
else
    check_result "admin-system菜单功能保留" "FAIL" "菜单管理功能丢失"
fi

# 检查过时文档删除
outdated_docs=(
    "PERMISSION_SYSTEM_IMPLEMENTATION.md"
    "TENANT_MANAGEMENT_IMPLEMENTATION.md"
    "REAL_DATABASE_IMPLEMENTATION.md"
    "PERFECT_ORGANIZATION_DESIGN.md"
)

docs_deleted=true
for doc in "${outdated_docs[@]}"; do
    if [ -f "$doc" ]; then
        docs_deleted=false
        break
    fi
done

if $docs_deleted; then
    check_result "过时文档清理" "PASS" "过时文档已删除"
else
    check_result "过时文档清理" "FAIL" "过时文档未完全清理"
fi

echo ""

# =============================================
# 5. 前端适配验证
# =============================================

echo -e "${BLUE}🖥️ 5. 前端适配验证${NC}"

# 检查前端API适配文档
if [ -f "XingChen-Vue/API_ADAPTATION_GUIDE.md" ]; then
    check_result "前端API适配指南" "PASS" "前端适配文档已创建"
else
    check_result "前端API适配指南" "FAIL" "前端适配文档不存在"
fi

# 检查前端用户服务更新
if [ -f "XingChen-Vue/apps/admin/src/services/user.service.ts" ]; then
    if grep -q "/api/identity/users" XingChen-Vue/apps/admin/src/services/user.service.ts; then
        check_result "前端用户服务更新" "PASS" "用户服务API路径已更新"
    else
        check_result "前端用户服务更新" "FAIL" "用户服务API路径未更新"
    fi
else
    check_result "前端用户服务更新" "FAIL" "前端用户服务文件不存在"
fi

# 检查租户服务创建
if [ -f "XingChen-Vue/apps/admin/src/services/tenant.service.ts" ]; then
    check_result "前端租户服务创建" "PASS" "租户服务已创建"
else
    check_result "前端租户服务创建" "FAIL" "租户服务未创建"
fi

# 检查路由服务更新
if [ -f "XingChen-Vue/apps/admin/src/services/router.service.ts" ]; then
    if grep -q "/api/identity/users" XingChen-Vue/apps/admin/src/services/router.service.ts; then
        check_result "前端路由服务更新" "PASS" "路由服务API路径已更新"
    else
        check_result "前端路由服务更新" "FAIL" "路由服务API路径未更新"
    fi
fi

echo ""

# =============================================
# 6. 测试文件验证
# =============================================

echo -e "${BLUE}🧪 6. 测试文件验证${NC}"

# 检查集成测试脚本
if [ -f "test/integration_test.sh" ] && [ -x "test/integration_test.sh" ]; then
    check_result "集成测试脚本" "PASS" "集成测试脚本可用"
else
    check_result "集成测试脚本" "FAIL" "集成测试脚本不可用"
fi

# 检查单元测试文件
if [ -f "admin-identity/src/test/java/com/admin/identity/controller/UserControllerTest.java" ]; then
    check_result "单元测试用例" "PASS" "单元测试用例已创建"
else
    check_result "单元测试用例" "FAIL" "单元测试用例不存在"
fi

# 检查测试配置文件
if [ -f "admin-identity/src/test/resources/application-test.yml" ]; then
    check_result "测试配置文件" "PASS" "测试配置文件已创建"
else
    check_result "测试配置文件" "FAIL" "测试配置文件不存在"
fi

echo ""

# =============================================
# 7. 文档更新验证
# =============================================

echo -e "${BLUE}📚 7. 文档更新验证${NC}"

# 检查新文档创建
new_docs=(
    "doc/UNIFIED_IDENTITY_SERVICE_DESIGN.md"
    "doc/MICROSERVICE_OPTIMIZATION_SUMMARY.md"
    "doc/CLEANUP_SUMMARY.md"
    "admin-identity/README.md"
)

for doc in "${new_docs[@]}"; do
    if [ -f "$doc" ]; then
        check_result "新文档: $(basename $doc)" "PASS"
    else
        check_result "新文档: $(basename $doc)" "FAIL" "文档不存在: $doc"
    fi
done

# 检查README更新
if [ -f "README.md" ]; then
    if grep -q "admin-identity" README.md && ! grep -q "admin-user" README.md; then
        check_result "主README更新" "PASS" "主README已更新"
    else
        check_result "主README更新" "FAIL" "主README未正确更新"
    fi
fi

# 检查admin-system文档更新
if [ -f "admin-system/doc/README.md" ]; then
    if grep -q "系统配置服务" admin-system/doc/README.md; then
        check_result "admin-system文档更新" "PASS" "admin-system文档已更新"
    else
        check_result "admin-system文档更新" "FAIL" "admin-system文档未更新"
    fi
fi

echo ""

# =============================================
# 8. 编译验证 (可选)
# =============================================

echo -e "${BLUE}🔨 8. 编译验证${NC}"

# Maven编译验证
if command -v mvn >/dev/null 2>&1; then
    echo -e "${YELLOW}正在进行Maven编译验证...${NC}"
    if mvn clean compile -q >/dev/null 2>&1; then
        check_result "Maven编译" "PASS" "项目编译成功"
    else
        check_result "Maven编译" "FAIL" "项目编译失败"
    fi
else
    check_result "Maven编译" "SKIP" "Maven未安装，跳过编译验证"
fi

echo ""

# =============================================
# 9. 总结和建议
# =============================================

echo -e "${GREEN}🎉 === 验证完成总结 ===${NC}"
echo ""
echo -e "${BLUE}📊 验证统计:${NC}"
echo -e "  • 总检查项: ${YELLOW}$TOTAL_CHECKS${NC}"
echo -e "  • 通过检查: ${GREEN}$PASSED_CHECKS${NC}"
echo -e "  • 失败检查: ${RED}$FAILED_CHECKS${NC}"

# 计算通过率
if [ $TOTAL_CHECKS -gt 0 ]; then
    pass_rate=$(( PASSED_CHECKS * 100 / TOTAL_CHECKS ))
    echo -e "  • 通过率: ${YELLOW}$pass_rate%${NC}"
else
    pass_rate=0
    echo -e "  • 通过率: ${RED}0%${NC}"
fi

echo ""

# 根据验证结果给出建议
if [ $FAILED_CHECKS -eq 0 ]; then
    echo -e "${GREEN}✅ 所有验证项通过！微服务架构优化工作完成！${NC}"
    echo ""
    echo -e "${BLUE}🚀 下一步操作建议:${NC}"
    echo -e "  1. ${GREEN}执行数据库迁移${NC}: ./scripts/migrate_database.sh"
    echo -e "  2. ${GREEN}启动新架构服务${NC}: ./start-all-services.sh"
    echo -e "  3. ${GREEN}运行集成测试${NC}: ./test/integration_test.sh"
    echo -e "  4. ${GREEN}部署到测试环境${NC}: 验证完整功能"
    echo -e "  5. ${GREEN}生产环境部署${NC}: 规划生产发布"
elif [ $pass_rate -ge 80 ]; then
    echo -e "${YELLOW}⚠️ 大部分验证通过，但需要解决部分问题${NC}"
    echo ""
    echo -e "${BLUE}🔧 修复建议:${NC}"
    echo -e "  • 检查失败的验证项"
    echo -e "  • 补充缺失的文件或配置"
    echo -e "  • 重新运行验证脚本"
else
    echo -e "${RED}❌ 验证失败较多，需要进一步完善${NC}"
    echo ""
    echo -e "${BLUE}🔧 修复建议:${NC}"
    echo -e "  • 重新检查优化步骤"
    echo -e "  • 补充缺失的核心文件"
    echo -e "  • 检查配置文件正确性"
    echo -e "  • 查看详细错误信息"
fi

echo ""
echo -e "${BLUE}📋 详细信息:${NC}"
echo -e "  • 验证时间: $(date)"
echo -e "  • 工作目录: $(pwd)"
echo -e "  • 验证脚本: $0"

# 根据验证结果设置退出码
if [ $FAILED_CHECKS -eq 0 ]; then
    echo ""
    echo -e "${GREEN}🎯 恭喜！微服务架构优化验证全部通过！${NC}"
    exit 0
else
    echo ""
    echo -e "${YELLOW}⚠️ 请根据失败项进行修复后重新验证${NC}"
    exit 1
fi
