package com.kzh.sys.util.qiniu;

import com.kzh.sys.app.AppConstant;
import com.kzh.sys.app.utils.AppUtils;
import com.kzh.sys.core.exception.WorldValidateException;
import com.kzh.sys.util.DateUtil;
import com.kzh.sys.util.JsonUtil;
import com.kzh.sys.util.SysUtil;
import com.kzh.sys.util.sign.MD5Util;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by xinyu.qiu on 2017/2/21.
 */
public class QiniuUtil {
    private static final Logger logger = LoggerFactory.getLogger(QiniuUtil.class);
    private static final String RETURN_BODY = "returnBody";//自定义返回值的key
    private static final String RETURN_BODY_JSON_DEFINE; //自定义返回值格式定义
    private static final int UPLOAD_EXPIRE_SECONDS = 120;//上传超时时间,秒
    private static final int PRIVATE_DOWNLOAD_EXPIRE_SECONDS = 3600;//私有下载链接过期时间


    static class ZoneConfig {
        public static final String ZONE0 = "zone0";//华东机房相关域名
        public static final String ZONE1 = "zone1";//华北机房相关域名
        public static final String ZONE2 = "zone2";//华南机房相关域名
        public static final String ZONE_NA0 = "zoneNa0";//北美机房相关域名
    }

    static {
        RETURN_BODY_JSON_DEFINE = genReturnBodyStr();
    }


    //根据zone的配置取到对应的zone
    public static Zone getZoneByStr(String zoneStr) {
        Zone zone = null;
        if (ZoneConfig.ZONE0.equals(zoneStr)) {
            zone = Zone.zone0();
        } else if (ZoneConfig.ZONE1.equals(zoneStr)) {
            zone = Zone.zone1();
        } else if (ZoneConfig.ZONE2.equals(zoneStr)) {
            zone = Zone.zone2();
        } else if (ZoneConfig.ZONE_NA0.equals(zoneStr)) {
            zone = Zone.zoneNa0();
        } else {
            zone = Zone.autoZone();
        }
        return zone;
    }


    //字节数组上传七牛私有空间
    public static ReturnBody bucketFileUploadByByteArr(byte[] uploadBytes, String filePath, String configKey) {
        try {
            //构造一个带指定Zone对象的配置类
            QiniuConfig privateBucketConfig = getBucketConfig(configKey);
            if (privateBucketConfig == null) {
                throw new WorldValidateException("文件上传失败!");
            }
            Zone zone = getZoneByStr(privateBucketConfig.getZone());
            Configuration cfg = new Configuration(zone);
            UploadManager uploadManager = new UploadManager(cfg);

            Auth auth = Auth.create(privateBucketConfig.getAccessKey(), privateBucketConfig.getSecretKey());
            StringMap putPolicy = new StringMap();
            putPolicy.put(RETURN_BODY, RETURN_BODY_JSON_DEFINE);
            String upToken = auth.uploadToken(privateBucketConfig.getBucketName(), filePath, UPLOAD_EXPIRE_SECONDS, putPolicy);

            Response response = uploadManager.put(uploadBytes, filePath, upToken);
            logger.info("filePath:[{}] 上传七牛完成,Response:[{}]:", filePath, JsonUtil.objectToJson(response));
            //解析上传成功的结果
            if (response != null && response.isOK()) {
                String responseBodyJson = response.bodyString();
                logger.info("filePath:[{}] 上传七牛成功,Response:[{}]:", filePath, responseBodyJson);
                return (ReturnBody) JsonUtil.jsonToObject(responseBodyJson, ReturnBody.class);
            } else {
                logger.error("filePath:[{}] 上传七牛失败", filePath);
                throw new WorldValidateException("文件上传失败!");
            }
        } catch (QiniuException ex) {
            Response r = ex.response;
            logger.error("filePath:[{}] 上传七牛失败,错误信息:[{}],QiniuException:[{}],", new Object[]{filePath, (r != null ? r.error : ""), SysUtil.valueOf(r)});
        }
        return null;
    }

    //保存本地私有文件
    public static void saveLocalPrivateFile(MultipartFile file, String filePathPrefix, String fileName) throws IOException {
        String localFilePath = QiniuUtil.getLocalPrivateFileFolder();
        File localFile = new File(localFilePath + filePathPrefix + fileName);
        boolean mkdirs = true;
        if (!localFile.exists()) {
            mkdirs = localFile.mkdirs();
        }
        if (!mkdirs) {
            throw new WorldValidateException("文件存储失败");
        }
        file.transferTo(localFile);
    }


    //公开空间 下载
    public static String getPublicDownFileUrl(String filePath, boolean isDownAttachment, String attachmentName) {
        QiniuConfig publicBucketConfig = getBucketConfig(AppConstant.QINIU_PUBLIC_BUCKET_CONFIG);
        if (publicBucketConfig == null) {
            throw new WorldValidateException("文件下载链接创建失败!");
        }
        //转义filePath
        StringBuilder encodingFilePath = new StringBuilder();
        if (StringUtils.isNotBlank(filePath)) {
            for (String path : filePath.split("/")) {
                if (encodingFilePath.length() > 0) {
                    encodingFilePath.append("/");
                }
                if (path != null) {
                    try {
                        encodingFilePath.append(URLEncoder.encode(path, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        //拼接域名
        StringBuilder publicUrlSb = new StringBuilder(publicBucketConfig.getBucketDomain());
        if (!publicBucketConfig.getBucketDomain().endsWith("/")) {
            publicUrlSb.append("/");
        }
        publicUrlSb.append(encodingFilePath);

        if (isDownAttachment) {
            int pos = publicUrlSb.indexOf("?");
            if (pos > 0) {
                publicUrlSb.append("&attname=");
            } else {
                publicUrlSb.append("?attname=");
            }
            if (StringUtils.isNotBlank(attachmentName)) {
                try {
                    publicUrlSb.append(URLEncoder.encode(attachmentName, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage(), e);
                }
            }

        }
        //空格会被 URLEncoder.encode 翻译成+需要再次转化成%20 才是正确的encoding
        String finalUrl = publicUrlSb.toString().replace("+", "%20");
        logger.info("文件下载地址:[{}]", finalUrl);
        System.out.println(finalUrl);
        return finalUrl;
    }

    //生成私有空间的下载地址,用于web的时候,可以设置isDown 生成的链接就可以点击下载了
    public static String getPrivateDownFileUrl(String filePath, boolean isDownAttachment, String attachmentName) {
        QiniuConfig privateBucketConfig = getBucketConfig(AppConstant.QINIU_PRIVATE_BUCKET_CONFIG);
        if (privateBucketConfig == null) {
            throw new WorldValidateException("文件下载链接创建失败!");
        }
        //用来签名的url
        StringBuffer signUrl = new StringBuffer();
        signUrl.append(privateBucketConfig.getBucketDomain());
        //转义filePath
        StringBuilder encodingFilePath = new StringBuilder();
        if (StringUtils.isNotBlank(filePath)) {
            for (String path : filePath.split("/")) {
                if (encodingFilePath.length() > 0) {
                    encodingFilePath.append("/");
                }
                if (path != null) {
                    try {
                        encodingFilePath.append(URLEncoder.encode(path, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //拼接域名
        StringBuilder privateUrl = new StringBuilder(privateBucketConfig.getBucketDomain());
        if (!privateBucketConfig.getBucketDomain().endsWith("/")) {
            privateUrl.append("/");
            signUrl.append("/");
        }
        privateUrl.append(encodingFilePath);
        if (isDownAttachment) {
            int pos = privateUrl.indexOf("?");
            if (pos > 0) {
                privateUrl.append("&attname=");
                signUrl.append("&attname=");
            } else {
                privateUrl.append("?attname=");
                signUrl.append("?attname=");
            }
            if (StringUtils.isNotBlank(attachmentName)) {
                try {
                    privateUrl.append(URLEncoder.encode(attachmentName, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage(), e);
                }
            }

        }
        //空格会被 URLEncoder.encode 翻译成+需要再次转化成%20 才是正确的encoding
        String privateUrlStr = privateUrl.toString().replace("+", "%20");
        Auth auth = Auth.create(privateBucketConfig.getAccessKey(), privateBucketConfig.getSecretKey());

        String finalUrl = auth.privateDownloadUrl(privateUrlStr, PRIVATE_DOWNLOAD_EXPIRE_SECONDS);
        logger.info("文件下载地址:[{}]", filePath);
        System.out.println(finalUrl);
        return finalUrl;
    }

    //上传完返回值配置
    public static String genReturnBodyStr() {
        Map<String, String> returnBodyKeyMap = new HashMap<>();
        Field[] declaredFields = ReturnBody.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ReturnBodyKey returnBodyKey = declaredField.getAnnotation(ReturnBodyKey.class);
            if (returnBodyKey != null) {
                returnBodyKeyMap.put(returnBodyKey.key(), returnBodyKey.keyTemplate());
            }
        }
        return JsonUtil.objectToJson(returnBodyKeyMap);
    }

    //获取私有空间配置
    public static QiniuConfig getBucketConfig(String configKey) {
        String config = AppUtils.getConfigValue(configKey);
        QiniuConfig qiniuConfig = null;
        if (StringUtils.isBlank(config)) {
            logger.error("七牛空间配置不正确");
        } else {
            qiniuConfig = JsonUtil.jsonToObject(config, QiniuConfig.class);
        }
        return qiniuConfig;
    }

    //七牛文件文件路径前缀 开头不带斜杠,结尾带斜杠
    public static String getQiniuPathPrefix() {
        String qiniuPrivateFilePathPrefix = DateUtil.convertDateLongToDateString(DateUtil.YEAR_MONTH_DATE_2, System.currentTimeMillis());
        return qiniuPrivateFilePathPrefix + "/" + MD5Util.md5(UUID.randomUUID().toString()) + "/";
    }

    //私有文件上传本地文件夹前缀,结尾带斜杠
    //C:\fileUpload\private\
    // /usr/local/upload_file/private/
    public static String getLocalPrivateFileFolder() {
        return AppUtils.getConfigValue(AppConstant.LOCAL_PRIVATE_FILE_FOLDER);
    }

    //文件上传本地文件夹前缀,结尾带斜杠
    //C:\fileUpload\public\
    // /usr/local/upload_file/public/
    public static String getLocalPublicFileFolder() {
        return AppUtils.getConfigValue(AppConstant.LOCAL_PUBLIC_FILE_FOLDER);
    }


}
