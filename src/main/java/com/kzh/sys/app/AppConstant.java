package com.kzh.sys.app;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AppConstant {
    private static final Logger logger = Logger.getLogger(AppConstant.class);

    //AES算法加密密码的key，可以根据AES类中的generateKeyString方法来生成
    public static final String encryptKey = "13872068D7E7AE12B2F026AAC5F670DE";
    public final static String USERNAME_ADMIN = "admin";//超级管理员用户名
    public final static String USERNAME_PLATFORM = "platform";//平台管理员用户名
    //项目根目录
    public static String rootPath = "";
    public static String rootParentPath = "";
    public static String dataPath = "";
    public static String uploadPath = "";

    public static Map<String, String> defaultRelatedMenuMap = new HashMap<>();

    //七牛云存储配置
    public static final String QINIU_PRIVATE_BUCKET_CONFIG = "qiniu.private.bucket.config";//七牛私有容器配置
    public static final String QINIU_PUBLIC_BUCKET_CONFIG = "qiniu.public.bucket.config";//七牛公共容器配置
    public static final String LOCAL_PRIVATE_FILE_FOLDER = "local.private.file.folder";//本地私有文件上传文件夹
    public static final String LOCAL_PUBLIC_FILE_FOLDER = "local.public.file.folder";//本地公共文件上传文件夹

    //如果ajax url含有这些单词，就在AppInterceptor或AppTokenInterceptor这两个类里面添加防止重复提交的限制
    public final static Set<String> preventRepeatKeywords = new HashSet<>();

    static {
        preventRepeatKeywords.add("save");
        preventRepeatKeywords.add("begin");
        preventRepeatKeywords.add("start");
        preventRepeatKeywords.add("submit");
    }

    public static void initPath() {
        AppConstant.rootPath = System.getProperty("javacat.root");
        if (rootPath.contains("apache-tomcat")) {
            int tomcatIndex = rootPath.indexOf("apache-tomcat");
            String tomcatPath = rootPath.substring(tomcatIndex);
            int tomcatEndIndex = tomcatPath.indexOf(File.separator);
            rootPath = rootPath.substring(0, tomcatIndex + tomcatEndIndex);
        }
        logger.info("程序根目录===" + rootPath);
        File rootDir = new File(rootPath);
        AppConstant.rootParentPath = rootDir.getParent();
        dataPath = rootParentPath + File.separator + "data";
        uploadPath = dataPath + File.separator + uploadPath;
    }
}
