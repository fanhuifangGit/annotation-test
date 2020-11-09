package com.fanhf.annotationtest.controller;

import com.alibaba.fastjson.JSONObject;
import com.fanhf.annotationtest.annotation.Decrypt;
import com.fanhf.annotationtest.annotation.Encrypt;
import com.fanhf.annotationtest.bean.EncryptBean;
import com.fanhf.annotationtest.bean.Result;
import com.fanhf.annotationtest.bean.UserBean;
import com.fanhf.annotationtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fanhf
 * @Description 用户接口
 * @date 2020-11-09 12:42
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 对密码加密
     */
    @PostMapping(value = "/encrypt")
    @Encrypt
    public JSONObject encrypt(EncryptBean encryptBean) {
        return (JSONObject) JSONObject.toJSON(encryptBean);
    }

    /**POST
     * 对密码解密
     * 解密之后是用userBean来接
     */
    @PostMapping(value = "/decrypt")
    @Decrypt
    public JSONObject decrypt(UserBean userBean) {
        return (JSONObject) JSONObject.toJSON(userBean);
    }


    /**POST
     * 对密码解密后处理后再加密
     * 解密之后是用userBean来接
     */
    @PostMapping(value = "/ende")
    @Decrypt
    @Encrypt
    public Result<UserBean> ende(UserBean userBean) {
        UserBean userBeanNew = userService.updatePasswd(userBean);
        return Result.of(userBeanNew);
    }

    /**
     * @date 2020/11/6 17:58
     * @return JSONObject
     * 说明：这个接口是无法正常放回加密后的数据，原因是参数和返回值不一致
     **/

    @PostMapping(value = "/endeplus")
    @Decrypt
    @Encrypt
    public JSONObject endeplus(UserBean userBean) {
        UserBean userBeanNew = userService.updatePasswd(userBean);
        return (JSONObject) JSONObject.toJSON(userBeanNew);
    }
}
