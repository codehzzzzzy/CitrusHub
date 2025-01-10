-- 创建库
create database if not exists citrus_hub;
-- 切换库
use citrus_hub;

-- 用户表
create table user
(
    id              int             auto_increment              PRIMARY KEY COMMENT '用户id',
    account         varchar(255)                                NOT NULL    COMMENT '账号',
    password        varchar(255)                                NOT NULL    COMMENT '密码',
    create_time     datetime        default CURRENT_TIMESTAMP   NOT NULL    COMMENT '创建时间',
    update_time     datetime        default CURRENT_TIMESTAMP   NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted      tinyint         default 0                   NOT NULL    COMMENT '是否删除（0:不删除;1:删除）',
    `type`          tinyint         default 2                   NOT NULL    COMMENT '用户类型（1:管理员;2:用户;3:专家）',
    expertise       varchar(255)                                            COMMENT '专业领域',
    remark          varchar(255)                                            COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin comment '用户表';

-- 知识库表
CREATE TABLE knowledge_base (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '知识库id',
    category_id     int                                                     COMMENT '分类id',
    title           varchar(255)                                NOT NULL    COMMENT '标题',
    context         TEXT                                        NOT NULL    COMMENT '内容',
    create_time     datetime        default CURRENT_TIMESTAMP   NOT NULL    COMMENT '创建时间',
    update_time     datetime        default CURRENT_TIMESTAMP   NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted      tinyint         default 0                   NOT NULL    COMMENT '是否删除（0:不删除;1:删除）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='知识库表';

-- 知识库分类表
CREATE TABLE knowledge_category (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '分类id',
    category_name   varchar(255)                                NOT NULL    COMMENT '分类名称',
    description     varchar(255)                                NOT NULL    COMMENT '分类描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='知识库分类表';

-- 供需信息
CREATE TABLE citrus_supply_demand (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '供需信息id',
    title           varchar(255)                                NOT NULL    COMMENT '标题',
    `type`          varchar(255)                                NOT NULL    COMMENT '供应/求购',
    category        varchar(255)                                NOT NULL    COMMENT '柑橘种类',
    region          varchar(255)                                NOT NULL    COMMENT '地区',
    release_date    varchar(255)                                NOT NULL    COMMENT '发布日期 mm-dd',
    require_time_pre    int                                     NOT NULL    COMMENT '需求日期-前',
    require_time_after  int                                     NOT NULL    COMMENT '需求日期-后'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='供需信息表';

-- 资讯信息
CREATE TABLE citrus_consultation (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '资讯信息id',
    title           varchar(255)                                NOT NULL    COMMENT '标题',
    context         varchar(255)                                NOT NULL    COMMENT '内容'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='资讯信息表';

-- 对话信息表
CREATE TABLE citrus_chat (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '对话id',
    user_id         int                                         NOT NULL    COMMENT '用户id',
    `name`          varchar(255)                                NOT NULL    COMMENT '对话名称',
    thread_slug     varchar(255)                                NOT NULL    COMMENT '对话标识'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='对话信息表';
