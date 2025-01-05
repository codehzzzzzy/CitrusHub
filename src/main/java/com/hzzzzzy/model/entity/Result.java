package com.hzzzzzy.model.entity;
import com.hzzzzzy.exception.ResultCode;
import lombok.Data;

/**
 * @author hzzzzzy
 * @create 2023/4/1
 * @description 统一结果返回类
 */
@Data
public class Result<T> {

    public static Integer BUSINESS_SUCCESS = 2000;
    public static Integer BUSINESS_FAIL = 3000;
    public static Integer SERVICE_EXCEPTION = 4000;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 返回码（'2xxx'为业务成功，'3xxx'为业务失败，'4xxx'为服务器异常）
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;


    public Result() {
    }

    public Result<T> success() {
        Result<T> r = this;
        r.setSuccess(true);
        r.setCode(BUSINESS_SUCCESS);
        r.setMessage("访问成功");
        return r;
    }

    public Result<T> fail() {
        Result<T> r = this;
        r.setSuccess(false);
        r.setCode(BUSINESS_FAIL);
        r.setMessage("访问失败");
        return r;
    }

    public Result<T> fail(ResultCode code) {
        Result<T> r = this;
        r.setSuccess(false);
        r.setCode(code.getCode());
        r.setMessage(code.getDesc());
        return r;
    }

    public Result<T> error() {
        Result<T> r = this;
        r.setSuccess(false);
        r.setCode(SERVICE_EXCEPTION);
        r.setMessage("访问异常");
        return r;
    }

    public Result<T> error(ResultCode code) {
        Result<T> r = this;
        r.setSuccess(false);
        r.setCode(code.getCode());
        r.setMessage(code.getDesc());
        return r;
    }

    public Result<T> success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public Result<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    public Result<T> code(ResultCode code) {
        this.setCode(code.getCode());
        return this;
    }

    public Result<T> data(T obj) {
        this.setData(obj);
        return this;
    }
}
