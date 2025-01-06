package com.hzzzzzy.model.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author hzzzzzy
 * @description 存储在ThreadLocal
 */
@Data
public class UserDTO implements Serializable {
    /**
     * 用户id
     */
    private Integer id;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;
}
