package com.hzzzzzy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 知识库分类表
 * @TableName knowledge_category
 */
@TableName(value ="knowledge_category")
@Data
public class KnowledgeCategory implements Serializable {
    /**
     * 分类id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类描述
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
