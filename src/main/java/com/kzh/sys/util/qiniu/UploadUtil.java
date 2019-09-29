package com.kzh.sys.util.qiniu;

import com.kzh.sys.util.SysUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Created by gang on 2016/12/9.
 */
public class UploadUtil {
    private static final Logger logger = Logger.getLogger(UploadUtil.class);

    //根据fileKey,换成绝对路径
    public static String getFullDownLoadUrlByFileKey(String fileKey) {
        if (StringUtils.isNotBlank(fileKey)) {
            return getRelativeDownLoadUrlByFileKey(fileKey);
        }
        return "";
    }

    //根据fileId,换成绝对路径
    public static String getFullDownLoadUrlByFileId(String fileId) {
        if (StringUtils.isNotBlank(fileId)) {
            return getRelativeDownLoadUrlByFileId(fileId);
        }
        return "";
    }

    //根据fileKey,换成相对路径
    public static String getRelativeDownLoadUrlByFileKey(String fileKey) {
        return "file/downLoad?fileKey=" + SysUtil.valueOf(fileKey);
    }

    public static String getRelativeDownLoadUrlByFileId(String fileId) {
        return "file/downLoadById?fileId=" + SysUtil.valueOf(fileId);
    }
}
