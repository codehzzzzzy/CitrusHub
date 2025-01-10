package com.hzzzzzy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.constant.RedisConstant;
import com.hzzzzzy.constant.UserType;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.dto.UserAltMsgRequest;
import com.hzzzzzy.model.dto.UserRegisterRequest;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.entity.User;
import com.hzzzzzy.model.vo.ExpertVO;
import com.hzzzzzy.service.UserService;
import com.hzzzzzy.mapper.UserMapper;
import com.hzzzzzy.utils.JwtUtil;
import com.hzzzzzy.utils.ListUtil;
import com.hzzzzzy.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static com.hzzzzzy.constant.CommonConstant.HEADER_TOKEN;
import static com.hzzzzzy.constant.RedisConstant.USER_LOGIN_TOKEN;
import static com.hzzzzzy.constant.RedisConstant.USER_LOGIN_TOKEN_EXPIRE;

/**
* @author hzzzzzy
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-01-05 17:57:58
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private static final String SALT = "hzzzzzy";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void register(UserRegisterRequest userRegisterRequest) {
        String account = userRegisterRequest.getAccount();
        String pwd = userRegisterRequest.getPassword();
        String checkPwd = userRegisterRequest.getCheckPassword();
        Integer type = userRegisterRequest.getType();
        // 校验
        if (StringUtils.isAnyBlank(account, pwd, checkPwd)) {
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("参数为空"));
        }
        if (account.length() < 4) {
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("用户账号过短"));
        }
        if (!(type >= 1 && type <= 3)){
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("用户类型错误"));
        }
        if (!pwd.equals(checkPwd)) {
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("两次输入的密码不一致"));
        }
        synchronized (account.intern()) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account", account);
            long count = this.count(queryWrapper);
            if (count > 0) {
                throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("账号重复"));
            }
            // 使用md5进行加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + pwd).getBytes());
            // 插入数据
            User user = new User();
            user.setAccount(account);
            user.setPassword(encryptPassword);
            user.setType(type);
            this.save(user);
        }
    }

    @Override
    public String login(String account, String password) {
        String token = "";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, account).eq(User::getPassword, encryptPassword);
        User user = this.getOne(queryWrapper);
        if(BeanUtil.isEmpty(user)){
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("账户或密码错误"));
        }else {
            try {
                token = JwtUtil.createToken(String.valueOf(user.getId()), user.getAccount());
                stringRedisTemplate.opsForValue().set(USER_LOGIN_TOKEN + token, JSONUtil.toJsonStr(user), USER_LOGIN_TOKEN_EXPIRE, TimeUnit.SECONDS);
            } catch (UnsupportedEncodingException e) {
                throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("存入token失败"));
            }
            return token;
        }
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = request.getHeader(HEADER_TOKEN);
        if (token == null){
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("token为空，登出失败"));
        }
        if (!stringRedisTemplate.delete(USER_LOGIN_TOKEN + token)) {
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("登出失败"));
        }
    }

    @Override
    public void alterPwd(HttpServletRequest request, String oldPassword, String newPassword, String verifyPassword) {
        String token = request.getHeader(HEADER_TOKEN);
        String jsonUser = stringRedisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_TOKEN + token);
        User user = JSONUtil.toBean(jsonUser, User.class);
        if(!user.getPassword().equals(DigestUtils.md5DigestAsHex((SALT + oldPassword).getBytes()))){
            throw new GlobalException(new Result<>().error(BusinessFailCode.OLD_PASSWORD_ERROR));
        }
        if(!newPassword.equals(verifyPassword)){
            throw new GlobalException(new Result<>().error(BusinessFailCode.REGISTER_PASSWORD_ERROR));
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());
        user.setPassword(encryptPassword);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getId,user.getId());
        this.update(user, userLambdaQueryWrapper);
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_LOGIN_TOKEN + token, JSONUtil.toJsonStr(user),USER_LOGIN_TOKEN_EXPIRE, TimeUnit.SECONDS);
    }

    @Override
    public void alterMsg(HttpServletRequest request, UserAltMsgRequest userAltMsgRequest) {
        String token = request.getHeader(HEADER_TOKEN);
        String jsonUser = stringRedisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_TOKEN + token);
        User user = JSONUtil.toBean(jsonUser, User.class);
        // 判断type
        if (user.getType() != UserType.EXPERT.getValue()){
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("用户类型不支持，只支持专家修改信息"));
        }
        user.setRemark(userAltMsgRequest.getRemark());
        user.setExpertise(userAltMsgRequest.getExpertise());
        this.updateById(user);
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_LOGIN_TOKEN + token, JSONUtil.toJsonStr(user),USER_LOGIN_TOKEN_EXPIRE, TimeUnit.SECONDS);
    }

    @Override
    public User getUserInfo(HttpServletRequest request) {
        String token = request.getHeader(HEADER_TOKEN);
        String jsonUser = stringRedisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_TOKEN + token);
        User user = JSONUtil.toBean(jsonUser, User.class);
        if(user == null){
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("token为空，获取用户信息失败"));
        }
        // 脱敏
        user.setPassword("");
        return user;
    }

    @Override
    public PageResult<ExpertVO> getExpertInfo(String expertise, Integer current, Integer pageSize) {
        List<User> userList = this.list(new LambdaQueryWrapper<User>()
                .eq(User::getType, UserType.EXPERT.getValue())
                .like(!StringUtils.isEmpty(expertise), User::getExpertise, expertise)
        );
        if (userList.isEmpty()){
            return null;
        }
        List<ExpertVO> voList = ListUtil.entity2VO(userList, ExpertVO.class);
        return PageUtil.getPage(voList, current, pageSize);
    }
}
