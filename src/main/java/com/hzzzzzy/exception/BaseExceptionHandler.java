package com.hzzzzzy.exception;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.model.entity.Result;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


/**
 * @author hzzzzzy
 * @create 2023/4/1
 * @description 全局异常处理
 */
@Slf4j
@RestControllerAdvice
@Component
public class BaseExceptionHandler {

    /**
     * 拦截参数验证异常
     */
    @SneakyThrows
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError firstFieldError = CollectionUtil.getFirst(bindingResult.getFieldErrors());
        String exceptionStr = Optional.ofNullable(firstFieldError)
                .map(FieldError::getDefaultMessage)
                .orElse(StrUtil.EMPTY);
        log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request), exceptionStr);
        if (StrUtil.isNotBlank(exceptionStr)){
            return new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message(exceptionStr);
        }
        return new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("参数验证异常");
    }

    /**
     * 拦截未捕获异常
     */
    @ExceptionHandler(value = Throwable.class)
    public Result defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
        if(throwable.getMessage() != null){
            log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), throwable, throwable.getCause());
            return new Result<>().error().message(throwable.getMessage());
        }
        log.error("[{}] {} ", request.getMethod(), getUrl(request), throwable);
        return new Result<>().error().message("系统异常");
    }

    private String getUrl(HttpServletRequest request) {
        if (StringUtils.isEmpty(request.getQueryString())) {
            return request.getRequestURL().toString();
        }
        return request.getRequestURL().toString() + "?" + request.getQueryString();
    }
}
