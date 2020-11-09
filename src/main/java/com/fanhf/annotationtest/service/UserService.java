package com.fanhf.annotationtest.service;

import com.fanhf.annotationtest.bean.UserBean;
import org.springframework.stereotype.Service;

/**
 * @author fanhf
 * @Description 用户业务层
 * @date 2020-11-09 12:41
 */
@Service
public class UserService {

    public UserBean updatePasswd(UserBean userBean) {
        userBean.setPasswd("@"+userBean.getPasswd()+"@");
        userBean.setAge(userBean.getAge()+1);
        userBean.setName(userBean.getName()+"AOP");
        return  userBean;
    }
}
