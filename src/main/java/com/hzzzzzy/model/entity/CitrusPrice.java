package com.hzzzzzy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 价格信息表
 * @TableName citrus_price
 */
@TableName(value ="citrus_price")
@Data
public class CitrusPrice implements Serializable {
    /**
     * 价格信息id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 时间
     */
    private Date date;

    /**
     * 种类
     */
    private String category;

    /**
     * 地区
     */
    private String region;

    /**
     * 价格
     */
    private String price;

    /**
     * 升降
     */
    private String lift;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
