package com.fanhf.annotationtest.interceptor;

import com.fanhf.annotationtest.annotation.sqlannotation.CreateTime;
import com.fanhf.annotationtest.annotation.sqlannotation.Deleted;
import com.fanhf.annotationtest.annotation.sqlannotation.UpdateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Properties;


@Slf4j
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class DBTimeInterceptor implements Interceptor {

    @Override
    public java.lang.Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object params = invocation.getArgs()[1];
        if(null != params && (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType))){
           Object list = ((DefaultSqlSession.StrictMap)params).get("list");
           if(Objects.nonNull(list) && list instanceof List){
              List<Object> objects = (List<Object>) list;
               objects.forEach( ob ->{
                  setTime(sqlCommandType,ob,invocation);
               });
           }
        }
        return null;
    }

    private void setTime(SqlCommandType sqlCommandType, Object ob, Invocation invocation) {
        /**
         *   对数据库表的deleted字段值进行赋值处理
         **/

        Field deleted  = null;
        try {
            deleted = ob.getClass().getDeclaredField("deleted");
        } catch (NoSuchFieldException e) {
            log.error("{}对象中没有deleted字段",invocation.getClass().getName());
            e.printStackTrace();
        }
        if(Objects.nonNull(deleted) && Objects.nonNull(deleted.getAnnotation(Deleted.class))){
            deleted.setAccessible(true);
            try {
                deleted.set(ob,1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        /**
         *   对updateTime字段值进行赋值处理
         **/
        Field updateTime = null;
        try {
             updateTime = ob.getClass().getDeclaredField("updateTIme");
        } catch (NoSuchFieldException e) {
            log.error("{}对象没有updateTime字段",invocation.getClass().getName());
            e.printStackTrace();
        }
        if(Objects.nonNull(updateTime) && Objects.nonNull(updateTime.getAnnotation(UpdateTime.class))){
            try {
                updateTime.setAccessible(true);
                updateTime.set(ob,getCurrentSecond());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        /**
         *   对数据库表的createTime字段值进行插入时赋值处理
         **/
        if(SqlCommandType.INSERT.equals(sqlCommandType)){
           Field createTime = null;
            try {
                createTime = ob.getClass().getDeclaredField("createTime");
            } catch (NoSuchFieldException e) {
                log.error("{}对象没有createTime字段",invocation.getClass().getName());
                e.printStackTrace();
            }
            if(Objects.nonNull(createTime) && Objects.nonNull(createTime.getAnnotation(CreateTime.class))){
                try {
                    createTime.setAccessible(true);
                    createTime.set(ob,getCurrentSecond());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public java.lang.Object plugin(java.lang.Object o) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public static Integer getCurrentSecond() {
        Long ms = System.currentTimeMillis() / 1000L;
        return ms.intValue();
    }
}
