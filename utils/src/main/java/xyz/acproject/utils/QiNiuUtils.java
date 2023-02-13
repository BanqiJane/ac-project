package xyz.acproject.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author Jane
 * @ClassName QinNiuUtils
 * @Description TODO
 * @date 2021/3/3 17:44
 * @Copyright:2021
 */
public class QiNiuUtils {

    private QiNiuUtils(){

    }

    public static String prefix;
    public static String bucketName;
    public static String accessKey;
    public static String secretKey;
    public static String zone;
    public static String domain;
    private static UploadManager uploadManager;
    private static BucketManager bucketManager;
    private static Auth auth;
    private static Configuration qiniuConfig;


    static {
        prefix= SpringUtils.getConfigByKey("acproject.qiniu.prefix");
        bucketName= SpringUtils.getConfigByKey("acproject.qiniu.bucketName");
        accessKey=SpringUtils.getConfigByKey("acproject.qiniu.accessKey");
        secretKey=SpringUtils.getConfigByKey("acproject.qiniu.secretKey");
        zone= SpringUtils.getConfigByKey("acproject.qiniu.zone");
        domain= SpringUtils.getConfigByKey("acproject.qiniu.domain");
        qiniuConfig = new Configuration(Region.autoRegion());
        uploadManager = new UploadManager(qiniuConfig);
        auth = Auth.create(accessKey, secretKey);
        bucketManager = new BucketManager(auth, qiniuConfig);
    }


    public static String getToken(String bucketName){
        if(StringUtils.isBlank(bucketName)){
            bucketName = QiNiuUtils.bucketName;
        }
        return auth.uploadToken(bucketName);
    }


    public static String uploadFile(byte[] files, String fileName, String bucketName) throws QiniuException {
        if(StringUtils.isBlank(bucketName)){
            bucketName = QiNiuUtils.bucketName;
        }
        Response response = uploadManager.put(files, fileName, getUploadToken(bucketName));
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = uploadManager.put(files, fileName, getUploadToken(bucketName));
            retry++;
        }
        if (response.statusCode == 200) {
            return "https://" + domain + "/" + fileName;
        }
        return "上传失败!";
    }

    public static String uploadFile(String filePath, String fileName, String bucketName) throws QiniuException {
        if(StringUtils.isBlank(bucketName)){
            bucketName = QiNiuUtils.bucketName;
        }
        Response response = uploadManager.put(filePath, fileName, getUploadToken(bucketName));
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = uploadManager.put(filePath, fileName, getUploadToken(bucketName));
            retry++;
        }
        if (response.statusCode == 200) {
            return "https://" + domain + "/" + fileName;
        }
        return "上传失败!";
    }

    /**
     * 上传文件
     * @param file
     * @param fileName
     * @return
     * @throws QiniuException
     */
    public static String uploadFile(File file, String fileName, String bucketName) throws QiniuException {
        if(StringUtils.isBlank(bucketName)){
            bucketName = QiNiuUtils.bucketName;
        }
        Response response = uploadManager.put(file, fileName, getUploadToken(bucketName));
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = uploadManager.put(file, fileName, getUploadToken(bucketName));
            retry++;
        }
        if (response.statusCode == 200) {
            return "https://" + domain + "/" + fileName;
        }
        return "上传失败!";
    }

    /**
     * 删除文件
     * @param bucketName
     * @return
     * @throws QiniuException
     */
    public static String delete(String fileName, String bucketName) throws QiniuException {
        if(StringUtils.isBlank(bucketName)){
            bucketName = QiNiuUtils.bucketName;
        }
        Response response = bucketManager.delete(bucketName, fileName);
        int retry = 0;
        while (response.needRetry() && retry++ < 3) {
            response = bucketManager.delete(bucketName, fileName);
        }
        return response.statusCode == 200 ? "删除成功!" : "删除失败!";
    }

    /**
     * 获取上传凭证
     */
    private static String getUploadToken() {
        return auth.uploadToken(bucketName);
    }

    /**
     * 获取上传凭证
     */
    private static String getUploadToken(String bucketName) {
        return auth.uploadToken(bucketName);
    }



}
