package com.hzzzzzy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 供需信息表
 * @TableName citrus_supply_demand
 */
@TableName(value ="citrus_supply_demand")
@Data
public class CitrusSupplyDemand implements Serializable {
    /**
     * 供需信息id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 供应/求购
     */
    private String type;

    /**
     * 柑橘种类
     */
    private String category;

    /**
     * 地区
     */
    private String region;

    /**
     * 发布日期 mm-dd
     */
    private String releaseDate;

    /**
     * 需求日期-前
     */
    private Integer requireTimePre;

    /**
     * 需求日期-后
     */
    private Integer requireTimeAfter;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
