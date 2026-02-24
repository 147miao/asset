CREATE DATABASE IF NOT EXISTS property_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE property_management;

CREATE TABLE pm_project (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    project_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    project_type VARCHAR(50) NOT NULL COMMENT '项目类型：residential-住宅, commercial-商业, industrial-工业园区',
    address VARCHAR(255) COMMENT '项目地址',
    building_area DECIMAL(15,2) COMMENT '建筑面积',
    building_count INT COMMENT '楼栋数量',
    business_distribution TEXT COMMENT '业态分布',
    status VARCHAR(20) DEFAULT 'operating' COMMENT '运营状态：operating-在营, pending-待交付, maintenance-维修中',
    equipment_online_rate DECIMAL(5,2) COMMENT '设备在线率',
    complaint_handling_rate DECIMAL(5,2) COMMENT '投诉处理率',
    description TEXT COMMENT '项目描述',
    deleted INT DEFAULT 0 COMMENT '删除标记：0-未删除, 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物业项目表';

CREATE TABLE pm_house (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    project_id BIGINT NOT NULL COMMENT '项目ID',
    project_name VARCHAR(100) COMMENT '项目名称',
    building_no VARCHAR(20) COMMENT '楼栋号',
    unit_no VARCHAR(20) COMMENT '单元号',
    room_no VARCHAR(20) COMMENT '房间号',
    house_type VARCHAR(50) COMMENT '房屋类型',
    building_area DECIMAL(10,2) COMMENT '建筑面积',
    usable_area DECIMAL(10,2) COMMENT '使用面积',
    owner_name VARCHAR(50) COMMENT '业主姓名',
    owner_id BIGINT COMMENT '业主ID',
    owner_phone VARCHAR(20) COMMENT '业主电话',
    status VARCHAR(20) DEFAULT 'vacant' COMMENT '状态：vacant-空置, occupied-已入住, rented-已出租',
    remark TEXT COMMENT '备注',
    deleted INT DEFAULT 0 COMMENT '删除标记',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房屋信息表';

CREATE TABLE pm_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    id_card VARCHAR(20) COMMENT '身份证号',
    user_type VARCHAR(20) NOT NULL COMMENT '用户类型：owner-业主, tenant-租户, employee-员工',
    project_id BIGINT COMMENT '所属项目ID',
    project_name VARCHAR(100) COMMENT '所属项目名称',
    house_id BIGINT COMMENT '房屋ID',
    house_info VARCHAR(100) COMMENT '房屋信息',
    lease_start_date DATE COMMENT '租赁开始日期',
    lease_end_date DATE COMMENT '租赁结束日期',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态：active-正常, disabled-禁用',
    avatar VARCHAR(255) COMMENT '头像',
    address VARCHAR(255) COMMENT '地址',
    remark TEXT COMMENT '备注',
    deleted INT DEFAULT 0 COMMENT '删除标记',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE pm_asset (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    asset_code VARCHAR(50) COMMENT '资产编号',
    asset_name VARCHAR(100) NOT NULL COMMENT '资产名称',
    asset_type VARCHAR(50) NOT NULL COMMENT '资产类型：fixed-固定资产, current-流动资产',
    category VARCHAR(50) COMMENT '资产分类：elevator-电梯, monitor-监控, power-配电设备, office-办公用品, cleaning-保洁工具',
    project_id BIGINT COMMENT '所属项目ID',
    project_name VARCHAR(100) COMMENT '所属项目名称',
    original_value DECIMAL(15,2) COMMENT '原值',
    current_value DECIMAL(15,2) COMMENT '现值',
    purchase_date DATE COMMENT '采购日期',
    storage_date DATE COMMENT '入库日期',
    allocate_date DATE COMMENT '分配日期',
    status VARCHAR(20) DEFAULT 'in_storage' COMMENT '状态：in_storage-在库, in_use-使用中, maintenance-维修中, scrapped-已报废',
    location VARCHAR(100) COMMENT '存放位置',
    department VARCHAR(100) COMMENT '使用部门',
    responsible_person VARCHAR(50) COMMENT '负责人',
    last_maintenance_date DATE COMMENT '最后维护日期',
    scrap_date DATE COMMENT '报废日期',
    scrap_reason VARCHAR(255) COMMENT '报废原因',
    remark TEXT COMMENT '备注',
    deleted INT DEFAULT 0 COMMENT '删除标记',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产表';

CREATE TABLE pm_fee_standard (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    project_id BIGINT COMMENT '项目ID',
    project_name VARCHAR(100) COMMENT '项目名称',
    fee_type VARCHAR(50) NOT NULL COMMENT '费用类型：property-物业费, water-水费, electricity-电费, parking-停车费',
    fee_name VARCHAR(100) NOT NULL COMMENT '费用名称',
    unit_price DECIMAL(10,4) COMMENT '单价',
    unit VARCHAR(20) COMMENT '单位',
    calculation_method VARCHAR(50) COMMENT '计算方式：area-按面积, fixed-固定金额, meter-按表读数',
    billing_cycle VARCHAR(50) COMMENT '账单周期：monthly-月, quarterly-季度, yearly-年',
    start_date DATE COMMENT '生效开始日期',
    end_date DATE COMMENT '生效结束日期',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态：active-生效, inactive-失效',
    remark TEXT COMMENT '备注',
    deleted INT DEFAULT 0 COMMENT '删除标记',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准表';

CREATE TABLE pm_fee_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    fee_no VARCHAR(50) COMMENT '费用编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    user_name VARCHAR(50) COMMENT '用户姓名',
    project_id BIGINT COMMENT '项目ID',
    project_name VARCHAR(100) COMMENT '项目名称',
    house_id BIGINT COMMENT '房屋ID',
    house_info VARCHAR(100) COMMENT '房屋信息',
    fee_type VARCHAR(50) NOT NULL COMMENT '费用类型',
    fee_name VARCHAR(100) COMMENT '费用名称',
    amount DECIMAL(15,2) NOT NULL COMMENT '应收金额',
    paid_amount DECIMAL(15,2) DEFAULT 0 COMMENT '已付金额',
    unpaid_amount DECIMAL(15,2) COMMENT '未付金额',
    billing_period VARCHAR(50) COMMENT '账单周期',
    due_date DATE COMMENT '应付日期',
    pay_date DATE COMMENT '支付日期',
    pay_method VARCHAR(50) COMMENT '支付方式：cash-现金, wechat-微信, alipay-支付宝, bank-银行转账',
    status VARCHAR(20) DEFAULT 'unpaid' COMMENT '状态：unpaid-未支付, partial-部分支付, paid-已支付',
    remark TEXT COMMENT '备注',
    deleted INT DEFAULT 0 COMMENT '删除标记',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='费用记录表';

CREATE TABLE pm_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    title VARCHAR(200) NOT NULL COMMENT '消息标题',
    content TEXT COMMENT '消息内容',
    message_type VARCHAR(50) NOT NULL COMMENT '消息类型：fee-缴费提醒, repair-维修进度, notice-项目公告, service-服务通知',
    sender_id BIGINT COMMENT '发送者ID',
    sender_name VARCHAR(50) COMMENT '发送者姓名',
    receiver_id BIGINT COMMENT '接收者ID',
    receiver_name VARCHAR(50) COMMENT '接收者姓名',
    project_id BIGINT COMMENT '项目ID',
    project_name VARCHAR(100) COMMENT '项目名称',
    related_type VARCHAR(50) COMMENT '关联类型',
    related_id BIGINT COMMENT '关联ID',
    status VARCHAR(20) DEFAULT 'unread' COMMENT '状态：unread-未读, read-已读',
    read_time DATETIME COMMENT '阅读时间',
    deleted INT DEFAULT 0 COMMENT '删除标记',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

CREATE TABLE pm_service_appointment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    appointment_no VARCHAR(50) COMMENT '预约编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    user_name VARCHAR(50) COMMENT '用户姓名',
    user_phone VARCHAR(20) COMMENT '用户电话',
    project_id BIGINT COMMENT '项目ID',
    project_name VARCHAR(100) COMMENT '项目名称',
    house_id BIGINT COMMENT '房屋ID',
    house_info VARCHAR(100) COMMENT '房屋信息',
    service_type VARCHAR(50) NOT NULL COMMENT '服务类型：cleaning-保洁, security-安保, venue-场地租赁, other-其他',
    service_name VARCHAR(100) COMMENT '服务名称',
    appointment_date DATE COMMENT '预约日期',
    appointment_time VARCHAR(20) COMMENT '预约时间',
    address VARCHAR(255) COMMENT '服务地址',
    description TEXT COMMENT '服务描述',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending-待处理, assigned-已分配, in_progress-进行中, completed-已完成, cancelled-已取消, rated-已评价',
    fee DECIMAL(10,2) COMMENT '服务费用',
    complete_date DATE COMMENT '完成日期',
    remark TEXT COMMENT '备注',
    feedback TEXT COMMENT '用户反馈',
    rating INT COMMENT '评分：1-5',
    deleted INT DEFAULT 0 COMMENT '删除标记',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务预约表';

CREATE TABLE pm_repair (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    repair_no VARCHAR(50) COMMENT '报修编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    user_name VARCHAR(50) COMMENT '用户姓名',
    user_phone VARCHAR(20) COMMENT '用户电话',
    project_id BIGINT COMMENT '项目ID',
    project_name VARCHAR(100) COMMENT '项目名称',
    house_id BIGINT COMMENT '房屋ID',
    house_info VARCHAR(100) COMMENT '房屋信息',
    repair_type VARCHAR(50) NOT NULL COMMENT '报修类型：water-水电, door-门窗, wall-墙面, other-其他',
    description TEXT COMMENT '报修描述',
    images TEXT COMMENT '图片',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending-待处理, assigned-已分配, in_progress-维修中, completed-已完成, cancelled-已取消, rated-已评价',
    assignee_id VARCHAR(50) COMMENT '维修人员ID',
    assignee_name VARCHAR(50) COMMENT '维修人员姓名',
    assign_date DATE COMMENT '分配日期',
    complete_date DATE COMMENT '完成日期',
    result TEXT COMMENT '维修结果',
    feedback TEXT COMMENT '用户反馈',
    rating INT COMMENT '评分：1-5',
    deleted INT DEFAULT 0 COMMENT '删除标记',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修表';

INSERT INTO pm_project (project_name, project_type, address, building_area, building_count, business_distribution, status, equipment_online_rate, complaint_handling_rate, description) VALUES
('阳光花园', 'residential', '北京市朝阳区阳光路1号', 150000.00, 20, '住宅为主，配套商业街', 'operating', 98.50, 99.20, '高端住宅小区'),
('创业大厦', 'commercial', '北京市海淀区中关村大街100号', 80000.00, 1, '写字楼，配套餐饮', 'operating', 99.00, 98.50, '甲级写字楼'),
('科技产业园', 'industrial', '北京市大兴区经济开发区A区', 200000.00, 15, '厂房、仓库、办公楼', 'operating', 97.80, 98.00, '综合性工业园区');

INSERT INTO pm_user (username, password, real_name, phone, email, user_type, project_id, project_name, status) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', '13800000000', 'admin@property.com', 'employee', NULL, NULL, 'active'),
('zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', '13800000001', 'zhangsan@example.com', 'owner', 1, '阳光花园', 'active'),
('lisi', 'e10adc3949ba59abbe56e057f20f883e', '李四', '13800000002', 'lisi@example.com', 'tenant', 2, '创业大厦', 'active');

INSERT INTO pm_house (project_id, project_name, building_no, unit_no, room_no, house_type, building_area, usable_area, owner_id, owner_name, owner_phone, status) VALUES
(1, '阳光花园', '1', '1', '101', '住宅', 120.00, 100.00, 2, '张三', '13800000001', 'occupied'),
(1, '阳光花园', '1', '1', '102', '住宅', 100.00, 85.00, NULL, NULL, NULL, 'vacant'),
(2, '创业大厦', 'A', NULL, '1001', '办公', 200.00, 180.00, 3, '李四', '13800000002', 'rented');

INSERT INTO pm_fee_standard (project_id, project_name, fee_type, fee_name, unit_price, unit, calculation_method, billing_cycle, status) VALUES
(1, '阳光花园', 'property', '物业费', 2.50, '元/平方米/月', 'area', 'monthly', 'active'),
(1, '阳光花园', 'water', '水费', 5.00, '元/吨', 'meter', 'monthly', 'active'),
(1, '阳光花园', 'electricity', '电费', 0.60, '元/度', 'meter', 'monthly', 'active'),
(2, '创业大厦', 'property', '物业费', 8.00, '元/平方米/月', 'area', 'monthly', 'active');

INSERT INTO pm_asset (asset_code, asset_name, asset_type, category, project_id, project_name, original_value, current_value, purchase_date, storage_date, status, location, responsible_person) VALUES
('AST001', '客梯A1', 'fixed', 'elevator', 1, '阳光花园', 500000.00, 450000.00, '2020-01-15', '2020-02-01', 'in_use', '1号楼', '王工程师'),
('AST002', '监控摄像头组', 'fixed', 'monitor', 1, '阳光花园', 80000.00, 60000.00, '2020-03-01', '2020-03-15', 'in_use', '全小区', '李安全'),
('AST003', '配电柜', 'fixed', 'power', 2, '创业大厦', 200000.00, 180000.00, '2019-06-01', '2019-07-01', 'in_use', '地下配电室', '张电工'),
('AST004', '办公桌椅', 'current', 'office', NULL, NULL, 5000.00, 4000.00, '2022-01-01', '2022-01-05', 'in_use', '物业办公室', '行政部');
