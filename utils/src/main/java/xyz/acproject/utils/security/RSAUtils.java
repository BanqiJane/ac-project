package xyz.acproject.utils.security;

import org.apache.commons.lang3.StringUtils;
import xyz.acproject.utils.enums.RSAInstance;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Enumeration;


/**
 * @author Jane
 * @ClassName RSAUtils
 * @Description 简单的rst工具类
 * @date 2021/3/28 22:35
 * @Copyright:2021
 * @version  乱! 乱! 乱!
 */
public class RSAUtils {
    /**
     * 密钥长度 于原文长度对应 以及越长速度越慢
     */
    private final static int KEY_SIZE = 1024;

    public static String SIGN_INSTANCE  ="SHA1withRSA";

    public static String SIGN_INSTANCE256 = "SHA256withRSA";

    static {
        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
    }


    /**
     * 建立新的密钥对，返回打包的byte[]形式私钥和公钥
     *
     * @return 包含打包成byte[]形式的私钥和公钥的object[], 其中，object[0]为私钥byte[],object[1]为公钥byte[]
     */
    public static Object[] giveRSAKeyPairInByte() {
        KeyPair newKeyPair = creatmyKey();
        if (newKeyPair == null) {
            return null;
        }
        Object[] re = new Object[2];
        if (newKeyPair != null) {
            PrivateKey priv = newKeyPair.getPrivate();
            byte[] b_priv = priv.getEncoded();
            PublicKey pub = newKeyPair.getPublic();
            byte[] b_pub = pub.getEncoded();
            re[0] = b_priv;
            re[1] = b_pub;
            return re;
        }
        return null;
    }

    /**
     * 新建密钥对
     *
     * @return KeyPair对象
     */
    public static KeyPair creatmyKey() {
        KeyPair myPair;
        long mySeed;
        mySeed = System.currentTimeMillis();
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            random.setSeed(mySeed);
            keyGen.initialize(1024, random);
            myPair = keyGen.generateKeyPair();
        } catch (Exception e1) {
            return null;
        }
        return myPair;
    }

    /**
     * 随机生成密钥对
     */
    public static String[] genKeyPair() throws NoSuchAlgorithmException {
        String[] keys = new String[2];
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        //0表示私钥
        keys[0] = privateKeyString;
        //1表示公钥
        keys[1] = publicKeyString;
        return keys;
    }


    /**
     * 使用私钥加密数据
     * 用一个已打包成byte[]形式的私钥加密数据，即数字签名
     *
     * @param keyInByte 打包成byte[]的私钥
     * @param source    要签名的数据，一般应是数字摘要
     * @return 签名 byte[]
     */
    public static byte[] sign(byte[] keyInByte, byte[] source,String instance) {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(keyInByte);
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Signature sig = Signature.getInstance(instance);
            sig.initSign(privKey);
            sig.update(source);
            return sig.sign();
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] sign(byte[] keyInByte, byte[] source,String aliasName,String password,String instance) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            char[] pscs = password.toCharArray();
            keyStore.load(new ByteArrayInputStream(keyInByte), pscs);
            if(StringUtils.isBlank(aliasName)){
                Enumeration<String> enums = keyStore.aliases();
                if (enums.hasMoreElements())
                {
                    aliasName = (String) enums.nextElement();
                }
            }
            PrivateKey privKey = (PrivateKey) keyStore.getKey(aliasName,pscs);
            Signature sig = Signature.getInstance(instance);
            sig.initSign(privKey);
            sig.update(source);
            return sig.sign();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static byte[] sign(PrivateKey privateKey, byte[] source,String instance) {
        try {
            Signature sig = Signature.getInstance(instance);
            sig.initSign(privateKey);
            sig.update(source);
            return sig.sign();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证数字签名
     *
     * @param keyInByte 打包成byte[]形式的公钥
     * @param source    原文的数字摘要
     * @param sign      签名（对原文的数字摘要的签名）
     * @return 是否证实 boolean
     */
    public static boolean verify(byte[] keyInByte, byte[] source, byte[] sign,String instance) {
        try {
//            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
//            FileInputStream bais = new FileInputStream(abcPubCertPath);
//            X509Certificate cert = (X509Certificate)certificatefactory.generateCertificate(new ByteArrayInputStream(keyInByte));
//            PublicKey pubKey  = cert.getPublicKey();
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            Signature sig = Signature.getInstance(instance);
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(keyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            sig.initVerify(pubKey);
            sig.update(source);
            return sig.verify(sign);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verify(PublicKey publicKey, byte[] source, byte[] sign,String instance){
        try {
            Signature sig = Signature.getInstance(instance);
            sig.initVerify(publicKey);
            sig.update(source);
            return sig.verify(sign);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 负载的公钥
     *
     * @param modulus  模量
     * @param exponent 指数
     * @param radix    基数
     * @return {@link RSAPublicKey}
     */
    public static RSAPublicKey loadPublicKey(String modulus, String exponent, int radix){
        try {
            BigInteger mod = new BigInteger(modulus, radix);
            BigInteger exp = new BigInteger(exponent, radix);
            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(mod, exp);
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            RSAPublicKey R = (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
            return R;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 使用RSA公钥加密数据
     *
     * @param pubKeyInByte 打包的byte[]形式公钥
     * @param data         要加密的数据
     * @return 加密数据
     */
    public static byte[] encryptByRSAPublic(byte[] pubKeyInByte, byte[] data, RSAInstance instance) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance(instance.getInstance());
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用RSA公钥加密数据
     *
     * @param rsaPublicKey 打包的byte[]形式公钥
     * @param data         要加密的数据
     * @return 加密数据
     */
    public static byte[] encryptByRSAPublic(RSAPublicKey rsaPublicKey, byte[] data,RSAInstance instance) {
        try {
            Cipher cipher = Cipher.getInstance(instance.getInstance());
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 用RSA私钥解密
     *
     * @param privKeyInByte 私钥打包成byte[]形式
     * @param data          要解密的数据
     * @return 解密数据
     */
    public static byte[] decryptByRSAPriave(byte[] privKeyInByte, byte[] data,RSAInstance instance) {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(
                    privKeyInByte);
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Cipher cipher = Cipher.getInstance(instance.getInstance());
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }

    }


    /**
     * 使用RSA私钥加密数据
     *
     * @param privKeyInByte 打包的byte[]形式私钥
     * @param data          要加密的数据
     * @return 加密数据
     */
    public static byte[] encryptByRSAPrivate(byte[] privKeyInByte, byte[] data) {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(
                    privKeyInByte);
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            //
            Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 用RSA公钥解密
     *
     * @param pubKeyInByte 公钥打包成byte[]形式
     * @param data         要解密的数据
     * @return 解密数据
     */
    public static byte[] decryptByRSAPublic(byte[] pubKeyInByte, byte[] data) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * RSA公钥加密
     *
     * @param str    字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

//    /**
//     * RSA私钥加密
//     *
//     * @param str    字符串
//     * @param privateKey 公钥
//     * @return 密文
//     * @throws Exception 加密过程中的异常信息
//     */
//    public static String encryptPrivate(String str, String privateKey) throws Exception {
//        //base64编码的公钥
//        byte[] decoded = Base64.getDecoder().decode(privateKey);
//        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
//        //RSA加密
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, priKey);
//        String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
//        return outStr;
//    }

    /**
     * RSA私钥解密
     *
     * @param str    字符串
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.getDecoder().decode(str);
        //base64编码的私钥
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte),"utf-8");
        return outStr;
    }

//    /**
//     * RSA公钥解密
//     *
//     * @param str    字符串
//     * @param publicKey 公钥
//     * @return 明文
//     * @throws Exception 解密过程中的异常信息
//     */
//    public static String decryptPublic(String str, String publicKey) throws Exception {
//        //64位解码加密后的字符串
//        byte[] inputByte = Base64.getDecoder().decode(str);
//        //base64编码的私钥
//        byte[] decoded = Base64.getDecoder().decode(publicKey);
//        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
//        //RSA解密
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.DECRYPT_MODE, pubKey);
//        String outStr = new String(cipher.doFinal(inputByte),"utf-8");
//        return outStr;
//    }

    public static void main(String[] args) throws Exception {
        try {
            //私钥加密 公钥解密
            //生成私钥-公钥对
            Object[] v = giveRSAKeyPairInByte();

            String[] keys = genKeyPair();
            System.err.println(keys[0]);
            System.err.println(keys[1]);
            String vEncode = encrypt("1",keys[1]);
            System.out.println(vEncode);
            //获得摘要
//            byte[] source = MdigestSHA("假设这是要加密的客户数据");
            byte[] source = "test".getBytes();
            //使用私钥对摘要进行加密 获得密文 即数字签名
            byte[] sign = sign((byte[]) v[0], source,SIGN_INSTANCE);
            //使用公钥对密文进行解密,解密后与摘要进行匹配
            boolean yes = verify((byte[]) v[1], source, sign,SIGN_INSTANCE);
            if (yes) {
                System.out.println("匹配成功 合法的签名!");
            }
//            FileInputStream fileInputStream = new FileInputStream("E:/WorkSpace/Github/acproject/touch_ablottery/src/main/resources/ABC_OpenBank_ThridPart_Test.pfx");
//            FileInputStream fileInputStreamcert = new FileInputStream("E:/WorkSpace/Github/acproject/touch_ablottery/src/main/resources/ABC_Openbank_Sandbox.cer");
//            byte[] sign2 = sign(ByteUtils.parse(fileInputStream).toByteArray(),source,"111111",SIGN_INSTANCE256);
//            boolean yes1 = verify(ByteUtils.parse(fileInputStreamcert).toByteArray(), source, sign2,SIGN_INSTANCE256);
//            System.out.println("y:"+yes1);
//
//            //公钥加密私钥解密
//            //获得摘要
//            byte[] sourcepub_pri = ("13265986584||316494646546486498||01||public").getBytes("UTF-8");
//
//            //使用公钥对摘要进行加密 获得密文
//            byte[] signpub_pri = encryptByRSAPublic((byte[]) v[1], sourcepub_pri);
//            //System.out.println("公钥加密密文："+new String(Base64.encodeBase64(signpub_pri)));
//
//            //使用私钥对密文进行解密 返回解密后的数据
//            byte[] newSourcepub_pri = decryptByRSAPriave((byte[]) v[0], signpub_pri);
//
//            System.out.println("私钥解密：" + new String(newSourcepub_pri, "UTF-8"));
//            //对比源数据与解密后的数据
//            if (Arrays.equals(sourcepub_pri, newSourcepub_pri)) {
//                System.out.println("匹配成功 合法的私钥!");
//            }
//
//
//            //私钥加密公钥解密
//            //获得摘要
//            //byte[] sourcepri_pub = MdigestSHA("假设这是要加密的客户数据");
//            byte[] sourcepri_pub = ("13265986584||316494646546486498||01||private").getBytes("UTF-8");
//
//
//            //使用私钥对摘要进行加密 获得密文
//            byte[] signpri_pub = encryptByRSAPrivate((byte[]) v[0], sourcepri_pub);
//
//            //   System.out.println("私钥加密密文："+new String(Base64.encodeBase64(sign11)));
//            //使用公钥对密文进行解密 返回解密后的数据
//            byte[] newSourcepri_pub = decryptByRSAPublic((byte[]) v[1], signpri_pub);
//
//            System.out.println("公钥解密：" + new String(newSourcepri_pub, "UTF-8"));
//
//
//            String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEGENnf3rdiO20isoLQqezw12FoWXII9FBw8nR1MWQ3X0CVzOsqY1hOmxD/YI9OB7WVIaVax5tj1l+wk6A0v85Z4OpGWqz4B5L3fCUlBwf/M6DXHlSN1OZttvQF3OeWvc6gvJHihR7pp18zc4KfCJx0Ry6IrGH/2SNOVE1AIgvRQIDAQAB";
//            String PRIVATEKEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIQYQ2d/et2I7bSKygtCp7PDXYWhZcgj0UHDydHUxZDdfQJXM6ypjWE6bEP9gj04HtZUhpVrHm2PWX7CToDS/zlng6kZarPgHkvd8JSUHB/8zoNceVI3U5m229AXc55a9zqC8keKFHumnXzNzgp8InHRHLoisYf/ZI05UTUAiC9FAgMBAAECgYAGNcHNds/G5G4QY8n1149cwx19b8YCL7Thu5ucUr1q/w6mcoUKY/oyjPWUCLH7wMyqVNTy51NJ4UhazjW0lrbK4ZbPDHFij9CiZ7QFASiQ/TQWaL+KSIWnE6/rK9IdouwFKxk+cvvLteZoAXP6mFcrsa7LzfkENiIMu7mjpTNHAQJBANXv9U5JWOAVhWHDQcEWKn7YKpAVRleXdeUeJrXcdkqBDI+P6suA9j+ahDREfu+x65wUsrJotPHUXgJG0TarJIUCQQCeEPLrv6Qvi5+nbn2Eifn/fjsmIdI0U2WZKDHWJEnLsRUuGDNYxVE/SPDNDedA2OHeFB6j0Kk/ECdsWnUq6zvBAkAgUGViFMwa1MVX1fFZo+p5TFdpef0s/9Cr8djxAULQ0BtAmAFkCa+oPcOYTXxK4jnvUmUHc69ZE7W7bEzvj/wtAkB50X4mClAzBFxK4XCC0QOG0HYtcStbgFpwqvWdn+Hvxc4Y9DW+WHPBXimXHvv2ki+gw8jJX2rQW1bGvwBFz30BAkASPkORJxVWv91StjI2f/HXDO5eG5/su/XIb3eajaLUSEdaQlcs3ywLrrJ0o3VAR0J9aq59cmp6em017AMnmbF7";
//
//            byte[] signPrivate = Base64.getDecoder().decode(PRIVATEKEY.getBytes());
//            byte[] signPublic = Base64.getDecoder().decode(PUBLICKEY.getBytes());
//
//
//            String publicpwd = "N/b4nYbbLFVq0yTAIOpNNydtNQUCQxQy0B7bD6kzxLMW2guYxXtWOC/9Z5dpWecx/y7d5CezUJ6cf/8++msiNie4DcKBaFDFPh5rPbjeEB+DRfhjcdR2BsVGXWLsq3dLYLgZObQXG6Tb9rXakuH34Y+6KIIwCjiODH2QAU+PSiM=";
//            String privatepwd = "MTMyNjU5ODY1ODR8fDMxNjQ5NDY0NjU0NjQ4NjQ5OHx8MDF8fHByaXZhdGU=";
//            //使用私钥对密文进行解密 返回解密后的数据
//            byte[] newSource111 = decryptByRSAPriave(signPrivate, Base64.getDecoder().decode(publicpwd.getBytes()));
//            System.out.println("私钥解密1：" + new String(newSource111, "UTF-8"));


        } catch (Exception e) {
            e.printStackTrace();
        }
//        long temp = System.currentTimeMillis();
//        //生成公钥和私钥
//        genKeyPair();
////        //加密字符串
//        System.out.print("公钥:" + keyMap.get(0));
//        System.out.println();
//        System.out.print("私钥:" + keyMap.get(1));
//        System.out.println("生成密钥消耗时间:" + (System.currentTimeMillis() - temp) / 1000.0 + "秒");
//        //客户id + 授权时间 + 所用模块
        String message = "{\"iv\":\"CreateByJane2021\",\"key\":\"wwwwwWwwwwwwwww1wwwwwAAAwwwwwww1\"}";
        System.out.println("原文:" + message);
        //通过原文，和公钥()加密。
        String  messageEn = encrypt(message, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCwQeHbk+64SVbVaG2ggt1CCBxLZ6e4feoEPKuMuccggKRlit1UmSv565yDqANveenhhyZGrSUWjxZxSD6Vo+ZVsggInxBxHiRfBO0iKEt7p2TudJa5ESFaulpQpyatMfcpr6x1UeNkz2L7n/aWFnsJ4ElerqFGyozeYmdn/GeQqwIDAQAB");
        System.out.println("密文:" + messageEn);
        String  messageDe= decrypt(messageEn,"MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBALBB4duT7rhJVtVobaCC3UIIHEtnp7h96gQ8q4y5xyCApGWK3VSZK/nrnIOoA2956eGHJkatJRaPFnFIPpWj5lWyCAifEHEeJF8E7SIoS3unZO50lrkRIVq6WlCnJq0x9ymvrHVR42TPYvuf9pYWewngSV6uoUbKjN5iZ2f8Z5CrAgMBAAECgYEAoIjxGNhtRmZt82Pw7ZAjzB+s/bUEWxI8Ee3cZglTZ7Qjh+Tp4EK+fhvt6zAdKOyGbAZ3g0nyjNB4pWcisZKdl43oT0O+NC0AWzn6wWAEAkDLjan754hYjpdLkkow1Nlfy+Qy8O8/2wKC1ZgoCwwYXFnvXsRB1Ze5ww20AX5d4nECQQDkHFJsP1NvEVZiiKfMUIxqsZYLCdowOIAbmS2zmnCCIoIGGurFpDEe+1CGp7KqzYSfvaYtDQ9q89X95MXJfHxzAkEAxc6YOWO26EVr24XnKnyBVH7SxKUvhfDBD07LvHz+oclaWVo2H3aq0PP2LGy4Vc/fG9xpHl+L+JJi24tdWZWE6QJBAJw0QMTbgHjYOH108uLYba9KOGMKthy7NA6yEDejFFQo6TzhBc+dpf1/Px9WiuSno5/3sGm9PceRfgJEvKRC2L0CQQCH7ID53qVPUnTdMlQ5A7kzQLrAWeogFeH0DOc/LH0k22H0SGMsl7SlRR8CQ+sfPGpJkNnC/1EFjEHWLxwEdRQRAkEAknfaDzu4Mge3e6RgAnK1ACjgWzAKp2KIeULojOJGVCpwvvzJKCAZTY6u2F9kerOjps6KIXTyDF3cRe1JaPgXTA==");
        //通过密文，和私钥解密。
        System.out.println("解密:" + messageDe);

        System.err.println(org.apache.commons.codec.binary.Base64.encodeBase64String("http://www.chudianhudong.com".getBytes()));

    }
}