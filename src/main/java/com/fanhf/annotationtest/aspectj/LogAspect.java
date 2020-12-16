package com.fanhf.annotationtest.aspectj;

import com.alibaba.fastjson.JSON;
import com.fanhf.annotationtest.annotation.NoPrintLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @author fanhf
 * @Description TODO
 * @date 2020-12-09 10:49
 */
@Aspect
@Configuration
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    public LogAspect() {
    }
//com.fanhf.annotationtest
    @Pointcut("execution(* com.fanhf.annotationtest.service.*.*.*Controller.*(..))")
    public void oauthExecute() {
    }

    @Pointcut("execution(* com.fanhf.annotationtest.service.*.*.*Controller.*(..))")
    public void platformExecute() {
    }

    @Around("oauthExecute() || platformExecute()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        NoPrintLog noPrintLog = (NoPrintLog)signature.getMethod().getAnnotation(NoPrintLog.class);
        log.debug("==============null != noPrintLog：{}", null != noPrintLog);
        if (null != noPrintLog) {
            Object result = pjp.proceed();
            return result;
        } else {
            String className = pjp.getTarget().getClass().getName();
            String methodName = signature.getName();
            String[] argNames = ((MethodSignature)pjp.getSignature()).getParameterNames();
            StringBuffer sb = new StringBuffer();
            Object[] params = pjp.getArgs();
            log.debug("==============params.length={}", params.length);

            for(int i = 0; i < params.length; ++i) {
                try {
//                    if (params[i] instanceof EncryptData) {
//                        EncryptData ed = (EncryptData)params[i];
//                        sb.append(ed.getDecryptedBusinessData());
//                    } else if (!(params[i] instanceof MultipartFile)) {
//                        sb.append(argNames[i] + ":" + JSON.toJSON(params[i]));
//                    }
                } catch (Exception var14) {
                    var14.printStackTrace();
                    log.info("入参:解析异常{}", var14.getMessage());
                }
            }

            long start = System.currentTimeMillis();
            log.debug("{}", className);
            log.debug(className + "." + methodName + "入参->" + sb.toString());
            Object result = pjp.proceed();
            long end = System.currentTimeMillis();
            log.debug(className + "." + methodName + "出参->" + JSON.toJSONString(result));
            log.debug(className + "." + methodName + "耗时->" + (end - start) + "ms");
            return result;
        }
    }
}
