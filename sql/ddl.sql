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
    avatar          varchar(255)    default 'https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/202501201820579.jpg'  COMMENT '头像',
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
    url             varchar(255)                                NOT NULL    COMMENT '链接url',
    create_time     datetime        default CURRENT_TIMESTAMP   NOT NULL    COMMENT '创建时间',
    update_time     datetime        default CURRENT_TIMESTAMP   NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted      tinyint         default 0                   NOT NULL    COMMENT '是否删除（0:不删除;1:删除）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='知识库表';

-- 知识库分类表
CREATE TABLE knowledge_category (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '分类id',
    category_name   varchar(255)                                NOT NULL    COMMENT '分类名称',
    url             varchar(255)                                NOT NULL    COMMENT '图片url',
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
    require_time_after  int                                     NOT NULL    COMMENT '需求日期-后',
    image_url       varchar(255)                                NOT NULL    COMMENT '图片链接',
    url             varchar(255)                                NOT NULL    COMMENT '跳转链接'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='供需信息表';

-- 大模型对话信息表
CREATE TABLE citrus_chat (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '对话id',
    user_id         int                                         NOT NULL    COMMENT '用户id',
    `name`          varchar(255)                                NOT NULL    COMMENT '对话名称',
    thread_slug     varchar(255)                                NOT NULL    COMMENT '对话标识'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='大模型对话信息表';

-- 敏感词表
CREATE TABLE sensitive_word (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '敏感词id',
    word            varchar(255)                                NOT NULL    COMMENT '敏感词'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='敏感词表';

-- 帖子图片表
CREATE TABLE image (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '帖子图片id',
    url             varchar(255)                                NOT NULL    COMMENT '图片url'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='帖子图片表';

-- 评论表
CREATE TABLE comment (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '评论id',
    post_id         int                                         NOT NULL    COMMENT '帖子id',
    user_id         int                                         NOT NULL    COMMENT '用户id',
    context         text                                        NOT NULL    COMMENT '评论内容',
    create_time     datetime        default CURRENT_TIMESTAMP   NOT NULL    COMMENT '创建时间',
    update_time     datetime        default CURRENT_TIMESTAMP   NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted      tinyint         default 0                   NOT NULL    COMMENT '是否删除（0:不删除;1:删除）',
    status          tinyint         default 1                   NOT NULL    COMMENT '评论状态（0:已下架;1:正常）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='评论表';

-- 举报表
CREATE TABLE reports (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '举报id',
    reporter_id     int                                         NOT NULL    COMMENT '举报人id',
    reason          text                                        NOT NULL    COMMENT '举报理由',
    object_id       int                                         NOT NULL    COMMENT '举报对象id',
    object_type     tinyint                                     NOT NULL    COMMENT '举报对象类型（1:评论;2:帖子）',
    status          tinyint         default 1                   NOT NULL    COMMENT '举报状态（1:待处理;2:已处理）',
    create_time     datetime        default CURRENT_TIMESTAMP   NOT NULL    COMMENT '举报时间',
    result          varchar(255)                                            COMMENT '处理结果'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='举报表';

-- 帖子表
CREATE TABLE post (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '帖子id',
    user_id         int                                         NOT NULL    COMMENT '用户id',
    title	        varchar(255)	                            NOT NULL    COMMENT '帖子标题',
    context         text                                        NOT NULL    COMMENT '帖子内容',
    image_id	    varchar(255)	                            NOT NULL    COMMENT '图片id（eg:1;2;3）',
    create_time     datetime        default CURRENT_TIMESTAMP   NOT NULL    COMMENT '创建时间',
    update_time     datetime        default CURRENT_TIMESTAMP   NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted      tinyint         default 0                   NOT NULL    COMMENT '是否删除（0:不删除;1:删除）',
    status          tinyint         default 1                   NOT NULL    COMMENT '帖子状态（0:已下架;1:正常）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='帖子表';

-- 价格信息表
CREATE TABLE citrus_price (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '价格信息id',
    `date`          varchar(255)                                NOT NULL    COMMENT '时间',
    category        varchar(255)                                NOT NULL    COMMENT '种类',
    region          varchar(255)                                NOT NULL    COMMENT '地区',
    price           varchar(255)                                NOT NULL    COMMENT '价格',
    lift            varchar(255)                                NULL        COMMENT '升降'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='价格信息表';

-- 价格信息表
CREATE TABLE citrus_news (
    id              int         AUTO_INCREMENT                  PRIMARY KEY COMMENT '价格信息id',
    `date`          varchar(255)                                NOT NULL    COMMENT '日期',
    title           varchar(255)                                NOT NULL    COMMENT '新闻标题',
    url             varchar(255)                                NOT NULL    COMMENT '新闻链接'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='价格信息表';
