package com.kzh.sys.util.qiniu;

/**
 * Created by xinyu.qiu on 2017/2/25.
 * 自定义七牛返回对象
 */
public class ReturnBody {
    @ReturnBodyKey(key = "bucket",keyTemplate = "$(bucket)")
    private String bucket;//获得上传的目标空间名。

    @ReturnBodyKey(key = "key",keyTemplate = "$(key)")
    private String key;//获得文件保存在空间中的资源名。

    @ReturnBodyKey(key = "fsize",keyTemplate = "$(fsize)")
    private Integer fsize;//	资源尺寸，单位为字节

    @ReturnBodyKey(key = "mimeType",keyTemplate = "$(mimeType)")
    private String mimeType;//资源类型，例如JPG图片的资源类型为image/jpg

    @ReturnBodyKey(key = "hash",keyTemplate = "$(etag)")
    private String hash;//文件的hash值 文件上传成功后的 HTTPETag。若上传时未指定资源ID，Etag将作为资源ID使用。

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getFsize() {
        return fsize;
    }

    public void setFsize(Integer fsize) {
        this.fsize = fsize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
