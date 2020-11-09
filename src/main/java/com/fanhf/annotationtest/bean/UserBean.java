package com.fanhf.annotationtest.bean;

/**
 * @author fanhf
 * @Description 要加密的bean类
 * @date 2020-11-09 12:43
 */
public class UserBean {
    private String passwd;
    private String name;
    private Integer age;

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
