package com.hzzzzzy.exception;

import com.hzzzzzy.model.entity.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hzzzzzy
 * @create 2023/4/1
 * @description 全局异常处理
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GlobalException extends RuntimeException {

    /**
     * 统一结果类
     */
    private Result<Object> result;

    public GlobalException(Result<Object> result) {
        super(result.getMessage());
        this.result = result;
    }

}
