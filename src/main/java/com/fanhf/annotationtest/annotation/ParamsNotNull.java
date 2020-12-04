package com.fanhf.annotationtest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fanhf
 * @Date 2020/11/16 10:19
 **/

@Target(value = ElementType.PARAMETER)
@Retention(value = RetentionPolicy.RUNTIME)

/*
 * Target 和 Retention都是元注解，也就是注解的注解
 * Target 是注解的范围，ElementType.FIELD是作用在属性上
 * Retention ：注解的生命周期，RUNTIME运行时，表示运行的时候注解都是起作用的
 *
 **/

public @interface ParamsNotNull {
}

