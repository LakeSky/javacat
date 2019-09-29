package com.kzh.sys.controller;

import com.kzh.sys.app.AppConstant;
import com.kzh.sys.app.utils.SessionUtil;
import com.kzh.sys.core.exception.WorldValidateException;
import com.kzh.sys.enums.FileType;
import com.kzh.sys.model.SysFile;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.UploadFileResponse;
import com.kzh.sys.service.sys.FileRelationService;
import com.kzh.sys.service.sys.FileService;
import com.kzh.sys.util.qiniu.QiniuUtil;
import com.kzh.sys.util.qiniu.ReturnBody;
import com.kzh.sys.util.sign.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * Created by gang on 2016/12/8.
 */
@Controller
@RequestMapping("/file")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Resource
    private FileService privateFileService;
    @Resource
    private FileRelationService fileRelationService;

    @RequestMapping(name = "文件上传并创建关联关系", value = "/uploadAndLink")
    @ResponseBody
    public Object uploadAndLink(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String className = request.getParameter("className");
        String objId = request.getParameter("objId");
        Result result = new Result();
        try {
            if (file == null || file.isEmpty()) {
                result.setMsg("上传文件为空");
                return result;
            }
            String fileName = file.getOriginalFilename();
            if (StringUtils.isBlank(fileName)) {
                fileName = MD5Util.md5(UUID.randomUUID().toString());
            }
            String filePathPrefix = QiniuUtil.getQiniuPathPrefix();
            //文件上传七牛
            ReturnBody returnBody = QiniuUtil.bucketFileUploadByByteArr(file.getBytes(), filePathPrefix + fileName, AppConstant.QINIU_PRIVATE_BUCKET_CONFIG);
            //文件存在本地
            QiniuUtil.saveLocalPrivateFile(file, filePathPrefix, fileName);
            //保存文件记录
            SysFile privateFile = privateFileService.savePrivateFile(SessionUtil.getUserName(), returnBody, filePathPrefix, fileName, FileType.DOC, objId);
            result.setSuccess(true);
            UploadFileResponse data = new UploadFileResponse(privateFile);
            result.setData(data);
            return result;
        } catch (WorldValidateException e) {
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("file.upload. error:", e);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @RequestMapping(name = "文件下载", value = "/downLoad")
    public String downLoad(HttpServletRequest request, HttpServletResponse response, String fileKey) throws Exception {
        String failMsg = "";
        try {
            String username = SessionUtil.getUserName();
            logger.info("username:[{}]下载了文件,,文件fileKey:[{}]", new Object[]{username, fileKey});

            SysFile privateFile = privateFileService.getPrivateFileByFileKey(fileKey);
            if (privateFile == null) {
                throw new WorldValidateException("下载的文件不存在");
            }
            return "redirect:" + QiniuUtil.getPrivateDownFileUrl(privateFile.getQiniuFilePath(), true, null);
        } catch (WorldValidateException e) {
            failMsg = e.getMessage();
        } catch (Exception e) {
            logger.error("file.download. error:", e);
            failMsg = "网络异常";
        }
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            String filename = "文件下载失败.txt";
            response.setContentType("text/plain;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            os.write(failMsg.getBytes());
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
        return null;
    }

    @RequestMapping(name = "文件下载", value = "/downLoadById")
    public String downLoadById(HttpServletRequest request, HttpServletResponse response, String fileId) throws Exception {
        String failMsg = "";
        try {
            String username = SessionUtil.getUserName();
            logger.info("username:[{}]下载了文件,,文件fileId:[{}]", new Object[]{username, fileId});

            SysFile privateFile = privateFileService.getPrivateFileByFileId(fileId);
            if (privateFile == null) {
                throw new WorldValidateException("下载的文件不存在");
            }
            return "redirect:" + QiniuUtil.getPrivateDownFileUrl(privateFile.getQiniuFilePath(), true, null);
        } catch (WorldValidateException e) {
            failMsg = e.getMessage();
        } catch (Exception e) {
            logger.error("file.download. error:", e);
            failMsg = "网络异常";
        }
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            String filename = "文件下载失败.txt";
            response.setContentType("text/plain;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            os.write(failMsg.getBytes());
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
        return null;
    }

    @RequestMapping(value = "/delUploadFile")
    @ResponseBody
    public Object delUploadFile(HttpServletRequest request) {
        String fileId = request.getParameter("key");
        Result result = fileRelationService.del(fileId);
        return result;
    }

    @RequestMapping(name = "本地文件下载", value = "/download/local")
    public void downloadLocalFile(HttpServletRequest request, HttpServletResponse response, String fileId) throws Exception {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            SysFile privateFile = privateFileService.getPrivateFileByFileId(fileId);
            if (privateFile == null) {
                throw new WorldValidateException("下载的文件不存在");
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=" + privateFile.getFileName());
            response.setHeader("Content-Length", String.valueOf(privateFile.getFileSize()));
            bis = new BufferedInputStream(new FileInputStream(privateFile.getLocalFilePath()));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }
}
