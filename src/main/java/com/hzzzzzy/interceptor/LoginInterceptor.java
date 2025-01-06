package com.hzzzzzy.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.hzzzzzy.model.dto.UserDTO;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.hzzzzzy.utils.User2ThreadLocalUtils.getUser;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserDTO userDTO = getUser();
        if(BeanUtil.isEmpty(userDTO)){
            response.setStatus(401);
            return false;
        }else {
            return true;
        }
    }
}
