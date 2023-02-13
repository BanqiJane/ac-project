package xyz.acproject.utils.enums;

/**
 * @author Jane
 * @ClassName AESInstance
 * @Description TODO
 * @date 2021/6/17 15:13
 * @Copyright:2021
 */
public enum AESInstance {
    ECB("AES/ECB/PKCS7Padding","")
    /***默认向量常量 必须为16字节**/
    ,CBC("AES/CBC/PKCS7Padding","CreateByJane2021")
    ,CUSTOM("")
    ;

    private String instance;
    private String iv;
    AESInstance(String instance) {
        this.instance = instance;
    }
    AESInstance(String instance, String iv) {
        this.instance = instance;
        this.iv = iv;
    }

    public String getInstance() {
        return instance;
    }

    public String getIv() {
        return iv;
    }

    public AESInstance instance(String instance) {
        this.instance = instance;
        return this;
    }
    public AESInstance iv(String iv) {
        this.iv = iv;
        return this;
    }
}
