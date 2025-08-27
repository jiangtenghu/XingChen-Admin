# XingChen-Admin 微服务文档中心

## 📋 文档导航

欢迎来到 XingChen-Admin 微服务系统的文档中心。这里汇集了整个系统的架构设计、服务文档、部署指南等完整资料。

## 🏗️ 架构文档

### 📊 整体架构
- [**系统架构概览**](./ARCHITECTURE_OVERVIEW.md) - 完整的系统架构设计和技术栈介绍
- [**完善组织架构设计**](../PERFECT_ORGANIZATION_DESIGN.md) - 用户组织架构、租户体系、角色分配设计方案
- [**实施指南**](../IMPLEMENTATION_GUIDE.md) - 具体的实现建议和核心代码示例

## 🔧 服务文档

### 🚪 admin-gateway (API网关)
- [**服务概述**](../admin-gateway/doc/README.md) - 网关服务的核心功能和架构设计
- [**安全架构设计**](../admin-gateway/doc/GATEWAY_SECURITY_DESIGN.md) - 网关层的安全防护体系和实现方案

### 🔐 admin-auth (认证服务)
- [**服务概述**](../admin-auth/doc/README.md) - 认证服务的功能特性和API接口
- [**认证架构设计**](../admin-auth/doc/AUTHENTICATION_DESIGN.md) - 多因子认证、JWT管理、会话安全设计

### 👥 admin-user (用户服务)
- [**服务概述**](../admin-user/doc/README.md) - 用户管理服务的功能模块和数据模型
- [**租户组织设计**](../admin-user/doc/TENANT_ORGANIZATION_DESIGN.md) - 多租户体系和组织架构的详细设计

### 🛠️ admin-system (系统服务)
- [**服务概述**](../admin-system/doc/README.md) - 系统管理服务的权限控制和配置管理
- [**权限角色设计**](../admin-system/doc/PERMISSION_ROLE_DESIGN.md) - RBAC+权限模型和角色管理系统设计

## 📚 专题文档

### 🔐 安全体系
- [网关安全架构](../admin-gateway/doc/GATEWAY_SECURITY_DESIGN.md) - 多层安全防护、DDoS防护、威胁检测
- [认证安全设计](../admin-auth/doc/AUTHENTICATION_DESIGN.md) - JWT安全、多因子认证、会话管理
- [权限控制系统](../admin-system/doc/PERMISSION_ROLE_DESIGN.md) - 权限计算引擎、动态权限、数据权限

### 🏢 多租户架构
- [租户体系设计](../admin-user/doc/TENANT_ORGANIZATION_DESIGN.md) - 租户分类、数据隔离、配额管理
- [组织架构管理](../admin-user/doc/TENANT_ORGANIZATION_DESIGN.md) - 多维度组织、动态管理、智能推荐

### 📊 数据架构
- [数据库设计](./DATABASE_DESIGN.md) - 数据模型、分库分表、索引优化
- [缓存架构](./CACHE_ARCHITECTURE.md) - 多级缓存、缓存策略、性能优化

## 🚀 部署运维

### 🐳 容器化部署
- [Docker部署指南](./DOCKER_DEPLOYMENT.md) - Docker镜像构建和容器化部署
- [Kubernetes部署](./K8S_DEPLOYMENT.md) - K8s集群部署和服务编排
- [Docker Compose](./docker-compose.yml) - 本地开发环境快速搭建

### 📊 监控运维
- [监控体系](./MONITORING_GUIDE.md) - Prometheus + Grafana监控方案
- [日志管理](./LOGGING_GUIDE.md) - ELK Stack日志收集和分析
- [告警配置](./ALERT_CONFIGURATION.md) - 告警规则和通知渠道配置

## 🔄 开发指南

### 📝 开发规范
- [代码规范](./CODING_STANDARDS.md) - 代码风格、命名规范、最佳实践
- [API设计规范](./API_DESIGN_GUIDE.md) - RESTful API设计原则和规范
- [数据库规范](./DATABASE_STANDARDS.md) - 表结构设计、索引规范、SQL规范

### 🧪 测试指南
- [单元测试](./UNIT_TEST_GUIDE.md) - 单元测试编写规范和工具使用
- [集成测试](./INTEGRATION_TEST_GUIDE.md) - 集成测试策略和自动化测试
- [性能测试](./PERFORMANCE_TEST_GUIDE.md) - 性能测试方案和压测工具

## 📖 用户文档

### 👤 用户手册
- [系统使用指南](./USER_MANUAL.md) - 功能操作说明和使用技巧
- [管理员指南](./ADMIN_GUIDE.md) - 系统管理和配置操作指南
- [常见问题](./FAQ.md) - 常见问题解答和故障排除

### 🔌 API文档
- [API接口文档](http://localhost:8080/doc.html) - Swagger在线API文档
- [接口调用示例](./API_EXAMPLES.md) - 常用API的调用示例和代码片段

## 📈 版本历史

### 🔄 更新日志
- [版本更新日志](./CHANGELOG.md) - 详细的版本更新记录和功能变更
- [升级指南](./UPGRADE_GUIDE.md) - 版本升级步骤和注意事项
- [迁移指南](./MIGRATION_GUIDE.md) - 数据迁移和系统迁移指南

## 🤝 贡献指南

### 👥 参与贡献
- [贡献指南](./CONTRIBUTING.md) - 如何参与项目开发和贡献代码
- [Issue模板](./ISSUE_TEMPLATE.md) - 问题反馈和需求提交模板
- [PR模板](./PULL_REQUEST_TEMPLATE.md) - 代码提交和审核流程

## 📞 技术支持

### 🆘 获取帮助
- **技术交流群**: [加入技术交流群]
- **问题反馈**: [GitHub Issues](https://github.com/your-org/xingchen-admin/issues)
- **邮件支持**: support@example.com
- **在线文档**: [项目官网](https://xingchen-admin.example.com)

## 📋 文档结构说明

```
📁 XingChen-admin/doc/
├── 📄 README.md                    # 文档导航首页
├── 📄 ARCHITECTURE_OVERVIEW.md     # 系统架构概览
├── 📄 DATABASE_DESIGN.md           # 数据库设计文档
├── 📄 DEPLOYMENT_GUIDE.md          # 部署指南
├── 📄 MONITORING_GUIDE.md          # 监控运维指南
└── 📄 API_DOCUMENTATION.md         # API接口文档

📁 admin-auth/doc/
├── 📄 README.md                    # 认证服务概述
└── 📄 AUTHENTICATION_DESIGN.md     # 认证架构设计

📁 admin-user/doc/
├── 📄 README.md                    # 用户服务概述
└── 📄 TENANT_ORGANIZATION_DESIGN.md # 租户组织设计

📁 admin-system/doc/
├── 📄 README.md                    # 系统服务概述
└── 📄 PERMISSION_ROLE_DESIGN.md    # 权限角色设计

📁 admin-gateway/doc/
├── 📄 README.md                    # 网关服务概述
└── 📄 GATEWAY_SECURITY_DESIGN.md   # 网关安全设计
```

## 🏷️ 文档标签

- 🏗️ **架构设计** - 系统架构和技术选型相关文档
- 🔐 **安全体系** - 认证、授权、安全防护相关文档  
- 🏢 **多租户** - 租户管理和数据隔离相关文档
- 📊 **数据架构** - 数据库设计和数据管理相关文档
- 🚀 **部署运维** - 部署、监控、运维相关文档
- 🔄 **开发指南** - 开发规范和最佳实践文档
- 📖 **用户文档** - 用户使用和操作指南文档

## ⚡ 快速开始

1. **了解系统架构**: 从 [系统架构概览](./ARCHITECTURE_OVERVIEW.md) 开始
2. **部署开发环境**: 参考 [Docker部署指南](./DOCKER_DEPLOYMENT.md)
3. **查看API文档**: 访问 [Swagger文档](http://localhost:8080/doc.html)
4. **开始开发**: 阅读 [开发规范](./CODING_STANDARDS.md)

---

📝 **文档维护**: 本文档由开发团队维护，如有问题请及时反馈。  
🔄 **最后更新**: 2024年1月  
📧 **联系我们**: tech-team@example.com
