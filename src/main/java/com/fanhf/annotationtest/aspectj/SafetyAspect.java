package com.fanhf.annotationtest.aspectj;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fanhf.annotationtest.annotation.Decrypt;
import com.fanhf.annotationtest.annotation.Encrypt;
import com.fanhf.annotationtest.bean.EncryptBean;
import com.fanhf.annotationtest.utils.AesUtil;
import com.fanhf.annotationtest.utils.RsaUtil;
import org.apache.commons.codec.binary.Base64;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


@Aspect
@Component
public class SafetyAspect {

    private static final Logger log = LoggerFactory.getLogger(SafetyAspectPlus.class);
    /**
     * 这里RSA加解密的时候需要根据公钥和私钥来进行加解密，真实的业务场景一般是从前端传过来公钥，私钥是随机获取的，我这里为了方便，就写死了。
     **/
    private static final String PRIVATEKEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALwT6AsfbEV/d5Mv+fbsaxjAKDPFmJu2JnV3b03T6RX1Oailg1tawbI3boR2PC1lOjRBHoqR8GPJGNu79NmnaZ9Fq5gqUZkO/K88/BdRgiqropEqLginYiiDJm22jqdFUb2VafwLj2ou5sevtbIR6fVou881k/YRsAImsA3WH7ITAgMBAAECgYAndmnOw6YdIvS8/mkNZWfHRrJowoIV0e9Z4FiLVPZoNA8IEspwBaf0s+rNgl14DPBcfHljC+ILnetIV7S1Yoonk5epsq7BjmrB7AqXazdXLv2Cmyw8CyGjs7ShhBZPn/oAmCWIQgObptDBGrdB9bZk1s7mSr96Z4fIw9/mLXuqsQJBAPI3mnjfVcr5A5KysZezqjljYGgf5cboP/t956H+LqMLkh2DC8nbiSoOy5etWOvoarRb6t8bWc4/crCXQvvTrIkCQQDGx6ZMoLURLz8KLCRKjpUt5Dx0jmu3utjPlmfLxpiAI0VvokWpFZrc4mlib/T4zDtIDFGzGBkSC7UhJ4339Nq7AkAGv6ncKEzZpOqGkdgE5AqgIray8ACU9C+kMDPd/ZkLDe16SQZxD17Y/ySJC1lo6Ubf05fNs5Ni/b2SUgSZw6IRAkAVbDjg80TwWC4sE3vJyToMmxdk3GCBiZKKNMR08q9GyAZYtJ1bTqfE/GWtJTG6ipAtAJ7hdUxmZHqd2xxyx6G3AkASaSgCdDxd7N6O6qZs0T5V2z9PzXj1exsUwIPUtVC1EnZ+OmklUBVqRHtqw+xSwomfcKvb7zx8T9L3oCgx6okj";
    private static final String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8E+gLH2xFf3eTL/n27GsYwCgzxZibtiZ1d29N0+kV9TmopYNbWsGyN26EdjwtZTo0QR6KkfBjyRjbu/TZp2mfRauYKlGZDvyvPPwXUYIqq6KRKi4Ip2IogyZtto6nRVG9lWn8C49qLubHr7WyEen1aLvPNZP2EbACJrAN1h+yEwIDAQAB";
    /**
     * Pointcut 切入点
     */
    @Pointcut("execution(public * com.fanhf.annotationtest.controller.UserController.encrypt(..))||" +
              "execution(public * com.fanhf.annotationtest.controller.UserController.decrypt(..))")

    public void safetyAspect() {}

    /**
     * 环绕通知
     */
    @Around(value = "safetyAspect()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            assert attributes != null;

            //request对象
            HttpServletRequest request = attributes.getRequest();

            //http请求方法  post get
            String httpMethod = request.getMethod().toLowerCase();

            //method方法
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();

            //method方法上面的注解
            Annotation[] annotations = method.getAnnotations();

            //方法的形参参数
            Object[] args = pjp.getArgs();

            //是否有@Decrypt
            boolean hasDecrypt = false;
            //是否有@Encrypt
            boolean hasEncrypt = false;
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == Decrypt.class) {
                    hasDecrypt = true;
                }
                if (annotation.annotationType() == Encrypt.class) {
                    hasEncrypt = true;
                }
            }

            InputStreamReader inputStreamReader = new InputStreamReader(request.getInputStream(), "UTF-8");
            StringBuilder stringBuilder;
            try (BufferedReader br = new BufferedReader(inputStreamReader)) {
                String line;
                stringBuilder = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
            String oridata = stringBuilder.toString();
            JSONObject jsonObject = JSONObject.parseObject(oridata);
            String line = prettyJson(oridata);
            log.info("入参的切面json串:\r\n{}", line);

            if ("post".equals(httpMethod) && hasDecrypt) {
                String data = null;
                if (oridata.indexOf("data") != -1) {
                    data = jsonObject.getString("data");
                    jsonObject = JSONObject.parseObject(data);
                }
                //AES加密后的数据
                String aesKey = jsonObject.getString("aesData");
                //后端RSA公钥加密后的AES数据
                String dataString = jsonObject.getString("rsaData");
                //后端私钥解密的到AES的key

                byte[] plaintext = RsaUtil.decryptByPrivateKey(Base64.decodeBase64(dataString), PRIVATEKEY);

                String rsaData = new String(plaintext);
                log.info("解密出来的AES生成的key为:{}", rsaData);

                String originData = AesUtil.decrypt(aesKey, rsaData);
                log.info("解密出来的data数据:\r\n{}", prettyJson(originData));
                if (args.length > 0) {
                    args[0] = JSONObject.parseObject(originData, args[0].getClass());
                }
            }

            if (hasEncrypt) {
                String  aesKey = AesUtil.getKey();
                log.info("AES的key:{}", aesKey);

                String  dataString = jsonObject.toJSONString();
                String aesData = AesUtil.encrypt(dataString, aesKey);
                log.info("AesUtil加密后的数据:{}", aesData);

                String rsaData = Base64.encodeBase64String(RsaUtil.encryptByPublicKey(aesKey.getBytes(), PUBLICKEY));
                log.info("RsaUtil加密后的data数据:{}", rsaData);

                EncryptBean encryptBean = new EncryptBean();
                encryptBean.setAesData(aesData);
                encryptBean.setRsaData(rsaData);

                log.info("加密后的数据为:{}", JSONObject.toJSONString(encryptBean));
                if (args.length > 0) {
                    args[0] = JSONObject.parseObject(JSONObject.toJSONString(encryptBean), args[0].getClass());
                }
            }

            return pjp.proceed(args);

        } catch (Throwable e) {
            log.error(e.getMessage());
            return JSONObject.parseObject("{ \"加解密异常\": \"" +e.getMessage() + "\"}");
        }
    }

    public static String prettyJson(String reqJson) {
        JSONObject object = JSONObject.parseObject(reqJson);
        return JSON.toJSONString(object, new SerializerFeature[]{SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat});
    }
}   
