#!/bin/bash

# XingChen Admin 完整服务启动脚本
# 避免服务遗漏，确保所有服务正确启动

set -e

echo "🚀 === XingChen Admin 服务启动脚本 ==="
echo ""

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 检查Docker和Docker Compose
echo -e "${BLUE}🔍 检查环境...${NC}"
if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker 未安装${NC}"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}❌ Docker Compose 未安装${NC}"
    exit 1
fi

# 停止现有服务
echo -e "${YELLOW}🛑 停止现有服务...${NC}"
docker-compose down

# 第一阶段：启动基础服务
echo -e "${BLUE}🏗️ 第一阶段：启动基础设施服务...${NC}"
docker-compose up -d mysql redis nacos
echo -e "${YELLOW}⏳ 等待基础服务启动 (20秒)...${NC}"
sleep 20

# 检查基础服务状态
echo -e "${BLUE}🔍 检查基础服务状态...${NC}"
for service in mysql redis nacos; do
    if docker-compose ps $service | grep -q "Up"; then
        echo -e "${GREEN}✅ $service 启动成功${NC}"
    else
        echo -e "${RED}❌ $service 启动失败${NC}"
        exit 1
    fi
done

# 第二阶段：启动微服务
echo -e "${BLUE}🚀 第二阶段：启动微服务应用...${NC}"
docker-compose up -d admin-gateway admin-auth admin-identity admin-system
echo -e "${YELLOW}⏳ 等待微服务启动 (30秒)...${NC}"
sleep 30

# 检查微服务状态
echo -e "${BLUE}🔍 检查微服务状态...${NC}"
for service in admin-gateway admin-auth admin-identity admin-system; do
    if docker-compose ps $service | grep -q "Up"; then
        echo -e "${GREEN}✅ $service 启动成功${NC}"
    else
        echo -e "${RED}❌ $service 启动失败${NC}"
        echo -e "${YELLOW}📋 检查日志: docker logs $service${NC}"
    fi
done

# 第三阶段：启动监控服务
echo -e "${BLUE}📊 第三阶段：启动监控服务...${NC}"
docker-compose up -d prometheus grafana zipkin
echo -e "${YELLOW}⏳ 等待监控服务启动 (15秒)...${NC}"
sleep 15

# 检查监控服务状态
echo -e "${BLUE}🔍 检查监控服务状态...${NC}"
for service in prometheus grafana zipkin; do
    if docker-compose ps $service | grep -q "Up"; then
        echo -e "${GREEN}✅ $service 启动成功${NC}"
    else
        echo -e "${RED}❌ $service 启动失败${NC}"
    fi
done

# 显示服务状态总览
echo ""
echo -e "${GREEN}🎉 === 服务启动完成 ===${NC}"
echo ""
echo -e "${BLUE}📊 服务状态总览:${NC}"
docker-compose ps

echo ""
echo -e "${BLUE}🔗 服务访问地址:${NC}"
echo -e "${GREEN}核心服务:${NC}"
echo "  • API网关:         http://localhost:8080"
echo "  • 认证服务:        http://localhost:8081"
echo "  • 身份管理服务:    http://localhost:8082"
echo "  • 系统配置服务:    http://localhost:8083"
echo ""
echo -e "${GREEN}管理界面:${NC}"
echo "  • Nacos:      http://localhost:8848/nacos"
echo "  • Grafana:    http://localhost:3000 (admin/admin)"
echo "  • Prometheus: http://localhost:9090"
echo "  • Zipkin:     http://localhost:9411"
echo ""

# 检查服务健康状态
echo -e "${BLUE}🏥 检查服务健康状态...${NC}"
sleep 5

# 检查Nacos
if curl -s http://localhost:8848/nacos/v1/console/health/readiness | grep -q "UP"; then
    echo -e "${GREEN}✅ Nacos 健康检查通过${NC}"
else
    echo -e "${YELLOW}⚠️ Nacos 健康检查失败，可能还在启动中${NC}"
fi

# 检查网关
if curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health | grep -q "200"; then
    echo -e "${GREEN}✅ API网关 健康检查通过${NC}"
else
    echo -e "${YELLOW}⚠️ API网关 健康检查失败，可能还在启动中${NC}"
fi

echo ""
echo -e "${GREEN}🎯 所有服务启动完成！${NC}"
echo -e "${BLUE}💡 提示: 如果某些服务显示失败，请等待1-2分钟后重新检查${NC}"
echo -e "${BLUE}📋 查看特定服务日志: docker logs <服务名>${NC}"
