package com.hzzzzzy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 资讯信息表
 * @TableName citrus_consultation
 */
@TableName(value ="citrus_consultation")
@Data
public class CitrusConsultation implements Serializable {
    /**
     * 资讯信息id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String context;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
