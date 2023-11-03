package com.jiawa.wiki.req;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserResetPasswordReq {


    @NotNull(message = "【密码】不能为空")
    //@Length(min = 6, max = 20, message = "【密码】6~20位")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "【密码】至少包含数字和英文，长度6-32")
    private String password;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserResetPasswordReq{" +
                "password='" + password + '\'' +
                '}';
    }
}