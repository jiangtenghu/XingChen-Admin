# XingChen-Vue 前端管理系统

[![Vue](https://img.shields.io/badge/Vue-3.5+-brightgreen.svg)](https://vuejs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.0+-blue.svg)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-6.0+-646CFF.svg)](https://vitejs.dev/)
[![Ant Design Vue](https://img.shields.io/badge/Ant%20Design%20Vue-4.0+-1890FF.svg)](https://antdv.com/)
[![pnpm](https://img.shields.io/badge/pnpm-9.0+-F69220.svg)](https://pnpm.io/)

基于 Vue 3 + TypeScript + Vite + Ant Design Vue 构建的现代化企业级管理后台前端应用。

## ✨ 特性

- 🚀 **最新技术栈**: Vue 3 + TypeScript + Vite 6 + Ant Design Vue 4
- 📦 **Monorepo**: 基于 pnpm workspaces 的多包管理
- 🎨 **主题定制**: 支持暗色/亮色主题切换
- 🌍 **国际化**: 内置中英文支持，可扩展多语言
- 🔐 **权限控制**: 完整的RBAC权限管理，支持路由级别和按钮级别权限
- 📱 **响应式**: 移动端适配，支持多种屏幕尺寸
- 🛠️ **开发体验**: 完整的TypeScript支持，ESLint + Prettier代码规范
- ⚡ **性能优化**: 路由懒加载、组件按需引入、构建优化

## 🏗️ 项目结构

```
XingChen-Vue/
├── apps/                          # 应用目录
│   └── admin/                     # 管理后台应用
│       ├── src/
│       │   ├── api/              # API接口
│       │   ├── components/        # 业务组件
│       │   ├── layouts/          # 布局组件
│       │   ├── router/           # 路由配置
│       │   ├── stores/           # 状态管理
│       │   ├── utils/            # 工具函数
│       │   └── views/            # 页面组件
│       ├── index.html            # 入口HTML
│       ├── package.json          # 应用依赖
│       └── vite.config.mts       # Vite配置
├── packages/                      # 共享包目录
│   ├── @core/                    # 核心包
│   │   ├── base/                 # 基础工具
│   │   ├── composables/          # 组合式函数
│   │   ├── preferences/          # 偏好设置
│   │   └── ui-kit/              # UI组件库
│   ├── constants/                # 常量定义
│   ├── effects/                  # 特效组件
│   ├── icons/                    # 图标库
│   ├── locales/                  # 国际化
│   ├── stores/                   # 全局状态
│   ├── styles/                   # 样式库
│   ├── types/                    # 类型定义
│   └── utils/                    # 工具函数
├── internal/                      # 内部工具
│   ├── lint-configs/             # 代码规范配置
│   ├── node-utils/               # Node工具
│   ├── tailwind-config/          # Tailwind配置
│   ├── tsconfig/                 # TypeScript配置
│   └── vite-config/              # Vite配置
└── scripts/                       # 构建脚本
    ├── turbo-run/                # Turbo构建工具
    └── vsh/                      # 脚本工具
```

## 🚀 快速开始

### 环境要求

- **Node.js**: >= 18.0.0
- **pnpm**: >= 9.0.0 (推荐使用最新版本)

### 安装依赖

1. **启用 Corepack** (如果尚未启用)
   ```bash
   corepack enable
   ```

2. **安装项目依赖**
   ```bash
   pnpm install
   ```

### 开发

1. **启动开发服务器**
   ```bash
   pnpm dev
   ```
   
2. **访问应用**
   ```
   http://localhost:5666
   ```

### 构建

1. **生产环境构建**
   ```bash
   pnpm build
   ```

2. **预览构建结果**
   ```bash
   pnpm preview
   ```

## 📖 开发指南

### 命令说明

| 命令 | 说明 |
|------|------|
| `pnpm dev` | 启动开发服务器 |
| `pnpm build` | 构建生产版本 |
| `pnpm preview` | 预览构建结果 |
| `pnpm lint` | 运行 ESLint 检查 |
| `pnpm lint:fix` | 自动修复 ESLint 错误 |
| `pnpm type-check` | TypeScript 类型检查 |
| `pnpm clean` | 清理构建文件和依赖 |

### 环境变量

详细的环境变量配置请参考：[ENV_VARIABLES.md](./ENV_VARIABLES.md)

常用环境变量：

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `VITE_APP_TITLE` | `XingChen Admin` | 应用标题 |
| `VITE_BASE_URL` | `/` | 应用基础路径 |
| `VITE_API_URL` | `http://localhost:8080/api` | API服务地址 |

### 代码规范

项目使用以下工具保证代码质量：

- **ESLint**: JavaScript/TypeScript 代码检查
- **Prettier**: 代码格式化
- **Stylelint**: CSS/SCSS 样式检查
- **TypeScript**: 类型检查

### IDE 配置

#### VS Code

推荐安装以下扩展：

- Vue Language Features (Volar)
- TypeScript Vue Plugin (Volar)
- ESLint
- Prettier - Code formatter
- Tailwind CSS IntelliSense

#### WebStorm

1. **ESLint 配置**: 启用 ESLint 并配置自动修复
2. **格式化快捷键**: `Ctrl+Alt+L` 绑定到 ESLint fix
3. **TypeScript 支持**: 启用 TypeScript 服务

## 🔧 技术栈

### 核心技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5+ | 渐进式 JavaScript 框架 |
| TypeScript | 5.0+ | JavaScript 的超集 |
| Vite | 6.0+ | 下一代前端构建工具 |
| Vue Router | 4.0+ | Vue.js 官方路由器 |
| Pinia | 2.0+ | Vue 状态管理库 |

### UI 组件

| 组件库 | 版本 | 说明 |
|--------|------|------|
| Ant Design Vue | 4.0+ | 企业级 UI 设计语言 |
| @vben/ui-kit | 自研 | 基于 shadcn/ui 的组件库 |
| Iconify | 最新 | 统一图标框架 |

### 开发工具

| 工具 | 版本 | 说明 |
|------|------|------|
| pnpm | 9.0+ | 快速、节省磁盘空间的包管理器 |
| Turbo | 最新 | 高性能构建系统 |
| ESLint | 最新 | JavaScript 代码检查工具 |
| Prettier | 最新 | 代码格式化工具 |

## 🎨 主题定制

### 主题配置

主题配置位于 `packages/@core/preferences/src/config.ts`：

```typescript
export const defaultPreferences = {
  // 主题配置
  theme: {
    mode: 'light', // 'light' | 'dark' | 'auto'
    colorPrimary: '#1677ff',
    radius: 0.5,
  },
  // 应用配置
  app: {
    name: 'XingChen Admin',
    logo: '/logo.png',
  },
};
```

### 自定义主题

1. **修改主色调**
   ```typescript
   theme: {
     colorPrimary: '#00b96b', // 绿色主题
   }
   ```

2. **自定义组件样式**
   ```css
   /* 在 packages/styles/src/custom.css 中添加 */
   .custom-button {
     @apply bg-primary text-white rounded-lg;
   }
   ```

## 🌍 国际化

### 支持的语言

- 🇨🇳 简体中文 (zh-CN)
- 🇺🇸 英语 (en-US)

### 添加新语言

1. **创建语言文件**
   ```
   packages/locales/src/langs/ja-JP/
   ├── common.json
   ├── authentication.json
   └── ...
   ```

2. **注册语言**
   ```typescript
   // packages/locales/src/index.ts
   import jaJP from './langs/ja-JP';
   
   export const messages = {
     'zh-CN': zhCN,
     'en-US': enUS,
     'ja-JP': jaJP, // 新增日语
   };
   ```

## 🔒 权限控制

### 路由权限

```typescript
// src/router/routes.ts
{
  path: '/system',
  meta: {
    requiresAuth: true,
    permissions: ['system:view'],
  },
}
```

### 按钮权限

```vue
<template>
  <a-button v-if="hasPermission('user:create')">
    新增用户
  </a-button>
</template>

<script setup>
import { usePermission } from '@/hooks/usePermission';

const { hasPermission } = usePermission();
</script>
```

## 📦 部署

### 构建配置

```bash
# 开发环境构建
pnpm build:dev

# 生产环境构建
pnpm build:prod

# 构建并分析包大小
pnpm build --analyze
```

### Docker 部署

```dockerfile
# Dockerfile
FROM node:18-alpine AS builder

WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production

COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### Nginx 配置

```nginx
# nginx.conf
server {
    listen 80;
    server_name localhost;
    
    location / {
        root /usr/share/nginx/html;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://backend:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 🔧 常见问题

### 开发环境问题

**Q: 启动时提示端口被占用**
```bash
# 查看端口占用
lsof -i :5666
# 杀死进程
kill -9 <PID>
```

**Q: 依赖安装失败**
```bash
# 清理缓存重新安装
pnpm store prune
rm -rf node_modules
pnpm install
```

**Q: TypeScript 类型错误**
```bash
# 重新生成类型定义
pnpm type-check
```

### 构建问题

**Q: 构建内存不足**
```bash
# 增加 Node.js 内存限制
export NODE_OPTIONS="--max-old-space-size=4096"
pnpm build
```

**Q: 构建速度慢**
```bash
# 使用 Turbo 加速构建
pnpm turbo build
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 打开 Pull Request

### 提交规范

使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
feat: 新功能
fix: 修复问题
docs: 文档更新
style: 代码格式化
refactor: 代码重构
test: 测试相关
chore: 构建过程或辅助工具的变动
```

## 📄 许可证

本项目基于 [MIT License](../LICENSE) 开源协议。

## 🙏 致谢

- [Vue.js](https://vuejs.org/) - 渐进式 JavaScript 框架
- [Ant Design Vue](https://antdv.com/) - 企业级 UI 组件库
- [Vite](https://vitejs.dev/) - 下一代前端构建工具
- [Vben Admin](https://doc.vben.pro/) - 开源管理后台模板

---

如有问题，请提交 [Issue](https://github.com/jiangtenghu/XingChen-Admin/issues) 或参与 [讨论](https://github.com/jiangtenghu/XingChen-Admin/discussions)。