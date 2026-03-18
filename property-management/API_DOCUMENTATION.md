# 智慧物业管理系统 - 前后端接口文档

## 基础信息

- **Base URL**: `/api`
- **响应格式**: JSON
- **统一响应结构**:
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": {},
    "total": 0  // 分页时返回
  }
  ```

## 通用状态码

| Code | 含义 |
|------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或登录过期 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 一、用户管理模块 (/user)

### 1.1 用户登录
- **接口**: `POST /user/login`
- **描述**: 用户登录
- **请求参数**:
  ```json
  {
    "phone": "string",    // 手机号
    "password": "string"  // 密码
  }
  ```
- **响应数据**: User 对象

### 1.2 用户注册
- **接口**: `POST /user/register`
- **描述**: 用户注册
- **请求参数**: User 对象
- **响应数据**: User 对象

### 1.3 分页查询用户
- **接口**: `GET /user/page`
- **描述**: 分页查询用户列表
- **请求参数**:
  - `pageNum`: 页码 (默认1)
  - `pageSize`: 每页大小 (默认10)
  - `userName`: 用户名 (可选)
  - `phone`: 手机号 (可选)
  - `userType`: 用户类型 (可选: owner/tenant/employee)
  - `status`: 状态 (可选: active/disabled)
- **响应数据**: PageResult<User>

### 1.4 根据ID查询用户
- **接口**: `GET /user/{id}`
- **描述**: 根据ID查询用户详情
- **路径参数**: `id` - 用户ID
- **响应数据**: User 对象

### 1.5 更新用户信息
- **接口**: `PUT /user`
- **描述**: 更新用户信息
- **请求参数**: User 对象
- **响应数据**: Boolean

### 1.6 删除用户
- **接口**: `DELETE /user/{id}`
- **描述**: 删除用户
- **路径参数**: `id` - 用户ID
- **响应数据**: Boolean

### 1.7 修改密码
- **接口**: `PUT /user/password`
- **描述**: 修改用户密码
- **请求参数**:
  - `id`: 用户ID
  - `oldPassword`: 旧密码
  - `newPassword`: 新密码
- **响应数据**: Boolean

### 1.8 重置密码
- **接口**: `PUT /user/resetPassword/{id}`
- **描述**: 重置用户密码为默认密码
- **路径参数**: `id` - 用户ID
- **响应数据**: Boolean

### 1.9 根据手机号查询用户
- **接口**: `GET /user/phone/{phone}`
- **描述**: 根据手机号查询用户
- **路径参数**: `phone` - 手机号
- **响应数据**: User 对象

### 1.10 用户统计
- **接口**: `GET /user/statistics`
- **描述**: 获取用户统计数据
- **响应数据**: List<Map<String, Object>>

### 1.11 用户费用统计
- **接口**: `GET /user/feeStatistics/{userId}`
- **描述**: 获取指定用户的费用统计
- **路径参数**: `userId` - 用户ID
- **响应数据**: List<Map<String, Object>>

### 1.12 更新用户状态
- **接口**: `PUT /user/status/{id}`
- **描述**: 更新用户状态
- **路径参数**: `id` - 用户ID
- **请求参数**: `status` - 状态 (active/disabled)
- **响应数据**: Boolean

---

## 二、报修管理模块 (/repair)

### 2.1 分页查询报修
- **接口**: `GET /repair/page`
- **描述**: 分页查询报修列表
- **请求参数**:
  - `pageNum`: 页码 (默认1)
  - `pageSize`: 每页大小 (默认10)
  - `userName`: 用户名 (可选)
  - `repairType`: 报修类型 (可选: plumbing/electrical/appliance/other)
  - `status`: 状态 (可选: pending/processing/completed/cancelled)
  - `projectId`: 项目ID (可选)
- **响应数据**: PageResult<Repair>

### 2.2 根据ID查询报修
- **接口**: `GET /repair/{id}`
- **描述**: 根据ID查询报修详情
- **路径参数**: `id` - 报修ID
- **响应数据**: Repair 对象

### 2.3 新增报修
- **接口**: `POST /repair`
- **描述**: 提交新的报修申请
- **请求参数**: Repair 对象
  ```json
  {
    "userId": 1,
    "title": "水管漏水",
    "description": "厨房水管漏水严重",
    "repairType": "plumbing"
  }
  ```
- **响应数据**: Boolean

### 2.4 更新报修
- **接口**: `PUT /repair`
- **描述**: 更新报修信息
- **请求参数**: Repair 对象
- **响应数据**: Boolean

### 2.5 取消报修
- **接口**: `PUT /repair/cancel/{id}`
- **描述**: 取消报修申请
- **路径参数**: `id` - 报修ID
- **响应数据**: Boolean

### 2.6 分配报修
- **接口**: `PUT /repair/assign/{id}`
- **描述**: 分配报修给维修人员
- **路径参数**: `id` - 报修ID
- **请求参数**:
  - `assigneeId`: 处理人ID
  - `assigneeName`: 处理人姓名
- **响应数据**: Boolean

### 2.7 完成报修
- **接口**: `PUT /repair/complete/{id}`
- **描述**: 标记报修为已完成
- **路径参数**: `id` - 报修ID
- **请求参数**: `result` - 处理结果
- **响应数据**: Boolean

### 2.8 评价报修
- **接口**: `PUT /repair/rate/{id}`
- **描述**: 对报修服务进行评价
- **路径参数**: `id` - 报修ID
- **请求参数**:
  - `rating`: 评分 (1-5)
  - `feedback`: 评价内容 (可选)
- **响应数据**: Boolean

### 2.9 查询用户的报修
- **接口**: `GET /repair/user/{userId}`
- **描述**: 查询指定用户的所有报修
- **路径参数**: `userId` - 用户ID
- **响应数据**: List<Repair>

### 2.10 报修统计
- **接口**: `GET /repair/statistics`
- **描述**: 获取报修统计数据
- **请求参数**: `projectId` - 项目ID (可选)
- **响应数据**: List<Map<String, Object>>

---

## 三、服务预约模块 (/service)

### 3.1 分页查询服务预约
- **接口**: `GET /service/page`
- **描述**: 分页查询服务预约列表
- **请求参数**:
  - `pageNum`: 页码 (默认1)
  - `pageSize`: 每页大小 (默认10)
  - `userName`: 用户名 (可选)
  - `serviceType`: 服务类型 (可选: cleaning/security/venue/other)
  - `status`: 状态 (可选: pending/assigned/in_progress/completed/cancelled/rated)
  - `projectId`: 项目ID (可选)
- **响应数据**: PageResult<ServiceAppointment>

### 3.2 根据ID查询服务预约
- **接口**: `GET /service/{id}`
- **描述**: 根据ID查询服务预约详情
- **路径参数**: `id` - 服务预约ID
- **响应数据**: ServiceAppointment 对象

### 3.3 新增服务预约
- **接口**: `POST /service`
- **描述**: 提交新的服务预约
- **请求参数**: ServiceAppointment 对象
  ```json
  {
    "userId": 1,
    "serviceName": "家庭保洁",
    "description": "需要深度清洁",
    "serviceType": "cleaning",
    "fee": 200.00
  }
  ```
- **响应数据**: Boolean

### 3.4 更新服务预约
- **接口**: `PUT /service`
- **描述**: 更新服务预约信息
- **请求参数**: ServiceAppointment 对象
- **响应数据**: Boolean

### 3.5 取消服务预约
- **接口**: `PUT /service/cancel/{id}`
- **描述**: 取消服务预约
- **路径参数**: `id` - 服务预约ID
- **响应数据**: Boolean

### 3.6 完成服务预约
- **接口**: `PUT /service/complete/{id}`
- **描述**: 标记服务预约为已完成
- **路径参数**: `id` - 服务预约ID
- **请求参数**: `remark` - 备注 (可选)
- **响应数据**: Boolean

### 3.7 评价服务
- **接口**: `PUT /service/rate/{id}`
- **描述**: 对服务进行评价
- **路径参数**: `id` - 服务预约ID
- **请求参数**:
  - `rating`: 评分 (1-5)
  - `feedback`: 评价内容 (可选)
- **响应数据**: Boolean

### 3.8 查询用户的服务预约
- **接口**: `GET /service/user/{userId}`
- **描述**: 查询指定用户的所有服务预约
- **路径参数**: `userId` - 用户ID
- **响应数据**: List<ServiceAppointment>

### 3.9 服务统计
- **接口**: `GET /service/statistics`
- **描述**: 获取服务预约统计数据
- **请求参数**: `projectId` - 项目ID (可选)
- **响应数据**: List<Map<String, Object>>

### 3.10 分配服务
- **接口**: `PUT /service/assign/{id}`
- **描述**: 分配服务给服务人员
- **路径参数**: `id` - 服务预约ID
- **请求参数**: `assigneeId` - 服务人员ID
- **响应数据**: Boolean

---

## 四、费用管理模块 (/fee)

### 4.1 分页查询费用记录
- **接口**: `GET /fee/page`
- **描述**: 分页查询费用记录
- **请求参数**:
  - `pageNum`: 页码 (默认1)
  - `pageSize`: 每页大小 (默认10)
  - `userName`: 用户名 (可选)
  - `feeType`: 费用类型 (可选: property/water/electricity/parking)
  - `status`: 状态 (可选: unpaid/partial/paid)
  - `projectId`: 项目ID (可选)
- **响应数据**: PageResult<FeeRecord>

### 4.2 根据ID查询费用记录
- **接口**: `GET /fee/{id}`
- **描述**: 根据ID查询费用记录详情
- **路径参数**: `id` - 费用记录ID
- **响应数据**: FeeRecord 对象

### 4.3 新增费用记录
- **接口**: `POST /fee`
- **描述**: 新增费用记录
- **请求参数**: FeeRecord 对象
- **响应数据**: Boolean

### 4.4 更新费用记录
- **接口**: `PUT /fee`
- **描述**: 更新费用记录
- **请求参数**: FeeRecord 对象
- **响应数据**: Boolean

### 4.5 删除费用记录
- **接口**: `DELETE /fee/{id}`
- **描述**: 删除费用记录
- **路径参数**: `id` - 费用记录ID
- **响应数据**: Boolean

### 4.6 支付费用
- **接口**: `POST /fee/pay/{id}`
- **描述**: 支付指定费用
- **路径参数**: `id` - 费用记录ID
- **请求参数**: `payMethod` - 支付方式 (cash/wechat/alipay/bank)
- **响应数据**: Boolean

### 4.7 查询用户的费用
- **接口**: `GET /fee/user/{userId}`
- **描述**: 查询指定用户的所有费用
- **路径参数**: `userId` - 用户ID
- **响应数据**: List<FeeRecord>

### 4.8 查询用户的未支付费用
- **接口**: `GET /fee/unpaid/{userId}`
- **描述**: 查询指定用户的未支付费用
- **路径参数**: `userId` - 用户ID
- **响应数据**: List<FeeRecord>

### 4.9 收入统计
- **接口**: `GET /fee/incomeStatistics`
- **描述**: 获取收入统计数据
- **请求参数**:
  - `projectId`: 项目ID (可选)
  - `startDate`: 开始日期
  - `endDate`: 结束日期
- **响应数据**: Map<String, Object>

### 4.10 支出统计
- **接口**: `GET /fee/expenseStatistics`
- **描述**: 获取支出统计数据
- **请求参数**:
  - `projectId`: 项目ID (可选)
  - `startDate`: 开始日期
  - `endDate`: 结束日期
- **响应数据**: Map<String, Object>

### 4.11 欠费统计
- **接口**: `GET /fee/arrearsStatistics`
- **描述**: 获取欠费统计数据
- **请求参数**: `projectId` - 项目ID (可选)
- **响应数据**: List<Map<String, Object>>

### 4.12 总欠费金额
- **接口**: `GET /fee/totalArrears`
- **描述**: 获取总欠费金额
- **请求参数**: `projectId` - 项目ID (可选)
- **响应数据**: BigDecimal

### 4.13 费用类型统计
- **接口**: `GET /fee/feeTypeStatistics`
- **描述**: 按费用类型统计
- **请求参数**:
  - `projectId`: 项目ID (可选)
  - `startDate`: 开始日期
  - `endDate`: 结束日期
- **响应数据**: List<Map<String, Object>>

---

## 五、房屋管理模块 (/house)

### 5.1 分页查询房屋
- **接口**: `GET /house/page`
- **描述**: 分页查询房屋列表
- **请求参数**:
  - `pageNum`: 页码 (默认1)
  - `pageSize`: 每页大小 (默认10)
  - `houseInfo`: 房屋信息 (可选)
  - `status`: 状态 (可选: vacant/occupied/rented)
  - `projectId`: 项目ID (可选)
- **响应数据**: PageResult<House>

### 5.2 根据ID查询房屋
- **接口**: `GET /house/{id}`
- **描述**: 根据ID查询房屋详情
- **路径参数**: `id` - 房屋ID
- **响应数据**: House 对象

### 5.3 新增房屋
- **接口**: `POST /house`
- **描述**: 新增房屋
- **请求参数**: House 对象
- **响应数据**: Boolean

### 5.4 更新房屋
- **接口**: `PUT /house`
- **描述**: 更新房屋信息
- **请求参数**: House 对象
- **响应数据**: Boolean

### 5.5 删除房屋
- **接口**: `DELETE /house/{id}`
- **描述**: 删除房屋
- **路径参数**: `id` - 房屋ID
- **响应数据**: Boolean

### 5.6 查询项目的房屋
- **接口**: `GET /house/project/{projectId}`
- **描述**: 查询指定项目的所有房屋
- **路径参数**: `projectId` - 项目ID
- **响应数据**: List<House>

### 5.7 查询业主的房屋
- **接口**: `GET /house/owner/{ownerId}`
- **描述**: 查询指定业主的所有房屋
- **路径参数**: `ownerId` - 业主ID
- **响应数据**: List<House>

### 5.8 更新房屋业主
- **接口**: `PUT /house/owner`
- **描述**: 更新房屋的业主信息
- **请求参数**:
  - `id`: 房屋ID
  - `ownerId`: 业主ID
  - `ownerName`: 业主姓名
  - `ownerPhone`: 业主电话
- **响应数据**: Boolean

---

## 六、消息通知模块 (/message)

### 6.1 分页查询消息
- **接口**: `GET /message/page`
- **描述**: 分页查询消息列表
- **请求参数**:
  - `pageNum`: 页码 (默认1)
  - `pageSize`: 每页大小 (默认10)
  - `userId`: 用户ID (可选)
  - `messageType`: 消息类型 (可选: fee/repair/notice/service)
  - `status`: 状态 (可选: unread/read)
- **响应数据**: PageResult<Message>

### 6.2 根据ID查询消息
- **接口**: `GET /message/{id}`
- **描述**: 根据ID查询消息详情
- **路径参数**: `id` - 消息ID
- **响应数据**: Message 对象

### 6.3 新增消息
- **接口**: `POST /message`
- **描述**: 新增消息
- **请求参数**: Message 对象
- **响应数据**: Boolean

### 6.4 发送消息
- **接口**: `POST /message/send`
- **描述**: 发送消息给指定用户
- **请求参数**:
  - `title`: 标题
  - `content`: 内容
  - `messageType`: 消息类型
  - `senderId`: 发送者ID (可选)
  - `receiverId`: 接收者ID
  - `projectId`: 项目ID (可选)
  - `relatedType`: 关联类型 (可选)
  - `relatedId`: 关联ID (可选)
- **响应数据**: Boolean

### 6.5 广播消息
- **接口**: `POST /message/broadcast`
- **描述**: 向项目内所有用户广播消息
- **请求参数**:
  - `title`: 标题
  - `content`: 内容
  - `messageType`: 消息类型
  - `projectId`: 项目ID (可选)
- **响应数据**: Boolean

### 6.6 标记消息已读
- **接口**: `PUT /message/read/{id}`
- **描述**: 将消息标记为已读
- **路径参数**: `id` - 消息ID
- **响应数据**: Boolean

### 6.7 标记所有消息已读
- **接口**: `PUT /message/readAll/{userId}`
- **描述**: 将用户的所有消息标记为已读
- **路径参数**: `userId` - 用户ID
- **响应数据**: Boolean

### 6.8 查询用户的未读消息
- **接口**: `GET /message/unread/{userId}`
- **描述**: 查询指定用户的未读消息
- **路径参数**: `userId` - 用户ID
- **响应数据**: List<Message>

### 6.9 统计未读消息数
- **接口**: `GET /message/count/{userId}`
- **描述**: 统计指定用户的未读消息数量
- **路径参数**: `userId` - 用户ID
- **响应数据**: Integer

---

## 七、项目管理模块 (/project)

### 7.1 分页查询项目
- **接口**: `GET /project/page`
- **描述**: 分页查询项目列表
- **请求参数**:
  - `pageNum`: 页码 (默认1)
  - `pageSize`: 每页大小 (默认10)
  - `projectName`: 项目名称 (可选)
  - `projectType`: 项目类型 (可选: residential/commercial/industrial)
  - `status`: 状态 (可选: operating/pending/maintenance)
- **响应数据**: PageResult<Project>

### 7.2 根据ID查询项目
- **接口**: `GET /project/{id}`
- **描述**: 根据ID查询项目详情
- **路径参数**: `id` - 项目ID
- **响应数据**: Project 对象

### 7.3 新增项目
- **接口**: `POST /project`
- **描述**: 新增项目
- **请求参数**: Project 对象
- **响应数据**: Boolean

### 7.4 更新项目
- **接口**: `PUT /project`
- **描述**: 更新项目信息
- **请求参数**: Project 对象
- **响应数据**: Boolean

### 7.5 删除项目
- **接口**: `DELETE /project/{id}`
- **描述**: 删除项目
- **路径参数**: `id` - 项目ID
- **响应数据**: Boolean

### 7.6 查询所有项目
- **接口**: `GET /project/all`
- **描述**: 查询所有项目
- **响应数据**: List<Project>

### 7.7 项目统计
- **接口**: `GET /project/statistics/{projectId}`
- **描述**: 获取指定项目的统计数据
- **路径参数**: `projectId` - 项目ID
- **响应数据**: Map<String, Object>

### 7.8 项目列表
- **接口**: `GET /project/list`
- **描述**: 获取项目列表（简化版）
- **响应数据**: List<Map<String, Object>>

---

## 八、数据模型定义

### 8.1 User (用户)
```json
{
  "id": 1,
  "username": "zhangsan",
  "realName": "张三",
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "idCard": "110101199001011234",
  "userType": "owner",
  "projectId": 1,
  "projectName": "阳光花园",
  "houseId": 1,
  "houseInfo": "1栋1单元101",
  "status": "active",
  "avatar": "http://example.com/avatar.jpg",
  "address": "北京市朝阳区xxx",
  "createTime": "2024-01-01 10:00:00",
  "updateTime": "2024-01-01 10:00:00"
}
```

### 8.2 Repair (报修)
```json
{
  "id": 1,
  "repairNo": "REP202401010001",
  "userId": 1,
  "userName": "张三",
  "userPhone": "13800138000",
  "projectId": 1,
  "projectName": "阳光花园",
  "houseId": 1,
  "houseInfo": "1栋1单元101",
  "title": "水管漏水",
  "description": "厨房水管漏水严重",
  "repairType": "plumbing",
  "status": "pending",
  "assigneeId": null,
  "assigneeName": null,
  "result": null,
  "rating": null,
  "feedback": null,
  "createTime": "2024-01-01 10:00:00",
  "updateTime": "2024-01-01 10:00:00"
}
```

### 8.3 ServiceAppointment (服务预约)
```json
{
  "id": 1,
  "appointmentNo": "SVC202401010001",
  "userId": 1,
  "userName": "张三",
  "userPhone": "13800138000",
  "projectId": 1,
  "projectName": "阳光花园",
  "houseId": 1,
  "houseInfo": "1栋1单元101",
  "serviceType": "cleaning",
  "serviceName": "家庭保洁",
  "appointmentDate": "2024-01-02",
  "appointmentTime": "09:00",
  "address": "1栋1单元101",
  "description": "需要深度清洁",
  "status": "pending",
  "fee": 200.00,
  "completeDate": null,
  "remark": null,
  "feedback": null,
  "rating": null,
  "createTime": "2024-01-01 10:00:00",
  "updateTime": "2024-01-01 10:00:00"
}
```

### 8.4 FeeRecord (费用记录)
```json
{
  "id": 1,
  "feeNo": "FEE202401010001",
  "userId": 1,
  "userName": "张三",
  "projectId": 1,
  "projectName": "阳光花园",
  "houseId": 1,
  "houseInfo": "1栋1单元101",
  "feeType": "property",
  "feeName": "物业费",
  "amount": 200.00,
  "paidAmount": 0,
  "unpaidAmount": 200.00,
  "billingPeriod": "2024-01",
  "dueDate": "2024-01-31",
  "payDate": null,
  "payMethod": null,
  "status": "unpaid",
  "remark": null,
  "createTime": "2024-01-01 10:00:00",
  "updateTime": "2024-01-01 10:00:00"
}
```

### 8.5 House (房屋)
```json
{
  "id": 1,
  "projectId": 1,
  "projectName": "阳光花园",
  "buildingNo": "1",
  "unitNo": "1",
  "roomNo": "101",
  "houseType": "三室一厅",
  "buildingArea": 120.00,
  "usableArea": 100.00,
  "ownerName": "张三",
  "ownerId": 1,
  "ownerPhone": "13800138000",
  "status": "occupied",
  "remark": null,
  "createTime": "2024-01-01 10:00:00",
  "updateTime": "2024-01-01 10:00:00"
}
```

### 8.6 Message (消息)
```json
{
  "id": 1,
  "title": "缴费提醒",
  "content": "您有一笔物业费待缴纳",
  "messageType": "fee",
  "senderId": null,
  "senderName": "系统",
  "receiverId": 1,
  "receiverName": "张三",
  "projectId": 1,
  "projectName": "阳光花园",
  "relatedType": "fee",
  "relatedId": 1,
  "status": "unread",
  "readTime": null,
  "createTime": "2024-01-01 10:00:00",
  "updateTime": "2024-01-01 10:00:00"
}
```

### 8.7 Project (项目)
```json
{
  "id": 1,
  "projectName": "阳光花园",
  "projectType": "residential",
  "address": "北京市朝阳区xxx",
  "buildingArea": 50000.00,
  "buildingCount": 10,
  "businessDistribution": "住宅",
  "status": "operating",
  "equipmentOnlineRate": 98.5,
  "complaintHandlingRate": 95.0,
  "description": "高档住宅小区",
  "createTime": "2024-01-01 10:00:00",
  "updateTime": "2024-01-01 10:00:00"
}
```

---

## 九、枚举值定义

### 9.1 用户类型 (userType)
- `owner` - 业主
- `tenant` - 租户
- `employee` - 员工

### 9.2 报修类型 (repairType)
- `plumbing` - 水管
- `electrical` - 电路
- `appliance` - 家电
- `other` - 其他

### 9.3 服务类型 (serviceType)
- `cleaning` - 保洁服务
- `security` - 安保服务
- `venue` - 场地租赁
- `other` - 其他

### 9.4 费用类型 (feeType)
- `property` - 物业费
- `water` - 水费
- `electricity` - 电费
- `parking` - 停车费

### 9.5 消息类型 (messageType)
- `fee` - 缴费提醒
- `repair` - 维修进度
- `notice` - 项目公告
- `service` - 服务通知

### 9.6 支付方式 (payMethod)
- `cash` - 现金
- `wechat` - 微信
- `alipay` - 支付宝
- `bank` - 银行转账

### 9.7 房屋状态 (houseStatus)
- `vacant` - 空置
- `occupied` - 已入住
- `rented` - 已出租

### 9.8 报修状态 (repairStatus)
- `pending` - 待处理
- `processing` - 处理中
- `completed` - 已完成
- `cancelled` - 已取消

### 9.9 服务状态 (serviceStatus)
- `pending` - 待处理
- `assigned` - 已分配
- `in_progress` - 进行中
- `completed` - 已完成
- `cancelled` - 已取消
- `rated` - 已评价

### 9.10 费用状态 (feeStatus)
- `unpaid` - 未支付
- `partial` - 部分支付
- `paid` - 已支付

### 9.11 项目类型 (projectType)
- `residential` - 住宅
- `commercial` - 商业
- `industrial` - 工业园区

### 9.12 项目状态 (projectStatus)
- `operating` - 在营
- `pending` - 待交付
- `maintenance` - 维修中

### 9.13 用户状态 (userStatus)
- `active` - 正常
- `disabled` - 禁用

### 9.14 消息状态 (messageStatus)
- `unread` - 未读
- `read` - 已读

---

## 十、前端 API 调用示例

### 10.1 业主端 API 调用 (frontend/owner/src/api/index.ts)

```typescript
// 登录
export const login = (data: LoginData) => 
  request<ApiResponse<UserInfo>>({ url: '/user/login', method: 'post', data })

// 获取房屋列表
export const getHouseList = () => 
  request({ url: '/house/list', method: 'get' })

// 获取费用列表
export const getFeeList = (params?: Record<string, unknown>) => 
  request({ url: '/fee/page', method: 'get', params })

// 支付费用
export const payFee = (id: string, payMethod: string) => 
  request({ url: `/fee/pay/${id}`, method: 'post', params: { payMethod } })

// 获取报修列表
export const getRepairsByUser = (userId: string) => 
  request({ url: `/repair/user/${userId}`, method: 'get' })

// 创建报修
export const createRepair = (data: Record<string, unknown>) => 
  request({ url: '/repair', method: 'post', data })

// 取消报修
export const cancelRepair = (id: string) => 
  request({ url: `/repair/cancel/${id}`, method: 'put' })

// 评价报修
export const rateRepair = (id: string, rating: number, feedback: string) => 
  request({ url: `/repair/rate/${id}`, method: 'put', params: { rating, feedback } })

// 获取服务列表
export const getServicesByUser = (userId: string) => 
  request({ url: `/service/user/${userId}`, method: 'get' })

// 创建服务预约
export const createService = (data: Record<string, unknown>) => 
  request({ url: '/service', method: 'post', data })

// 取消服务
export const cancelService = (id: string) => 
  request({ url: `/service/cancel/${id}`, method: 'put' })

// 评价服务
export const rateService = (id: string, rating: number, feedback: string) => 
  request({ url: `/service/rate/${id}`, method: 'put', params: { rating, feedback } })

// 获取消息列表
export const getUnreadMessages = (userId: string) => 
  request({ url: `/message/unread/${userId}`, method: 'get' })

// 标记消息已读
export const markAsRead = (id: string) => 
  request({ url: `/message/read/${id}`, method: 'put' })

// 更新用户信息
export const updateUserInfo = (data: Record<string, unknown>) => 
  request({ url: '/user', method: 'put', data })
```

### 10.2 管理端 API 调用 (frontend/admin/src/api/)

```typescript
// 用户管理
export function getUserPage(params: PageParams): Promise<ApiResponse<PageResult<User>>> {
  return request({ url: '/user/page', method: 'get', params })
}

export function updateUser(data: Partial<User>): Promise<ApiResponse<User>> {
  return request({ url: '/user', method: 'put', data })
}

// 报修管理
export function getRepairPage(params: PageParams): Promise<ApiResponse<PageResult<Repair>>> {
  return request({ url: '/repair/page', method: 'get', params })
}

export function assignRepair(id: string, assigneeId: string, assigneeName: string): Promise<ApiResponse<void>> {
  return request({ url: `/repair/assign/${id}`, method: 'put', params: { assigneeId, assigneeName } })
}

export function completeRepair(id: string, result: string): Promise<ApiResponse<void>> {
  return request({ url: `/repair/complete/${id}`, method: 'put', params: { result } })
}

// 服务管理
export function getServicePage(params: PageParams): Promise<ApiResponse<PageResult<Service>>> {
  return request({ url: '/service/page', method: 'get', params })
}

export function assignService(id: string, assigneeId: string): Promise<ApiResponse<void>> {
  return request({ url: `/service/assign/${id}`, method: 'put', params: { assigneeId } })
}

export function completeService(id: string, remark?: string): Promise<ApiResponse<void>> {
  return request({ url: `/service/complete/${id}`, method: 'put', params: { remark } })
}

// 费用管理
export function getFeePage(params: PageParams): Promise<ApiResponse<PageResult<FeeRecord>>> {
  return request({ url: '/fee/page', method: 'get', params })
}

export function addFeeRecord(data: Partial<FeeRecord>): Promise<ApiResponse<FeeRecord>> {
  return request({ url: '/fee', method: 'post', data })
}

// 房屋管理
export function getHousePage(params: PageParams): Promise<ApiResponse<PageResult<House>>> {
  return request({ url: '/house/page', method: 'get', params })
}

export function updateHouseOwner(id: string, ownerId: string, ownerName: string, ownerPhone: string): Promise<ApiResponse<void>> {
  return request({ url: '/house/owner', method: 'put', params: { id, ownerId, ownerName, ownerPhone } })
}

// 消息管理
export function getMessagePage(params: PageParams): Promise<ApiResponse<PageResult<Message>>> {
  return request({ url: '/message/page', method: 'get', params })
}

export function sendBroadcastMessage(title: string, content: string, messageType: string, projectId?: string): Promise<ApiResponse<void>> {
  return request({ url: '/message/broadcast', method: 'post', params: { title, content, messageType, projectId } })
}

// 项目管理
export function getProjectPage(params: PageParams): Promise<ApiResponse<PageResult<Project>>> {
  return request({ url: '/project/page', method: 'get', params })
}

export function getProjectStatistics(projectId: string): Promise<ApiResponse<Record<string, number>>> {
  return request({ url: `/project/statistics/${projectId}`, method: 'get' })
}
```

---

## 十一、分页参数说明

所有分页接口都支持以下参数：

| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| pageNum | Integer | 否 | 1 | 当前页码 |
| pageSize | Integer | 否 | 10 | 每页记录数 |

分页响应结构：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [],      // 数据列表
    "total": 100,       // 总记录数
    "pageNum": 1,       // 当前页码
    "pageSize": 10      // 每页大小
  }
}
```

---

## 十二、认证说明

- 除登录和注册接口外，所有接口都需要在请求头中携带 JWT Token
- Token 格式: `Authorization: Bearer {token}`
- Token 过期时间为 24 小时
- Token 过期后需要重新登录获取新的 Token

---

**文档版本**: v1.0  
**更新日期**: 2026-03-10  
**作者**: 智慧物业开发团队
