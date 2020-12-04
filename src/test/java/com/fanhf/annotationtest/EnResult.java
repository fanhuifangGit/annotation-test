package com.fanhf.annotationtest;

/**
 * @author fanhf
 * @Description TODO
 * @date 2020-11-23 16:41
 */
public class EnResult {
    private String data;
    private String rsaKey;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRsaKey() {
        return rsaKey;
    }

    public void setRsaKey(String rsaKey) {
        this.rsaKey = rsaKey;
    }

    @Override
    public String toString() {
        return "{" +
                "data='" + data + '\'' +
                ", rsaKey='" + rsaKey + '\'' +
                '}';
    }
}   
