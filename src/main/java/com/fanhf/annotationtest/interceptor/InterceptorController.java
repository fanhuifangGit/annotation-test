package com.fanhf.annotationtest.interceptor;

import com.fanhf.annotationtest.annotation.ParamsNotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fanhf
 * @Description TODO
 * @date 2020-11-16 12:43
 */
@RestController
@RequestMapping("/test")
public class InterceptorController {
    @RequestMapping(path = "/{userId}")
//    @PostMapping(value = "/test")
    //注解不要加，在参数类型前边加上
    public String test(@ParamsNotNull String userId){
        return  "ok";
    }

}   
