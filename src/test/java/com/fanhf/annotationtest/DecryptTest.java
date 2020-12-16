package com.fanhf.annotationtest;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * @author fanhf
 * @Description 给金保信测试同事用来解密的工具类
 * @date 2020-11-24 14:46
 */
public class DecryptTest {
    //私钥可以从RsaUtil中获取
    private static String PRIVATEKEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIU8CXjrNvE5Gi1z+2teFs6EFQ1VWb4ucIzBF75RI3t87iLvWCOe+pNJnZLRI5LRbkbHgnKya18bTnXCkXI1WoGZlXNDpMRjfevH7Fp5IVMssGe7jKnUf0dUPwx4g9KpCPXpgOcP61s4FV7wp1mCW6/SRJ934hXBUxFsTYdxW2m5AgMBAAECgYAoZ7urqxGfeCTDMhBGUGN0P8QupfqwXV8OoKR1uB+dnJaPi6xcKSNutX2O/9Pc+5yI5IpgBFHhpuNVfSskyp9mjCjGoObZba4bqsFRYZCTJb6skvTcN++WMftJCzPYUq/CzEsX6z+sarSRnel4RbX0nGc3v30h4zxRmmFkPZhB3QJBAMIH38o6S4c4vmuuyvnHxP7PfR1OP30gY62gRu9zdMYPqsDpDdgP72bpnvCY3ZXZxPq9pDg7xKxhuMvePMJGaOcCQQCvyWijTBHjCdhZyxpGBO8nA/xpLvTfrLYpmImBh7/WA9LXUY6O8ovEyvzNicLQ2aEikj/7lzP64gHx3YcodiRfAkBZ/EDjjumcSsq1MhlcgjNwYVYLE1EKfMz9pwl+37LWcEmmse8Dt1A2ED7wIlURvC9Igk747W8m6yNDGUxgZTsdAkAGkv7B1K96kTyxJwJlifuzttdyy5nG37u0VIsX+A65n0z8VoWBXRrTElBgE33gt7LqCxQvgZklEdzGweUzIy/7AkEAgVhqRTrSm5dYxPvRbkRMhwesIoyI99+ybKV69aqEb3qATH7386TIBSFAK9wdJ9Nk28m1kIBuG+n/nFNzVjItxA==";
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();
    static String iv = "d22b0a851e014f7b";


    public static void  main(String[] args) throws Exception {
        String data ="c0MOuWHIDgDRY7xNaHGzDA==";
        String rsaKey ="G1NVXTB+RShT7y6PFNFnc/l1E3x0anEHOxAWBt7lpjtDPcHaV/82esD548zbCckAb54qCKs8CZFe\n" +
                "iVBaDLjRIkdoWzoQbk4CA0eNPeHczdQzTejZFTbFS766HcTBPG3L+2AgypivhqAgWqfppo/g/tZjYPoG0rJiijqGFM9QqAY=";
        String rsaKeyDe = rsaDecrypt(rsaKey,PRIVATEKEY);
        String decryptData = doAES(data, rsaKeyDe, iv.getBytes("UTF-8"), 2);
        System.out.println("decryptData:"+decryptData);
    }

    /**
     * 给测试同事调用的解密的方法
     *
     **/

    public EnResult exec(String data, String rsaKey){
        EnResult result = new EnResult();
        String decryptData = null;
        try {
            String rsaKeyDe = rsaDecrypt(rsaKey,PRIVATEKEY);
            decryptData = doAES(data, rsaKeyDe, iv.getBytes("UTF-8"), 2);
            result.setData(decryptData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * ===========================================下面是AES的方法======================================================
     **/
    public static PrivateKey getPrivateKey(String key) throws Exception {
        BASE64Decoder base64 = new BASE64Decoder();
        byte[] keyBytes = base64.decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static String rsaDecrypt(String sourceStr, String privateKeyStr) throws Exception {
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] byteSources = decoder.decodeBuffer(sourceStr);
        byte[] byteDest = decrypt(byteSources, privateKey);
        String destStr = new String(byteDest, "UTF-8");
        return destStr;
    }

    public static byte[] decrypt(byte[] source, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(2, privateKey);
        byte[] dest = cipher.doFinal(source);
        return dest;
    }

    /**
     * ===========================================下面是RSA的方法======================================================
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

}   
