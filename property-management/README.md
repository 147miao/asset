# 物业管理系统

## 项目结构

```
property-management/
├── backend/              # 后端项目
├── frontend/
│   ├── admin/           # 管理端
│   └── owner/           # 业主端
├── start-backend.bat     # 启动后端
├── start-admin.bat      # 启动管理端
└── start-owner.bat      # 启动业主端
```

## 环境要求

- JDK 17
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+
- Redis（可选）

## 数据库配置

1. 创建数据库
```sql
CREATE DATABASE property_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本
```bash
mysql -u root -p1234 property_management < backend/src/main/resources/db/schema.sql
```

## 配置说明

### 后端配置
- 数据库密码：1234
- 服务端口：8080
- 配置文件：`backend/src/main/resources/application.yml`

### 前端配置
- 管理端端口：3000
- 业主端端口：3001
- API代理：http://localhost:8080

## 启动方式

### 方式一：使用启动脚本（推荐）

1. **启动后端**
   - 双击 `start-backend.bat`
   - 等待Maven依赖下载和编译完成
   - 看到启动成功信息即可

2. **启动管理端**
   - 双击 `start-admin.bat`
   - 等待npm依赖安装完成
   - 浏览器自动打开 http://localhost:3000

3. **启动业主端**
   - 双击 `start-owner.bat`
   - 等待npm依赖安装完成
   - 浏览器自动打开 http://localhost:3001

### 方式二：使用IDEA

1. **启动后端**
   - 用IDEA打开 `backend` 目录
   - 等待Maven同步完成
   - 运行 `PropertyManagementApplication.java`

2. **启动前端**
   - 用IDEA打开 `frontend/admin` 或 `frontend/owner` 目录
   - 运行 npm 命令

### 方式三：命令行启动

```bash
# 后端
cd backend
mvn clean install -DskipTests
mvn spring-boot:run

# 管理端（新终端）
cd frontend/admin
npm install
npm run dev

# 业主端（新终端）
cd frontend/owner
npm install
npm run dev
```

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | 13800000000 | 123456 |
| 业主 | 13800000001 | 123456 |
| 租户 | 13800000002 | 123456 |

## 功能模块

### 后端功能
- 项目管理：新增、编辑、删除物业项目
- 资产管理：固定资产/流动资产分类管理
- 用户管理：业主/租户/员工多角色管理
- 费用管理：物业费/水电费/停车费管理
- 报修服务：在线报修、进度跟踪
- 增值服务：保洁/安保/场地租赁预约
- 消息通知：缴费提醒、维修进度推送

### 管理端功能
- 首页：数据概览、统计图表
- 项目管理：项目信息维护
- 资产管理：资产信息管理、导入导出
- 用户管理：用户信息维护、角色管理
- 房屋管理：房屋信息维护
- 费用管理：收费标准、费用记录、报表
- 服务管理：服务预约管理
- 报修管理：报修工单管理
- 消息管理：消息发送管理

### 业主端功能
- 首页：个人信息概览
- 我的房屋：房屋信息查看
- 费用缴纳：在线缴费
- 报修服务：提交报修、进度查询
- 增值服务：服务预约
- 消息通知：消息查看
- 个人中心：个人信息管理

## 技术栈

### 后端
- Spring Boot 2.7.18
- MyBatis Plus 3.5.3.1
- Spring Security
- JWT
- MySQL 8.0
- Redis

### 前端
- Vue 3 + Element Plus（管理端）
- React 18 + Ant Design（业主端）
- Vite
- Axios
- Pinia/Zustand

## 常见问题

### Maven依赖下载慢
- 配置阿里云Maven镜像
- 或使用IDEA的Maven设置

### 数据库连接失败
- 检查MySQL服务是否启动
- 检查用户名密码是否正确
- 检查数据库是否已创建

### 前端启动失败
- 确保Node.js版本 >= 16
- 删除node_modules重新安装
- 检查端口是否被占用

## 开发说明

- 后端API地址：http://localhost:8080/api
- 管理端地址：http://localhost:3000
- 业主端地址：http://localhost:3001

## 许可证

MIT License
