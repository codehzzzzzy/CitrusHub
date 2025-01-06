package com.hzzzzzy.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.hzzzzzy.model.dto.UserDTO;
import com.hzzzzzy.model.entity.User;
import com.hzzzzzy.utils.User2ThreadLocalUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import static com.hzzzzzy.constant.CommonConstant.HEADER_TOKEN;
import static com.hzzzzzy.constant.RedisConstant.USER_LOGIN_TOKEN;

public class RefreshTokenInterceptor implements HandlerInterceptor {

    private static final int LOGIN_USER_TTL=30;

    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取请求头中的token
        String token = request.getHeader(HEADER_TOKEN);
        if (StrUtil.isBlank(token)) {
            return true;
        }
        String key  = USER_LOGIN_TOKEN + token;
        String jsonUser = stringRedisTemplate.opsForValue().get(key);
        User user = JSONUtil.toBean(jsonUser, User.class);
        if (BeanUtil.isEmpty(user)) {
            return true;
        }
        UserDTO UserDTO = BeanUtil.toBean(user,UserDTO.class);
        // 存在，保存用户信息到 ThreadLocal
        User2ThreadLocalUtils.saveUser(UserDTO);
        // 刷新有效期
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        // 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        User2ThreadLocalUtils.removeUser();
    }
}
