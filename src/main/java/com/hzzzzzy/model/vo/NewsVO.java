package com.hzzzzzy.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hzzzzzy
 * @date 2025/1/11
 * @description
 */
@Data
public class NewsVO {

    @ApiModelProperty("新闻标题")
    String title;

    @ApiModelProperty("日期")
    String date;

    @ApiModelProperty("新闻链接")
    String url;
}
