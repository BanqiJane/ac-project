package xyz.acproject.utils.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import xyz.acproject.utils.io.ByteUtils;
import xyz.acproject.utils.enums.AESInstance;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

/**
 * @author Jane
 * @ClassName AESUtils
 * @Description TODO
 * @date 2021/6/17 11:02
 * @Copyright:2021
 */
public class AESUtils {

    private final static Logger LOGGER = LogManager.getLogger(AESUtils.class);


    /**
     * 使用PKCS7Padding填充必须添加一个支持PKCS7Padding的Provider
     * 类加载的时候就判断是否已经有支持256位的Provider,如果没有则添加进去
     */
    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        Security.setProperty("crypto.policy","unlimited");
        System.setProperty("crypto.policy","unlimited");
    }

    /**
     * 获得密钥
     *
     * @param secretKey
     * @return
     * @throws Exception
     */
    private static SecretKey generateKey(String secretKey) throws Exception {
        //防止linux下 随机生成key
        Provider p = Security.getProvider("SUN");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", p);
        secureRandom.setSeed(secretKey.getBytes());
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(secureRandom);
        // 生成密钥
        return kg.generateKey();
    }

    /**
     * 加密 128位
     *
     * @param content 需要加密的原内容
     * @param pkey    密匙
     * @return
     */
    public static byte[] aesEncrypt(String content, byte[] pkey, AESInstance instance) {
        try {
            //SecretKey secretKey = generateKey(pkey);
            //byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec skey = new SecretKeySpec(pkey, "AES");
            Cipher cipher = Cipher.getInstance(instance.getInstance(),"BC");// "算法/加密/填充"
            if(StringUtils.isNotBlank(instance.getIv())&&instance==AESInstance.CBC) {
                IvParameterSpec iv = new IvParameterSpec(instance.getIv().getBytes());
                cipher.init(Cipher.ENCRYPT_MODE, skey,iv);//初始化加密器
            }else{
                cipher.init(Cipher.ENCRYPT_MODE, skey);
            }
            byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
            return encrypted; // 加密
        } catch (Exception e) {
            LOGGER.error("aesEncrypt() method error:", e);
        }
        return null;
    }



    /**
     * 解密 128位
     *
     * @param content 解密前的byte数组
     * @param pkey    密匙
     * @return result  解密后的byte数组
     * @throws Exception
     */
    public static byte[] aesDecode(byte[] content, byte[] pkey, AESInstance instance) {

        //SecretKey secretKey = generateKey(pkey);
        //byte[] enCodeFormat = secretKey.getEncoded();
        try {
            SecretKeySpec skey = new SecretKeySpec(pkey, "AES");
            Cipher cipher = Cipher.getInstance(instance.getInstance(), "BC");// 创建密码器
            if (StringUtils.isNotBlank(instance.getIv()) && instance == AESInstance.CBC) {
                IvParameterSpec iv = new IvParameterSpec(instance.getIv().getBytes(Charset.forName("utf-8")));
                cipher.init(Cipher.DECRYPT_MODE, skey, iv);// 初始化解密器
            } else {
                cipher.init(Cipher.DECRYPT_MODE, skey);// 初始化解密器
            }
            byte[] result = cipher.doFinal(content);
            return result;
        }catch (Exception e){
            LOGGER.error("aesDecode() method error:", e);
        }
        return null; // 解密
    }

    /**
     * @param content 加密前原内容
     * @param pkey    长度为16个字符,128位   密匙
     * @return base64EncodeStr   aes加密完成后内容
     * @throws
     * @Title: aesEncryptStr
     * @Description: aes对称加密
     */
    public static String aesEncryptStr(String content, String pkey, AESInstance instance) {
        byte[] aesEncrypt = aesEncrypt(content, pkey.getBytes(),instance);
        String base64EncodeStr = Base64.encodeBase64String(aesEncrypt);
        return base64EncodeStr;
    }

    public static String aesEncryptBytes(String content,byte[] pkey, AESInstance instance) {
        byte[] aesEncrypt = aesEncrypt(content, pkey,instance);
        String base64EncodeStr = Base64.encodeBase64String(aesEncrypt);
        return base64EncodeStr;
    }

    /**
     * @param content base64处理过的字符串
     * @param pkey    密匙
     * @return String    返回类型
     * @throws Exception
     * @throws
     * @Title: aesDecodeStr
     * @Description: 解密 失败将返回NULL
     */
    public static String aesDecodeStr(String content, String pkey, AESInstance instance) {
        String result = null;
        try {
            byte[] base64DecodeStr = Base64.decodeBase64(content);
            byte[] aesDecode = aesDecode(base64DecodeStr, pkey.getBytes(),instance);
            if (aesDecode == null) {
                return null;
            }
            result = new String(aesDecode, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("aesDecode() method error:", e);
        }
        return result;
    }

    public static String aesDecodeBytes(String content, byte[] pkey, AESInstance instance) {
        String result = null;
        try {
            byte[] base64DecodeStr = Base64.decodeBase64(content);
            byte[] aesDecode = aesDecode(base64DecodeStr, pkey,instance);
            if (aesDecode == null) {
                return null;
            }
            result = new String(aesDecode, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("aesDecode() method error:", e);
        }
        return result;
    }


    public static void main(String[] args) throws Exception {
        //明文
        String content = "{\"accDate\":null,\"acqFee\":null,\"amount\":null,\"bankType\":null,\"batchNo\":null,\"errorMessage\":null,\"hostDate\":null,\"hostTime\":null,\"iRspRef\":null,\"issFee\":null,\"merchantID\":null,\"merchantRemarks\":null,\"notifyType\":null,\"orderNo\":null,\"payType\":null,\"returnCode\":null,\"success\":false,\"thirdOrderNo\":null,\"trnxNo\":null,\"trxType\":null,\"voucherNo\":null}";
        //密匙 16 或32位
        String pkey = "wwwwwWwwwwwwwww1wwwwwAAAwwwwwww1";
        //演示hex
        String hex = "E188F579F6BF52B7B382EF56DF07DCF80523E7FD3C578E4BE2F1F1526CCB8A9F";
        byte[] bytes2 = ByteUtils.hexToByteArray(hex);
        System.out.println("待加密报文:" + content);
        System.out.println("密匙:" + pkey);
        String aesEncryptStr = aesEncryptBytes(content, pkey.getBytes(),AESInstance.CBC);
        System.out.println("加密报文:" + aesEncryptStr);
        String aesDecodeStr = aesDecodeBytes(aesEncryptStr,  pkey.getBytes(),AESInstance.CBC);
        System.out.println("解密报文:" + aesDecodeStr);
        System.out.println("加解密前后内容是否相等:" + aesDecodeStr.equals(content));
    }
}
