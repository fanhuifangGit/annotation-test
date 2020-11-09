package com.fanhf.annotationtest.bean;

/**
 * @author fanhf
 * @Description 加密后的bean字段
 * @date 2020-11-09 14:06
 */
public class EncryptBean {
    private String aesData;
    private String rsaData;

    public String getAesData() {
        return aesData;
    }

    public void setAesData(String aesData) {
        this.aesData = aesData;
    }

    public String getRsaData() {
        return rsaData;
    }

    public void setRsaData(String rsaData) {
        this.rsaData = rsaData;
    }
}
