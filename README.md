# XingChen Admin 🌟

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.0+-brightgreen.svg)](https://vuejs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.0+-blue.svg)](https://www.typescriptlang.org/)

**XingChen Admin** 是一个现代化的企业级管理后台系统，采用微服务架构设计，支持多租户、权限管理、组织架构等核心功能。

## 🚀 项目特色

- **🏗️ 微服务架构**: 基于Spring Boot微服务框架，支持独立部署和横向扩展
- **🔐 统一认证**: 网关统一认证，支持JWT Token和OAuth2.1标准
- **👥 多租户支持**: 完整的多租户数据隔离和管理体系
- **🎨 现代化前端**: 基于Vue3 + TypeScript + Ant Design Vue的响应式界面
- **⚡ 高性能**: 优化的数据库设计和缓存策略
- **🔧 开箱即用**: 完整的RBAC权限控制和基础功能模块


## 📚 架构设计

### 🏛️ 系统架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        前端层                                 │
├─────────────────────────────────────────────────────────────┤
│  XingChen-Vue (Vue3 + TypeScript + Ant Design Vue)          │
│  ├── 用户界面层 (UI Layer)                                    │
│  ├── 状态管理层 (Pinia Store)                                │
│  ├── 路由层 (Vue Router)                                     │
│  └── 服务层 (API Services)                                   │
└─────────────────────────────────────────────────────────────┘
                              │ HTTP/HTTPS
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                        网关层                                 │
├─────────────────────────────────────────────────────────────┤
│  admin-gateway (Spring Cloud Gateway)                      │
│  ├── 统一认证过滤器                                           │
│  ├── API路由管理                                             │
│  ├── 限流熔断                                               │
│  └── 监控日志                                               │
└─────────────────────────────────────────────────────────────┘
                              │ 负载均衡
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      微服务层                                 │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐   │
│  │admin-auth   │  │admin-identity│  │   admin-system      │   │
│  │认证服务      │  │身份管理服务   │  │   系统管理服务       │   │
│  │- JWT Token  │  │- 用户管理    │  │   - 组织架构        │   │
│  │- OAuth2.1   │  │- 角色权限    │  │   - 菜单管理        │   │
│  │- 登录认证    │  │- 多租户     │  │   - 字典管理        │   │
│  └─────────────┘  └─────────────┘  └─────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              │ 数据访问
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       数据层                                  │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐   │
│  │admin_auth DB │  │admin_user DB │  │  admin_system DB │   │
│  │认证数据库     │  │用户数据库     │  │  系统数据库       │   │
│  └──────────────┘  └──────────────┘  └──────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### 🔧 技术架构

#### 后端技术栈

| 分类 | 技术栈 | 版本 | 说明 |
|------|--------|------|------|
| **核心框架** | Spring Boot | 3.0+ | 微服务基础框架 |
| **数据访问** | MyBatis Plus | 3.5+ | ORM框架和数据访问 |
| **数据库** | MySQL | 8.0+ | 主数据库 |
| **缓存** | Redis | 7.0+ | 分布式缓存 |
| **认证** | Spring Security | 6.0+ | 安全认证框架 |
| **网关** | Spring Cloud Gateway | 4.0+ | API网关 |
| **文档** | Swagger/OpenAPI | 3.0+ | API文档生成 |
| **监控** | Spring Boot Actuator | 3.0+ | 应用监控 |
| **容器化** | Docker | 最新 | 容器化部署 |

#### 前端技术栈

| 分类 | 技术栈 | 版本 | 说明 |
|------|--------|------|------|
| **核心框架** | Vue.js | 3.5+ | 渐进式前端框架 |
| **编程语言** | TypeScript | 5.0+ | 类型安全的JavaScript |
| **UI框架** | Ant Design Vue | 4.0+ | 企业级UI组件库 |
| **构建工具** | Vite | 6.0+ | 下一代前端构建工具 |
| **状态管理** | Pinia | 2.0+ | 现代化状态管理 |
| **路由** | Vue Router | 4.0+ | 官方路由管理器 |
| **HTTP客户端** | Axios | 1.0+ | Promise-based HTTP库 |
| **包管理器** | pnpm | 9.0+ | 高效的包管理器 |
| **代码规范** | ESLint + Prettier | 最新 | 代码质量和格式化 |

## 🗂️ 项目结构

```
XingChen-admin/
├── XingChen-admin/          # 后端Java微服务
│   ├── admin-gateway/       # 网关服务
│   ├── admin-auth/          # 认证服务
│   ├── admin-identity/      # 身份管理服务
│   ├── admin-system/        # 系统管理服务
│   ├── admin-common/        # 公共模块
│   └── admin-common-web/    # Web公共模块
├── XingChen-Vue/           # 前端Vue应用
│   ├── apps/admin/         # 管理后台应用
│   ├── packages/           # 共享包
│   ├── internal/           # 内部工具包
│   └── scripts/            # 构建脚本
├── sql/                    # 数据库脚本
├── docs/                   # 项目文档
└── docker-compose.yml      # Docker编排文件
```

## 🎯 核心功能

### 👤 用户权限管理
- **用户管理**: 用户CRUD、状态管理、密码重置、角色分配
- **角色管理**: 角色权限配置、菜单权限、数据权限
- **权限管理**: 细粒度权限控制、动态权限验证
- **组织架构**: 多级部门管理、岗位管理

### 🏢 多租户系统
- **租户管理**: 租户注册、配置、状态管理
- **数据隔离**: 基于租户ID的数据隔离
- **资源配额**: 租户资源使用限制
- **自动初始化**: 新租户自动创建默认数据

### 🔐 认证授权
- **JWT认证**: 无状态Token认证
- **OAuth2.1**: 标准OAuth协议支持
- **统一认证**: 网关层统一认证处理
- **会话管理**: 登录状态管理、强制下线

### 📊 系统管理
- **菜单管理**: 动态菜单配置、权限关联
- **字典管理**: 系统字典维护、多级分类
- **日志管理**: 操作日志、登录日志、系统日志
- **监控管理**: 系统监控、性能指标

## 🚀 快速开始

### 环境要求

- **Java**: 17+
- **Node.js**: 18+
- **MySQL**: 8.0+
- **Redis**: 7.0+
- **pnpm**: 9.0+

### 后端启动

1. **克隆项目**
   ```bash
   git clone https://github.com/jiangtenghu/XingChen-Admin.git
   cd XingChen-Admin
   ```

2. **数据库初始化**
   ```bash
   # 执行SQL脚本
   mysql -u root -p < sql/init.sql
   ```

3. **启动服务**
   ```bash
   # 启动网关服务
   cd XingChen-admin/admin-gateway
   ./mvnw spring-boot:run
   
   # 启动认证服务
   cd ../admin-auth
   ./mvnw spring-boot:run
   
   # 启动身份管理服务
   cd ../admin-identity
   ./mvnw spring-boot:run
   
   # 启动系统管理服务
   cd ../admin-system
   ./mvnw spring-boot:run
   ```

### 前端启动

1. **安装依赖**
   ```bash
   cd XingChen-Vue
   pnpm install
   ```

2. **启动开发服务器**
   ```bash
   pnpm dev
   ```

3. **访问应用**
   ```
   http://localhost:5666
   ```

### Docker部署

```bash
# 一键启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps
```

## 📖 开发指南

### 后端开发

- [API设计规范](./docs/backend/api-design.md)
- [数据库设计](./docs/backend/database-design.md)
- [微服务开发指南](./docs/backend/microservice-guide.md)

### 前端开发

- [组件开发规范](./docs/frontend/component-guide.md)
- [状态管理指南](./docs/frontend/state-management.md)
- [路由配置说明](./docs/frontend/routing-guide.md)

## 🔧 配置说明

### 环境变量配置

查看 [环境变量配置指南](./XingChen-Vue/ENV_VARIABLES.md) 了解详细的前端环境变量配置。

### 数据库配置

默认数据库配置位于各服务的 `application.yml` 文件中，支持多环境配置。

## 📸 系统截图

### 登录界面
![登录界面](./docs/images/admin_login_page.png)

### 控制台
![控制台](./docs/images/admin_dashboard.png)

### 用户管理
![用户管理](./docs/images/admin_user_management.png)

### 角色管理
![角色管理](./docs/images/admin_role_management.png)

## 🤝 贡献指南

我们欢迎所有形式的贡献，包括但不限于：

- 🐛 Bug报告
- 💡 功能建议
- 📖 文档完善
- 💻 代码贡献

请查看 [贡献指南](./CONTRIBUTING.md) 了解详细信息。

## 📄 开源协议

本项目基于 [MIT License](./LICENSE) 开源协议。

## 💬 交流群

- **QQ群**: [待建立]
- **微信群**: [待建立]
- **Issues**: [GitHub Issues](https://github.com/jiangtenghu/XingChen-Admin/issues)

## 🙏 致谢

感谢以下开源项目的支持：

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [Ant Design Vue](https://antdv.com/)
- [Vben Admin](https://doc.vben.pro/)

---

⭐ **如果这个项目对你有帮助，请给我们一个Star！** ⭐
