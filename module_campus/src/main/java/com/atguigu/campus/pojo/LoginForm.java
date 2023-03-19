package com.atguigu.campus.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Linda
 * @version 1.0
 *
 * 用户登录表单信息
 * 将登录的信息封装到该类 便于获取
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {

    private String username;
    private String password;
    private String verifiCode;
    private Integer userType;
}
