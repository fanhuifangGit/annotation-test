package com.fanhf.annotationtest;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author fanhf
 * @Description TODO
 * @date 2020-11-23 16:41
 */
public class EncryptTest {
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();
    //公钥可以从AesUtil中获取
    private static final String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFPAl46zbxORotc/trXhbOhBUNVVm+LnCMwRe+USN7fO4i71gjnvqTSZ2S0SOS0W5Gx4JysmtfG051wpFyNVqBmZVzQ6TEY33rx+xaeSFTLLBnu4yp1H9HVD8MeIPSqQj16YDnD+tbOBVe8KdZgluv0kSfd+IVwVMRbE2HcVtpuQIDAQAB";
    static String iv = "d22b0a851e014f7b";

    public static void  main(String[] args) throws Exception {
        //{"name":"fanny","age":"18"}
//        String json = "{\"name\":\"fanny\",\"age\":\"18\"}";
        String json = "undefined";
        EncryptTest EncryptTest = new EncryptTest();
        EncryptTest.exec(json);
    }

    public EnResult exec(String json) {
        EnResult result = new EnResult();
        try {
            String aesKey = "S82Y4ou9qL6TJdFW";
            //使用AesUtil的encrypt方法对json进行加密
            String data = doAES(json, aesKey, iv.getBytes("UTF-8"), 1);
            result.setData(data);
            //  拿着公钥对aesKey进行加密
            String rsaKey = rsaEncrypt(aesKey, PUBLICKEY);
            result.setRsaKey(rsaKey);
            System.out.println("result:"+result);
            return result;
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    /**
     * ===========================================下面是AES的方法======================================================
     **/

    private static String doAES(String data, String secretKey, byte[] iv, int mode) {
        try {
            boolean encrypt = mode == 1;
            byte[] content;
            if (encrypt) {
                content = data.getBytes("UTF-8");
            } else {
                content = decoder.decode(data);
            }

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher.init(mode, skeySpec, new IvParameterSpec(iv));
            byte[] result = cipher.doFinal(content);
            return encrypt ? new String(encoder.encode(result)) : new String(result, "UTF-8");
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }


    /**
     * ===========================================下面是RSA的方法======================================================
     **/
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static String rsaEncrypt(String sourceStr, String publicKeyStr) throws Exception {
        PublicKey publicKey = getPublicKey(publicKeyStr);
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] byteSources = sourceStr.getBytes("UTF-8");
        byte[] byteDest = rasEncryptByte(byteSources, publicKey);
        String destStr = encoder.encode(byteDest);
        return destStr;
    }
    public static byte[] rasEncryptByte(byte[] source, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(1, publicKey);
        byte[] dest = cipher.doFinal(source);
        return dest;
    }
}   
