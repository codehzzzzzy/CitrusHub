package com.hzzzzzy.constant;

/**
 * @author hzzzzzy
 * @date 2025/1/10
 * @description 用户类型（1:管理员;2:用户;3:专家）
 */
public enum UserType {

    ADMIN(1),
    USER(2),
    EXPERT(3);

    private final int type;

    UserType(int type) {
        this.type = type;
    }

    public int getValue() {
        return type;
    }
}
