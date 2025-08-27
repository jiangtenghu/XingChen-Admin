# XingChen Admin 环境变量配置指南

## 概述

XingChen Admin 使用 Vite 的环境变量系统来管理不同环境下的配置。项目支持以下环境变量文件：

- `.env` - 所有环境下都会加载
- `.env.local` - 本地环境，被 git 忽略
- `.env.development` - 开发环境
- `.env.development.local` - 本地开发环境，被 git 忽略
- `.env.production` - 生产环境
- `.env.production.local` - 本地生产环境，被 git 忽略

## 当前项目状态

目前项目中没有环境变量文件，所有配置都使用代码中的默认值。环境变量的默认值定义在：
- `internal/vite-config/src/utils/env.ts` - Vite 相关配置的默认值
- `packages/@core/preferences/src/config.ts` - 应用偏好设置的默认值

## 支持的环境变量

### 应用基础配置

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `VITE_APP_TITLE` | `XingChen Admin` | 应用标题，显示在浏览器标签页和登录页面 |
| `VITE_APP_NAMESPACE` | - | 应用命名空间，用于区分不同项目的本地存储 |
| `VITE_APP_VERSION` | - | 应用版本号 |

### 路由和权限配置

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `VITE_ROUTER_ACCESS_MODE` | - | 路由访问模式：`frontend`(前端控制) 或 `backend`(后端控制) |

### 开发服务器配置

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `VITE_PORT` | `5173` | 开发服务器端口 |
| `VITE_BASE` | `/` | 应用基础路径 |

### 功能开关

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `VITE_PWA` | `false` | 是否启用 PWA |
| `VITE_DEVTOOLS` | `false` | 是否启用开发者工具 |
| `VITE_INJECT_APP_LOADING` | `false` | 是否注入应用加载页面 |
| `VITE_NITRO_MOCK` | `false` | 是否启用 Nitro Mock 服务 |
| `VITE_VISUALIZER` | `false` | 是否启用打包分析器 |
| `VITE_ARCHIVER` | `false` | 是否启用文件压缩 |
| `VITE_COMPRESS` | - | 压缩类型：`gzip,brotli` |

## 如何使用环境变量

### 1. 创建环境变量文件

在项目根目录创建对应的 `.env` 文件：

```bash
# 开发环境
touch .env.development

# 本地开发环境（不会被提交到 git）
touch .env.local
```

### 2. 配置环境变量

在 `.env.local` 文件中添加配置：

```bash
# 自定义应用标题
VITE_APP_TITLE=我的 XingChen Admin

# 自定义端口
VITE_PORT=3000

# 启用开发者工具
VITE_DEVTOOLS=true

# 路由访问模式
VITE_ROUTER_ACCESS_MODE=frontend
```

### 3. 在代码中使用

环境变量可以通过 `import.meta.env` 访问：

```typescript
// 在 TypeScript 文件中
const appTitle = import.meta.env.VITE_APP_TITLE;
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;
```

## 示例配置文件

### .env.development
```bash
# 开发环境配置
VITE_APP_TITLE=XingChen Admin (开发)
VITE_PORT=5666
VITE_DEVTOOLS=true
VITE_INJECT_APP_LOADING=true
VITE_ROUTER_ACCESS_MODE=frontend
```

### .env.production
```bash
# 生产环境配置
VITE_APP_TITLE=XingChen Admin
VITE_BASE=/
VITE_COMPRESS=gzip,brotli
VITE_PWA=true
VITE_ROUTER_ACCESS_MODE=backend
```

## 注意事项

1. 以 `VITE_` 开头的变量会被 Vite 暴露给客户端代码
2. 环境变量文件的优先级：`.env.local` > `.env.{mode}.local` > `.env.{mode}` > `.env`
3. 更改配置后可能需要清空浏览器缓存才能生效
4. 敏感信息不应该放在环境变量中，因为它们会被打包到客户端代码中

## 当前配置状态

项目当前使用的配置值：
- 应用标题：`XingChen Admin` (来自代码默认值)
- 路由访问模式：从 `import.meta.env.VITE_ROUTER_ACCESS_MODE` 读取，如果未设置则使用默认值
- 其他配置：主要来自 `packages/@core/preferences/src/config.ts` 中的默认配置
