package com.hzzzzzy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 对话信息表
 * @TableName citrus_chat
 */
@TableName(value ="citrus_chat")
@Data
public class CitrusChat implements Serializable {
    /**
     * 对话id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 对话名称
     */
    private String name;

    /**
     * 对话标识
     */
    private String threadSlug;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
