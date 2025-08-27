#!/bin/bash

# 检查所有后端服务状态的脚本

echo "🔍 检查Spring Cloud Admin后端服务状态"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 服务列表
declare -A services=(
    ["认证服务"]="http://localhost:8081/actuator/health"
    ["用户服务"]="http://localhost:8082/actuator/health"
    ["系统服务"]="http://localhost:8083/actuator/health"
    ["网关服务"]="http://localhost:8080/actuator/health"
    ["Nacos注册中心"]="http://localhost:8848/nacos"
)

# 检查服务状态
check_service() {
    local name=$1
    local url=$2
    
    echo -n "检查 $name ... "
    
    if curl -f -s "$url" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ 运行中${NC}"
        return 0
    else
        echo -e "${RED}❌ 未运行${NC}"
        return 1
    fi
}

# 检查进程状态
check_processes() {
    echo -e "\n${BLUE}📋 Java进程状态:${NC}"
    
    processes=("admin-auth" "admin-user" "admin-system" "admin-gateway")
    
    for process in "${processes[@]}"; do
        if pgrep -f "$process" > /dev/null; then
            echo -e "  ${GREEN}✅ $process 进程运行中${NC}"
        else
            echo -e "  ${RED}❌ $process 进程未运行${NC}"
        fi
    done
}

# 检查Docker容器状态
check_docker() {
    echo -e "\n${BLUE}📋 Docker容器状态:${NC}"
    
    if command -v docker &> /dev/null; then
        containers=("mysql" "spring-admin-redis" "spring-admin-nacos")
        
        for container in "${containers[@]}"; do
            if docker ps --format "table {{.Names}}" | grep -q "$container"; then
                echo -e "  ${GREEN}✅ $container 容器运行中${NC}"
            else
                echo -e "  ${RED}❌ $container 容器未运行${NC}"
            fi
        done
    else
        echo -e "  ${YELLOW}⚠️ Docker未安装或未运行${NC}"
    fi
}

# 主检查流程
main() {
    echo -e "${BLUE}📊 服务健康检查:${NC}"
    
    running_count=0
    total_count=${#services[@]}
    
    for service_name in "${!services[@]}"; do
        if check_service "$service_name" "${services[$service_name]}"; then
            ((running_count++))
        fi
    done
    
    echo -e "\n${BLUE}📈 服务状态统计:${NC}"
    echo "  运行中: $running_count/$total_count"
    
    if [ $running_count -eq $total_count ]; then
        echo -e "  ${GREEN}🎉 所有服务运行正常！${NC}"
    else
        echo -e "  ${YELLOW}⚠️ 有 $((total_count - running_count)) 个服务未运行${NC}"
    fi
    
    # 检查进程和容器状态
    check_processes
    check_docker
    
    echo -e "\n${BLUE}🔗 服务访问地址:${NC}"
    echo "  • API网关: http://localhost:8080"
    echo "  • 认证服务: http://localhost:8081"
    echo "  • 用户服务: http://localhost:8082"
    echo "  • 系统服务: http://localhost:8083"
    echo "  • Nacos控制台: http://localhost:8848/nacos (nacos/nacos)"
    echo "  • API文档: http://localhost:8080/doc.html"
    
    echo -e "\n${BLUE}📋 启动命令参考:${NC}"
    echo "  • 启动所有服务: ./start.sh dev"
    echo "  • 停止所有服务: ./start.sh stop"
    echo "  • 重启服务: ./start.sh restart"
    echo "  • Docker启动: ./start.sh docker"
}

# 运行检查
main
