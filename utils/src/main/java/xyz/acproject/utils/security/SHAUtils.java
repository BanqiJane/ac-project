package xyz.acproject.utils.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import xyz.acproject.utils.io.ByteUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Jane
 * @ClassName SHA256Utils
 * @Description TODO
 * @date 2021/6/18 10:31
 * @Copyright:2021
 */
public class SHAUtils {


    public static String SHA1(String str,String charsetName) {
        if(StringUtils.isBlank(charsetName)){
            charsetName = "UTF-8";
        }
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(str.getBytes(charsetName));
            encodeStr = ByteUtils.bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    public static String SHA256(String str,String charsetName) {
        if(StringUtils.isBlank(charsetName)){
            charsetName = "UTF-8";
        }
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(charsetName));
            encodeStr = ByteUtils.bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey,String charsetName) throws Exception {
        if(StringUtils.isBlank(charsetName))charsetName = "utf-8";

        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKeySpec secretKey = new SecretKeySpec(encryptKey.getBytes(charsetName), "HmacSHA1");
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance("HmacSHA1");
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        // 完成 Mac 操作
        return mac.doFinal(encryptText.getBytes(charsetName));
    }

    public static byte[]  HmacSignBase64(String content,String key,String hamaAlgorithm,String charset){
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(charset), hamaAlgorithm);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(hamaAlgorithm);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac;
            rawHmac = mac.doFinal(content.getBytes(charset));
            result = Base64.encodeBase64(rawHmac);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }  catch (IllegalStateException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null != result) {
            return result;
        } else {
            return null;
        }
    }

    public static byte[]  HmacSign(String content,String key,String hamaAlgorithm,String charset){
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(charset), hamaAlgorithm);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(hamaAlgorithm);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac;
            rawHmac = mac.doFinal(content.getBytes(charset));
            result = rawHmac;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }  catch (IllegalStateException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null != result) {
            return result;
        } else {
            return null;
        }
    }

}
