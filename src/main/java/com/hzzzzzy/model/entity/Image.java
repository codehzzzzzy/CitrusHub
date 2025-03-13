package com.hzzzzzy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 帖子图片表
 * @TableName image
 */
@TableName(value ="image")
@Data
public class Image {
    /**
     * 帖子图片id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 图片url
     */
    private String url;
}