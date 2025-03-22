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
 * @TableName citrus_news
 */
@TableName(value ="citrus_news")
@Data
public class CitrusNews implements Serializable {
    /**
     * 价格信息id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    private Date date;

    /**
     * 新闻标题
     */
    private String title;

    /**
     * 新闻链接
     */
    private String url;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
