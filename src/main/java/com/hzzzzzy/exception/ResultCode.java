package com.hzzzzzy.exception;

/**
 * @author hzzzzzy
 * @create 2023/4/1
 * @description 状态码
 */
public interface ResultCode {
    /**
     * 获取结果状态编号
     *
     * @return 结果状态编号
     */
    default Integer getCode() {
        return 2000;
    }

    /**
     * 获取结果状态简短描述信息
     *
     * @return 状态描述
     */
    default String getDesc() {
        return "success";
    }
}
