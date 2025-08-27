# Spring Cloud Admin 分布式微服务管理系统

## 项目介绍

基于 Spring Boot 3.x + Spring Cloud 2023.x 的前后端分离分布式微服务架构，采用主流的微服务技术栈，包含完整的权限管理、用户管理、系统管理等功能模块。

## 技术栈

### 后端技术
- **基础框架**: Spring Boot 3.2.1
- **微服务框架**: Spring Cloud 2023.0.0
- **服务治理**: Spring Cloud Alibaba 2023.0.0.0-RC1
- **服务注册发现**: Nacos 2.3.0
- **配置中心**: Nacos Config
- **API网关**: Spring Cloud Gateway
- **负载均衡**: Spring Cloud LoadBalancer
- **服务调用**: OpenFeign
- **数据库**: MySQL 8.0
- **ORM框架**: MyBatis-Plus 3.5.5
- **连接池**: Druid 1.2.20
- **缓存**: Redis 7
- **认证授权**: JWT + Spring Security
- **监控**: Micrometer + Prometheus
- **链路追踪**: Spring Cloud Sleuth + Zipkin
- **API文档**: Knife4j 4.4.0
- **工具类**: Hutool 5.8.25

### 运维部署
- **容器化**: Docker + Docker Compose
- **监控可视化**: Grafana
- **日志聚合**: 支持 ELK 集成

## 项目结构

```
XingChen-admin/
├── admin-common/           # 公共模块
│   ├── core/              # 核心工具类
│   ├── config/            # 公共配置
│   └── exception/         # 异常处理
├── admin-gateway/         # API网关服务
├── admin-auth/           # 认证授权服务
├── admin-identity/       # 统一身份管理服务 (用户、租户、组织、权限)
├── admin-system/         # 系统配置服务 (菜单、字典)
├── sql/                  # 数据库脚本
├── monitoring/           # 监控配置
├── docker-compose.yml    # Docker编排文件
└── README.md
```

## 功能特性

### 🔐 认证授权
- JWT Token 认证
- 用户登录/注册/注销
- Token 自动刷新
- 权限拦截和验证

### 👤 统一身份管理
- 用户生命周期管理
- 租户管理和配额控制
- 组织架构管理
- 角色权限管理
- 数据权限控制

### ⚙️ 系统配置管理
- 菜单配置管理
- 数据字典管理
- 系统参数配置
- 操作日志记录

### 🌐 微服务特性
- 服务注册与发现
- 配置中心统一管理
- API网关路由转发
- 负载均衡
- 服务间通信
- 分布式链路追踪

### 📊 监控运维
- 应用性能监控
- 服务健康检查
- 链路追踪分析
- 指标数据可视化

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.8+
- Docker & Docker Compose
- MySQL 8.0+
- Redis 7+

### 本地开发

1. **克隆项目**
```bash
git clone <repository-url>
cd XingChen-admin
```

2. **启动基础服务**
```bash
# 启动 MySQL、Redis、Nacos 等基础服务
docker-compose up -d mysql redis nacos
```

3. **编译项目**
```bash
mvn clean compile
```

4. **启动微服务**
```bash
# 按顺序启动各个服务
# 1. 网关服务
cd admin-gateway && mvn spring-boot:run

# 2. 认证服务
cd admin-auth && mvn spring-boot:run

# 3. 用户服务
cd admin-user && mvn spring-boot:run

# 4. 系统服务
cd admin-system && mvn spring-boot:run
```

### Docker 部署

1. **构建项目**
```bash
mvn clean package -DskipTests
```

2. **启动全部服务**
```bash
docker-compose up -d
```

3. **查看服务状态**
```bash
docker-compose ps
```

## 服务端口

| 服务名称 | 端口 | 说明 |
|---------|------|------|
| admin-gateway | 8080 | API网关 |
| admin-auth | 8081 | 认证服务 |
| admin-user | 8082 | 用户服务 |
| admin-system | 8083 | 系统服务 |
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存 |
| Nacos | 8848 | 注册中心/配置中心 |
| Prometheus | 9090 | 监控数据收集 |
| Grafana | 3000 | 监控可视化 |
| Zipkin | 9411 | 链路追踪 |

## API 文档

启动服务后，可以通过以下地址访问 API 文档：

- **网关聚合文档**: http://localhost:8080/doc.html
- **认证服务**: http://localhost:8081/doc.html
- **用户服务**: http://localhost:8082/doc.html
- **系统服务**: http://localhost:8083/doc.html

## 监控面板

- **Nacos 控制台**: http://localhost:8848/nacos (用户名/密码: nacos/nacos)
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (用户名/密码: admin/admin123)
- **Zipkin**: http://localhost:9411

## 默认账号

- **用户名**: admin
- **密码**: 123456

## 开发指南

### 新增微服务

1. 创建新的 Maven 模块
2. 添加必要的依赖（参考现有服务）
3. 配置 Nacos 注册和配置
4. 在网关中添加路由规则
5. 更新 Docker Compose 配置

### 配置说明

- **Nacos 配置**: 所有服务的配置统一在 Nacos 中管理
- **数据库配置**: 每个服务使用独立的数据库
- **Redis 配置**: 不同服务使用不同的数据库编号

### 日志配置

项目集成了分布式链路追踪，日志中会自动包含 traceId 和 spanId，便于问题排查。

## 部署说明

### 生产环境部署

1. **环境准备**
   - 服务器配置建议: 4核8G内存以上
   - 安装 Docker 和 Docker Compose
   - 配置防火墙开放相应端口

2. **配置修改**
   - 修改数据库连接配置
   - 修改 Redis 密码
   - 配置 Nacos 认证
   - 设置 JWT 密钥

3. **部署步骤**
```bash
# 1. 上传项目文件
scp -r XingChen-admin/ user@server:/opt/

# 2. 构建镜像
mvn clean package -DskipTests
docker-compose build

# 3. 启动服务
docker-compose up -d

# 4. 检查服务状态
docker-compose ps
docker-compose logs -f
```

### 扩容说明

支持水平扩容，可以通过以下方式增加服务实例：

```bash
# 扩展用户服务到3个实例
docker-compose up -d --scale admin-user=3
```

## 常见问题

### 1. 服务启动失败
- 检查端口是否被占用
- 确认 MySQL 和 Redis 是否正常启动
- 查看服务日志排查具体错误

### 2. 服务注册失败
- 检查 Nacos 是否正常运行
- 确认网络连接是否正常
- 检查 Nacos 配置是否正确

### 3. 网关路由不通
- 检查服务是否已注册到 Nacos
- 确认网关路由配置是否正确
- 查看网关服务日志

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证，详情请参见 [LICENSE](LICENSE) 文件。

## 联系我们

如有问题或建议，请提交 Issue 或通过以下方式联系：

- 邮箱: admin@example.com
- 微信: xingchen-admin

---

⭐ 如果这个项目对您有帮助，请给我们一个 Star！
