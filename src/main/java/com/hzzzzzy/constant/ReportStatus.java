package com.hzzzzzy.constant;

/**
 * @author hzzzzzy
 * @date 2025/2/7
 * @description
 */
public enum ReportStatus {


	/**
	 * 举报状态: 待处理
	 */
	PENDING(1, "待处理"),

	/**
	 * 举报状态: 已处理
	 */
	PROCESSED(2, "已处理");


	/**
	 * 类型
	 */
	private final Integer type;

	/**
	 * 描述
	 */
	private final String desc;


	ReportStatus(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public Integer getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	// 判断是否有以上的status
	public static boolean containsStatus(Integer type){
		if (PENDING.getType() != type && PROCESSED.getType() != type){
			return false;
		}
		return true;
	}
}
