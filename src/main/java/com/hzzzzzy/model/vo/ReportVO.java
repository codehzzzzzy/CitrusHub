package com.hzzzzzy.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hzzzzzy
 * @date 2025/1/24
 * @description
 */
@Data
public class ReportVO {

	@ApiModelProperty("举报主键id")
	private Integer id;

	@ApiModelProperty("举报用户id")
	private Integer reporterId;

	@ApiModelProperty("举报用户头像")
	private String reporterAvatar;

	@ApiModelProperty("举报理由")
	private String reason;

	@ApiModelProperty("举报对象")
	private Object object;

	@ApiModelProperty("举报状态（1:待处理;2:已处理）")
	private Integer status;

	@ApiModelProperty("举报对象类型（1:用户;2:评论;3:帖子）")
	private Integer objectType;

	@ApiModelProperty("举报时间")
	@JsonFormat(pattern = "yyyy年MM月dd日 HH时mm分ss秒", timezone = "GMT+8")
	private Date createTime;
}
