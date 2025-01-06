package com.hzzzzzy.utils;


import com.hzzzzzy.model.dto.UserDTO;

/**
 * 用户与ThreadLocal中存取的工具类
 */
public class User2ThreadLocalUtils {
    private static final ThreadLocal<UserDTO> thread = new ThreadLocal<>();
    public static void saveUser(UserDTO user){
        thread.set(user);
    }
    public static UserDTO getUser(){
        return thread.get();
    }
    public static void removeUser(){
        thread.remove();
    }
}
