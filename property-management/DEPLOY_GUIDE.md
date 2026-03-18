# 智慧物业管理系统 - 部署指南

## 项目结构

```
property-management/
├── backend/                 # 后端 Spring Boot 项目
│   ├── src/
│   │   └── main/
│   │       ├── java/        # Java 源代码
│   │       └── resources/   # 配置文件
│   │           ├── db/      # 数据库脚本
│   │           └── mapper/  # MyBatis 映射文件
│   └── pom.xml
│
├── frontend/                # 前端项目
│   ├── admin/              # 管理端 (Vue 3 + TypeScript)
│   └── owner/              # 业主端 (React + TypeScript)
│
├── README.md               # 项目说明
└── API_DOCUMENTATION.md    # 接口文档
```

---

## 一、环境要求

### 后端环境
- JDK 1.8 或更高版本
- Maven 3.6+
- MySQL 5.7+

### 前端环境
- Node.js 16+
- npm 8+

---

## 二、数据库部署

### 2.1 创建数据库

```sql
CREATE DATABASE property_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2.2 导入表结构

找到 `backend/src/main/resources/db/schema.sql` 文件，在 MySQL 中执行其中的 SQL 语句。

---

## 三、后端部署

### 3.1 修改配置文件

编辑 `backend/src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/property_management?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root          # 改为你的数据库用户名
    password: your_password  # 改为你的数据库密码
```

### 3.2 构建后端项目

```bash
cd backend
mvn clean package -DskipTests
```

构建成功后，会在 `backend/target/` 目录下生成 `property-management-1.0.0.jar` 文件。

### 3.3 启动后端服务

```bash
java -jar backend/target/property-management-1.0.0.jar
```

或者在开发环境运行：

```bash
cd backend
mvn spring-boot:run
```

后端服务默认端口：**8080**

---

## 四、前端部署

### 4.1 安装依赖

```bash
# 安装管理端依赖
cd frontend/admin
npm install

# 安装业主端依赖
cd ../owner
npm install
```

### 4.2 修改 API 配置

#### 管理端 (frontend/admin/src/utils/request.ts)
```typescript
const request = axios.create({
  baseURL: 'http://your-server-ip:8080/api',  // 改为后端服务地址
  timeout: 30000
})
```

#### 业主端 (frontend/owner/src/utils/request.ts)
```typescript
const request = axios.create({
  baseURL: 'http://your-server-ip:8080/api',  // 改为后端服务地址
  timeout: 30000
})
```

### 4.3 构建生产版本

```bash
# 构建管理端
cd frontend/admin
npm run build

# 构建业主端
cd ../owner
npm run build
```

构建成功后，会在各自目录下生成 `dist` 文件夹。

---

## 五、Nginx 配置示例

### 5.1 安装 Nginx

下载 Nginx 并安装，编辑 `nginx.conf`：

```nginx
worker_processes 1;

events {
    worker_connections 1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;
    keepalive_timeout  65;

    # 管理端
    server {
        listen       80;
        server_name  admin.yourdomain.com;  # 管理端域名

        location / {
            root   D:/property-management/frontend/admin/dist;  # 管理端构建目录
            index  index.html;
            try_files $uri $uri/ /index.html;
        }
    }

    # 业主端
    server {
        listen       81;
        server_name  owner.yourdomain.com;  # 业主端域名

        location / {
            root   D:/property-management/frontend/owner/dist;  # 业主端构建目录
            index  index.html;
            try_files $uri $uri/ /index.html;
        }
    }

    # 后端 API 代理
    server {
        listen       8080;
        server_name  api.yourdomain.com;

        location / {
            proxy_pass http://localhost:8080;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
        }
    }
}
```

### 5.2 启动 Nginx

```bash
nginx -c /path/to/nginx.conf
nginx -s reload  # 重载配置
```

---

## 六、访问地址

部署完成后，通过以下地址访问：

| 应用 | 地址 |
|------|------|
| 管理端 | http://your-server-ip:80 |
| 业主端 | http://your-server-ip:81 |
| 后端 API | http://your-server-ip:8080/api |

---

## 七、测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 业主 | 13800138000 | 123456 |

---

## 八、常见问题

### 8.1 前端无法访问后端 API
- 检查后端服务是否启动
- 检查 Nginx 代理配置是否正确
- 检查防火墙是否放行端口

### 8.2 数据库连接失败
- 检查数据库用户名和密码是否正确
- 检查数据库是否已创建
- 检查 MySQL 服务是否启动

### 8.3 前端页面空白
- 确认已经执行 `npm run build` 构建
- 检查 `baseURL` 配置是否正确
- 查看浏览器控制台错误信息

---

## 九、目录结构说明

部署时需要的文件：

```
D:/property-management/
├── backend/
│   ├── target/
│   │   └── property-management-1.0.0.jar  # 后端 jar 包
│   └── src/
│       └── main/
│           └── resources/
│               ├── db/
│               │   └── schema.sql           # 数据库脚本
│               └── application.yml           # 配置文件
│
├── frontend/
│   ├── admin/
│   │   └── dist/                            # 管理端构建文件
│   └── owner/
│       └── dist/                            # 业主端构建文件
│
├── README.md
└── API_DOCUMENTATION.md
```

---

**部署完成！** 如有问题，请查看日志或联系技术支持。
