package com.kzh.sys.util;

import com.kzh.sys.core.exception.WorldValidateException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2015/12/28.
 */
public class FileUploadUtil {
    public static String uploadFile(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
        //创建文件存储位置
        String path = "";
        String realPath = request.getSession().getServletContext().getRealPath("/uploadFile");
        File pathFile = new File(realPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();   //文件夹不存在创建
        }
        try {
            file.transferTo(new File((realPath + "/" + file.getOriginalFilename())));
            path = realPath + "/" + file.getOriginalFilename();
            System.out.println("path==========================" + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String uploadFileEx(HttpServletRequest request, MultipartFile file, String dir) {
        if (file != null) {
            String result = "";
            String baseDir = File.separator + "uploadFile" + File.separator + dir;
            String realPath = request.getSession().getServletContext().getRealPath(baseDir);
            File pathFile = new File(realPath);
            if (!pathFile.exists()) {
                pathFile.mkdir();
            }
            try {
                UUID uuid = UUID.randomUUID();
                String prefix = uuid.toString();
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String path = realPath + File.separator + prefix + suffix;
                result = dir + "/" + prefix + suffix;
                file.transferTo(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }

    //保存本地私有文件
    public static void saveLocal(MultipartFile file, String localFilePath, String fileName) throws IOException {
        File localFile = new File(localFilePath + File.separator + fileName);
        boolean mkDirs = true;
        if (!localFile.exists()) {
            mkDirs = localFile.mkdirs();
        }
        if (!mkDirs) {
            throw new WorldValidateException("文件存储失败");
        }
        file.transferTo(localFile);
    }
}
