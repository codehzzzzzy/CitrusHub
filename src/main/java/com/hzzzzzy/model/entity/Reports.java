package com.hzzzzzy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 举报表
 * @TableName reports
 */
@TableName(value ="reports")
@Data
public class Reports implements Serializable {
    /**
     * 举报id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 举报人id
     */
    private Integer reporterId;

    /**
     * 举报理由
     */
    private String reason;

    /**
     * 举报对象id
     */
    private Integer objectId;

    /**
     * 举报对象类型（1:评论;2:帖子）
     */
    private Integer objectType;

    /**
     * 举报状态（1:待处理;2:已处理）
     */
    private Integer status;

    /**
     * 举报时间
     */
    private Date createTime;

    /**
     * 处理结果
     */
    private String result;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
