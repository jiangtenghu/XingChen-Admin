#!/bin/bash

# OAuth 2.1 + JWT 认证系统接口功能测试脚本
# 
# 使用方法：
# 1. 启动服务：mvn spring-boot:run
# 2. 运行测试：chmod +x test-api.sh && ./test-api.sh

echo "🚀 开始OAuth 2.1 + JWT认证系统接口测试"

# 测试配置
BASE_URL="http://localhost:8081"
USERNAME="admin"
PASSWORD="admin123"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 测试结果统计
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# 测试函数
test_endpoint() {
    local test_name="$1"
    local method="$2"
    local url="$3"
    local headers="$4"
    local data="$5"
    local expected_code="$6"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    echo -e "${BLUE}📋 测试: $test_name${NC}"
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" $headers "$url")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" $headers -d "$data" "$url")
    fi
    
    # 提取HTTP状态码
    http_code=$(echo "$response" | tail -n1)
    response_body=$(echo "$response" | head -n -1)
    
    echo "   请求: $method $url"
    echo "   响应码: $http_code"
    echo "   响应体: $response_body"
    
    if [ "$http_code" = "$expected_code" ]; then
        echo -e "   ${GREEN}✅ 测试通过${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "   ${RED}❌ 测试失败 (期望: $expected_code, 实际: $http_code)${NC}"
        FAILED_TESTS=$((FAILED_TESTS + 1))
    fi
    
    echo
}

# 等待服务启动
wait_for_service() {
    echo -e "${YELLOW}⏳ 等待服务启动...${NC}"
    for i in {1..30}; do
        if curl -s "$BASE_URL/actuator/health" > /dev/null 2>&1; then
            echo -e "${GREEN}✅ 服务已启动${NC}"
            return 0
        fi
        echo "   尝试 $i/30..."
        sleep 2
    done
    echo -e "${RED}❌ 服务启动超时${NC}"
    exit 1
}

# 检查服务是否运行
check_service() {
    if ! curl -s "$BASE_URL/actuator/health" > /dev/null 2>&1; then
        echo -e "${YELLOW}⚠️  服务未运行，请先启动服务：${NC}"
        echo "   mvn spring-boot:run"
        echo
        echo -e "${YELLOW}或者在后台启动：${NC}"
        echo "   nohup mvn spring-boot:run > app.log 2>&1 &"
        echo
        read -p "按回车键继续测试，或按Ctrl+C退出..."
        wait_for_service
    fi
}

# 主测试流程
main() {
    echo "🔍 检查服务状态..."
    check_service
    
    echo -e "${GREEN}🎯 开始接口功能测试${NC}"
    echo
    
    # 1. 健康检查
    test_endpoint \
        "健康检查" \
        "GET" \
        "$BASE_URL/actuator/health" \
        "" \
        "" \
        "200"
    
    # 2. Swagger文档访问
    test_endpoint \
        "Swagger文档" \
        "GET" \
        "$BASE_URL/swagger-ui.html" \
        "" \
        "" \
        "200"
    
    # 3. 用户登录 - 成功场景
    test_endpoint \
        "用户登录(成功)" \
        "POST" \
        "$BASE_URL/auth/login" \
        "-H 'Content-Type: application/json'" \
        "{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}" \
        "200"
    
    # 4. 用户登录 - 失败场景
    test_endpoint \
        "用户登录(密码错误)" \
        "POST" \
        "$BASE_URL/auth/login" \
        "-H 'Content-Type: application/json'" \
        "{\"username\":\"$USERNAME\",\"password\":\"wrongpassword\"}" \
        "401"
    
    # 5. 用户登录 - 参数验证
    test_endpoint \
        "用户登录(参数为空)" \
        "POST" \
        "$BASE_URL/auth/login" \
        "-H 'Content-Type: application/json'" \
        "{\"username\":\"\",\"password\":\"\"}" \
        "400"
    
    # 6. OAuth 2.1密码模式
    test_endpoint \
        "OAuth密码模式" \
        "POST" \
        "$BASE_URL/oauth2/token" \
        "-H 'Content-Type: application/x-www-form-urlencoded'" \
        "grant_type=password&client_id=web-admin-client&client_secret=web-admin-secret&username=$USERNAME&password=$PASSWORD" \
        "200"
    
    # 7. OAuth 2.1客户端凭证模式
    test_endpoint \
        "OAuth客户端凭证模式" \
        "POST" \
        "$BASE_URL/oauth2/token" \
        "-H 'Content-Type: application/x-www-form-urlencoded'" \
        "grant_type=client_credentials&client_id=service-client&client_secret=service-secret&scope=service" \
        "200"
    
    # 8. 获取用户信息
    test_endpoint \
        "获取用户信息" \
        "GET" \
        "$BASE_URL/auth/me" \
        "-H 'X-User-Id: 1'" \
        "" \
        "200"
    
    # 9. 获取用户信息 - 缺少用户ID
    test_endpoint \
        "获取用户信息(缺少ID)" \
        "GET" \
        "$BASE_URL/auth/me" \
        "" \
        "" \
        "400"
    
    # 10. 用户注销
    test_endpoint \
        "用户注销" \
        "POST" \
        "$BASE_URL/auth/logout" \
        "-H 'Authorization: Bearer fake-token'" \
        "" \
        "200"
    
    # 11. 不支持的授权类型
    test_endpoint \
        "不支持的授权类型" \
        "POST" \
        "$BASE_URL/oauth2/token" \
        "-H 'Content-Type: application/x-www-form-urlencoded'" \
        "grant_type=unsupported&client_id=web-admin-client" \
        "400"
    
    # 12. 令牌撤销
    test_endpoint \
        "令牌撤销" \
        "POST" \
        "$BASE_URL/oauth2/revoke" \
        "-H 'Content-Type: application/x-www-form-urlencoded'" \
        "token=fake-token&client_id=web-admin-client&client_secret=web-admin-secret" \
        "200"
    
    # 13. 令牌自省
    test_endpoint \
        "令牌自省" \
        "POST" \
        "$BASE_URL/oauth2/introspect" \
        "-H 'Content-Type: application/x-www-form-urlencoded'" \
        "token=fake-token&client_id=web-admin-client&client_secret=web-admin-secret" \
        "200"
    
    # 测试结果统计
    echo -e "${BLUE}📊 测试结果统计${NC}"
    echo "   总测试数: $TOTAL_TESTS"
    echo -e "   通过: ${GREEN}$PASSED_TESTS${NC}"
    echo -e "   失败: ${RED}$FAILED_TESTS${NC}"
    
    if [ $FAILED_TESTS -eq 0 ]; then
        echo -e "${GREEN}🎉 所有测试通过！OAuth 2.1 + JWT认证系统功能正常${NC}"
        exit 0
    else
        echo -e "${RED}⚠️  有 $FAILED_TESTS 个测试失败，请检查实现${NC}"
        exit 1
    fi
}

# 显示使用说明
show_usage() {
    echo "OAuth 2.1 + JWT 认证系统接口测试脚本"
    echo
    echo "使用方法："
    echo "  1. 启动认证服务："
    echo "     mvn spring-boot:run"
    echo
    echo "  2. 运行测试："
    echo "     chmod +x test-api.sh"
    echo "     ./test-api.sh"
    echo
    echo "  3. 查看详细输出："
    echo "     ./test-api.sh | tee test-results.log"
    echo
}

# 检查参数
if [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
    show_usage
    exit 0
fi

# 运行主测试
main
