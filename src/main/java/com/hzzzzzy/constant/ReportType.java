package com.hzzzzzy.constant;

/**
 * @author hzzzzzy
 * @date 2025/1/24
 * @description
 */
public enum ReportType {

	/**
	 * 类型：评论
	 */
	COMMENT(1, "评论"),

	/**
	 * 类型：帖子
	 */
	POST(2, "帖子");

	/**
	 * 类型
	 */
	private final Integer type;

	/**
	 * 描述
	 */
	private final String desc;

	ReportType(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public Integer getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	// 判断是否有以上的type
	public static boolean containsType(Integer type){
		if (COMMENT.getType() != type && POST.getType() != type){
			return false;
		}
		return true;
	}

}
