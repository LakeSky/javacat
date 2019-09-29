package com.kzh.sys.model;


import com.kzh.busi.enums.BusiDirType;
import com.kzh.sys.app.AppConstant;
import com.kzh.sys.service.generate.auto.QClass;
import com.kzh.sys.util.SysUtil;
import com.kzh.sys.util.qiniu.UploadUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.File;

/**
 * Created by xinyu.qiu on 2017/2/25.
 * 用户私有文件表
 * 本地文件位置 :localFilePathPrefix+localFilePath
 * 七牛上文件的位置,qiniuFileBucket->的域名 + qiniuFilePath
 */
@QClass(name = "上传文件")
@Entity
@Table(name = "sys_file")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFile extends BaseEntity {
    @Column(name = "username")
    private String username;//用户名
    @Column(name = "file_name")
    private String fileName;//文件名
    @Column(name = "mime_type")
    private String mimeType;//文件类型
    @Column(name = "local_file_path_prefix")
    private String localFilePathPrefix;//本地文件文件夹
    @Column(name = "local_file_path")
    private String localFilePath;//本地存储文件位置
    @Column(name = "qiniu_file_bucket")
    private String qiniuFileBucket;//七牛文件服务器容器
    @Column(name = "qiniu_file_path")
    private String qiniuFilePath;//七牛文件存储位置
    @Column(name = "file_size")
    private Integer fileSize;//文件大小,字节
    @Column(name = "file_hash")
    private String fileHash;//文件hash
    @Column(name = "file_key")
    private String fileKey;//文件的一个随机码
    @Column(name = "seq")
    private Integer seq;//上传文件排序

    //扩增字段
    @Transient
    private String downLoadUrl;//内部下载地址相对路径 ,相对路径可以用于存到别的表里,使用的时候,
    @Transient
    private String downLoadFullUrl;//内部下载地址绝对路径
    @Transient
    private String appDownLoadFullUrl;//给APP端使用的下载地址
    @Transient
    private String fullPath;

    public String getLocalFilePath() {
        if (SysUtil.isNotEmpty(localFilePath)) {
            File file = new File(localFilePath);
            if (file.exists()) {
                return localFilePath;
            } else {
                localFilePath = AppConstant.dataPath + File.separator + BusiDirType.upload.name() + File.separator + fileName;

                return localFilePath;
            }
        }
        return localFilePath;
    }

    public String getLocalFilePathPrefix() {
        if (SysUtil.isNotEmpty(localFilePath)) {
            File file = new File(localFilePath);
            if (file.exists()) {
                return file.getParent();
            } else {
                localFilePathPrefix = AppConstant.dataPath + File.separator + BusiDirType.upload.name();
                return localFilePathPrefix;
            }
        }
        return localFilePathPrefix;
    }

    public void setLocalFilePathPrefix(String localFilePathPrefix) {
        this.localFilePathPrefix = localFilePathPrefix;
    }

    public String getFullPath() {
        return this.localFilePathPrefix + this.localFilePath;
    }

    public String getDownLoadUrl() {
        return UploadUtil.getRelativeDownLoadUrlByFileKey(this.getFileKey());
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getDownLoadFullUrl() {
        return UploadUtil.getFullDownLoadUrlByFileKey(this.getFileKey());
    }

    public void setDownLoadFullUrl(String downLoadFullUrl) {
        this.downLoadFullUrl = downLoadFullUrl;
    }
}
