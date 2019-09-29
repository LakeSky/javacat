package com.kzh.sys.pojo;

import com.kzh.sys.model.SysFile;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UploadFileResponse {
    private String id;
    private String fileName;//文件名
    private String mimeType;//文件类型
    private Integer fileSize;//文件大小,字节
    private String fileHash;//文件hash
    private String downloadUrl;//下载地址

    private String fileKey;//文件的一个随机码,用于下载

    public UploadFileResponse(SysFile privateFile) {
        this.id = privateFile.getId();
        this.fileName = privateFile.getFileName();
        this.mimeType = privateFile.getMimeType();
        this.fileSize = privateFile.getFileSize();
        this.fileHash = privateFile.getFileHash();
        this.downloadUrl = privateFile.getDownLoadFullUrl();
        this.fileKey = privateFile.getFileKey();
    }
}
