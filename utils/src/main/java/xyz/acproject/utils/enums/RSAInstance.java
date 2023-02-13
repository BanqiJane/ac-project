package xyz.acproject.utils.enums;

/**
 * @author Jane
 * @ClassName RSAInstance
 * @Description TODO
 * @date 2021/7/31 22:01
 * @Copyright:2021
 */
public enum RSAInstance {
    PKCS1("RSA/ECB/PKCS1Padding")
    ,PKCS7("RSA/ECB/PKCS7Padding")
    ,CUSTOM("")
            ;
    private String instance;

    RSAInstance(String instance) {
        this.instance = instance;
    }

    public String getInstance() {
        return instance;
    }


    public RSAInstance instance(String instance) {
        this.instance = instance;
        return this;
    }
}
