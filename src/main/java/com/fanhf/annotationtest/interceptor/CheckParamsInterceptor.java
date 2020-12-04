package com.fanhf.annotationtest.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fanhf.annotationtest.annotation.ParamsNotNull;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;


/**
 * @author fanhf
 * @Description https://blog.csdn.net/weixin_37535975/article/details/94443617?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.channel_param&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.channel_param
 * @date 2020-11-16 10:37
 */
public class CheckParamsInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean  preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        List<String> list =  getParamsName((HandlerMethod)handler);
        for(String s: list){
            String parameter  = request.getParameter(s);
            if(StringUtils.isEmpty(parameter)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("status",203);
                jsonObject.put("msg","缺少必要的"+s+"参数");
                response.setHeader("Content-type","application/json;charset=UTF-8");
                //跨域
                response.setHeader("Access-Control-Allow-Origin","*");
                response.getWriter().write(jsonObject.toJSONString());
                return false;
            }
        }
        return  true;
    }

    private List<String> getParamsName(HandlerMethod handler) {
        Parameter[] parameters = handler.getMethod().getParameters();
        List<String> list = new ArrayList<>();
        for(Parameter parameter:parameters){
           if( parameter.isAnnotationPresent(ParamsNotNull.class)){
               list.add(parameter.getName());
           }
        }
        return  list;
    }
}
