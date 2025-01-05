package com.hzzzzzy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
     * 需求日期 mm月-mm月
     */
    private String requireTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
