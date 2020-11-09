package com.fanhf.annotationtest.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fanhf
 * @Description 结果返回的统一对象
 * @date 2020-11-09 12:45
 */

@Data
public class Result<T> implements Serializable {
    /**
     * 通信数据
     */
    private T data;

    /**
     * 通过静态方法获取实例
     */
    public static <T> Result<T> of(T data) {
        return new Result<>(data);
    }


    @Deprecated
    public Result() {
    }

    private Result(T data) {
        this.data = data;
    }
}