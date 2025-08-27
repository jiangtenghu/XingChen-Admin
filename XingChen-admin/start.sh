#!/bin/bash

# Spring Cloud Admin 项目启动脚本
# 使用方法: ./start.sh [dev|docker|stop|restart]

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

log_step() {
    echo -e "${BLUE}[STEP]${NC} $1"
}

# 检查命令是否存在
check_command() {
    if ! command -v $1 &> /dev/null; then
        log_error "$1 命令未找到，请先安装 $1"
        exit 1
    fi
}

# 检查端口是否被占用
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null ; then
        log_warn "端口 $1 已被占用"
        return 1
    fi
    return 0
}

# 等待服务启动
wait_for_service() {
    local url=$1
    local service_name=$2
    local max_attempts=30
    local attempt=1
    
    log_info "等待 $service_name 启动..."
    
    while [ $attempt -le $max_attempts ]; do
        if curl -f -s $url > /dev/null 2>&1; then
            log_info "$service_name 启动成功！"
            return 0
        fi
        
        echo -n "."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    log_error "$service_name 启动超时！"
    return 1
}

# 开发环境启动
start_dev() {
    log_step "启动开发环境..."
    
    # 检查必要的命令
    check_command "mvn"
    check_command "docker-compose"
    
    # 检查端口
    ports=(3306 6379 8848 8080 8081 8082 8083)
    for port in "${ports[@]}"; do
        if ! check_port $port; then
            log_error "请先停止占用端口 $port 的进程"
            exit 1
        fi
    done
    
    # 启动基础服务
    log_step "启动基础服务 (MySQL, Redis, Nacos)..."
    docker-compose up -d mysql redis nacos
    
    # 等待基础服务启动
    wait_for_service "http://localhost:8848/nacos" "Nacos"
    
    # 编译项目
    log_step "编译项目..."
    mvn clean compile -q
    
    # 启动微服务
    log_step "启动微服务..."
    
    # 后台启动各个服务
    cd admin-gateway && nohup mvn spring-boot:run > ../logs/gateway.log 2>&1 &
    cd ../admin-auth && nohup mvn spring-boot:run > ../logs/auth.log 2>&1 &
    cd ../admin-user && nohup mvn spring-boot:run > ../logs/user.log 2>&1 &
    cd ../admin-system && nohup mvn spring-boot:run > ../logs/system.log 2>&1 &
    cd ..
    
    # 等待服务启动
    sleep 10
    wait_for_service "http://localhost:8080/actuator/health" "API Gateway"
    wait_for_service "http://localhost:8081/actuator/health" "Auth Service"
    wait_for_service "http://localhost:8082/actuator/health" "User Service"
    wait_for_service "http://localhost:8083/actuator/health" "System Service"
    
    log_info "开发环境启动完成！"
    log_info "API网关: http://localhost:8080"
    log_info "Nacos控制台: http://localhost:8848/nacos (nacos/nacos)"
    log_info "API文档: http://localhost:8080/doc.html"
}

# Docker环境启动
start_docker() {
    log_step "启动Docker环境..."
    
    # 检查Docker
    check_command "docker"
    check_command "docker-compose"
    
    # 构建项目
    log_step "构建项目..."
    mvn clean package -DskipTests -q
    
    # 启动所有服务
    log_step "启动所有服务..."
    docker-compose up -d
    
    # 等待服务启动
    sleep 20
    wait_for_service "http://localhost:8848/nacos" "Nacos"
    wait_for_service "http://localhost:8080/actuator/health" "API Gateway"
    
    log_info "Docker环境启动完成！"
    log_info "API网关: http://localhost:8080"
    log_info "Nacos控制台: http://localhost:8848/nacos (nacos/nacos)"
    log_info "Grafana监控: http://localhost:3000 (admin/admin123)"
    log_info "Zipkin链路追踪: http://localhost:9411"
}

# 停止服务
stop_services() {
    log_step "停止服务..."
    
    # 停止Docker服务
    if [ -f "docker-compose.yml" ]; then
        docker-compose down
    fi
    
    # 停止开发环境的Java进程
    pkill -f "spring-boot:run" || true
    pkill -f "admin-gateway" || true
    pkill -f "admin-auth" || true
    pkill -f "admin-user" || true
    pkill -f "admin-system" || true
    
    log_info "服务已停止"
}

# 重启服务
restart_services() {
    log_step "重启服务..."
    stop_services
    sleep 3
    
    if [ "$1" = "docker" ]; then
        start_docker
    else
        start_dev
    fi
}

# 显示帮助信息
show_help() {
    echo "Spring Cloud Admin 启动脚本"
    echo ""
    echo "使用方法:"
    echo "  ./start.sh dev      - 启动开发环境（本地运行）"
    echo "  ./start.sh docker   - 启动Docker环境"
    echo "  ./start.sh stop     - 停止所有服务"
    echo "  ./start.sh restart  - 重启服务（默认开发环境）"
    echo "  ./start.sh help     - 显示帮助信息"
    echo ""
    echo "开发环境说明:"
    echo "  - 使用Maven启动各个微服务"
    echo "  - 基础服务(MySQL/Redis/Nacos)使用Docker"
    echo "  - 适合开发调试"
    echo ""
    echo "Docker环境说明:"
    echo "  - 所有服务都运行在Docker容器中"
    echo "  - 适合生产部署或完整测试"
    echo ""
}

# 创建日志目录
mkdir -p logs

# 主逻辑
case "$1" in
    "dev")
        start_dev
        ;;
    "docker")
        start_docker
        ;;
    "stop")
        stop_services
        ;;
    "restart")
        restart_services $2
        ;;
    "help"|"-h"|"--help")
        show_help
        ;;
    *)
        log_error "无效的参数: $1"
        show_help
        exit 1
        ;;
esac
