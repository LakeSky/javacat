package com.kzh.sys.service.sys;

import com.kzh.sys.app.AppConstant;
import com.kzh.sys.app.utils.SessionUtil;
import com.kzh.sys.core.exception.WorldValidateException;
import com.kzh.sys.dao.FileDao;
import com.kzh.sys.dao.UserDao;
import com.kzh.sys.enums.FileType;
import com.kzh.sys.model.SysFile;
import com.kzh.sys.model.User;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.UploadFileResponse;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.SysUtil;
import com.kzh.sys.util.qiniu.QiniuConfig;
import com.kzh.sys.util.qiniu.QiniuUtil;
import com.kzh.sys.util.qiniu.ReturnBody;
import com.kzh.sys.util.sign.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional
public class FileService {
    private static Logger logger = LoggerFactory.getLogger(FileService.class);

    @Resource
    private UserDao userDao;
    @Resource
    private FileDao fileDao;
    @Resource
    private FileRelationService fileRelationService;

    public SysFile savePrivateFile(String username, ReturnBody returnBody, String filePath, String filePathPrefix, String fileName) {
        SysFile privateFile = new SysFile();
        User user = null;
        if (SysUtil.isNotEmpty(username)) {
            user = userDao.findByUsername(username);
        }
        if (user == null) {
            throw new WorldValidateException("用户不存在");
        }
        privateFile.setUsername(user.getUsername());
        privateFile.setFileName(fileName);
        if (!filePath.endsWith(java.io.File.separator)) {
            filePath = filePath + java.io.File.separator;
        }
        privateFile.setLocalFilePathPrefix(filePath);
        privateFile.setLocalFilePath(filePath + fileName);
//        QiniuConfig privateBucketConfig = QiniuUtil.getBucketConfig(AppConstant.QINIU_PRIVATE_BUCKET_CONFIG);
//        privateFile.setQiniuFileBucket(privateBucketConfig.getBucketName());
//        privateFile.setQiniuFilePath(filePathPrefix + fileName);

        privateFile.setMimeType(returnBody.getMimeType());
        privateFile.setFileSize(returnBody.getFsize());
        privateFile.setFileHash(returnBody.getHash());
        privateFile.setFileKey(MD5Util.md5(UUID.randomUUID().toString()) + "_" + MD5Util.md5(UUID.randomUUID().toString()));
        fileDao.save(privateFile);
        return privateFile;
    }

    public SysFile savePrivateFile(String username, ReturnBody returnBody, String filePathPrefix, String fileName, FileType fileType, String objId) {
        SysFile privateFile = new SysFile();
        User user = null;
        if (SysUtil.isNotEmpty(username)) {
            user = userDao.findByUsername(username);
        }
        if (user == null) {
            throw new WorldValidateException("用户不存在");
        }
        privateFile.setUsername(user.getUsername());
        privateFile.setFileName(fileName);
        privateFile.setLocalFilePathPrefix(QiniuUtil.getLocalPrivateFileFolder());
        privateFile.setLocalFilePath(filePathPrefix + fileName);
        QiniuConfig privateBucketConfig = QiniuUtil.getBucketConfig(AppConstant.QINIU_PRIVATE_BUCKET_CONFIG);
        privateFile.setQiniuFileBucket(privateBucketConfig.getBucketName());
        privateFile.setQiniuFilePath(filePathPrefix + fileName);

        privateFile.setMimeType(returnBody.getMimeType());
        privateFile.setFileSize(returnBody.getFsize());
        privateFile.setFileHash(returnBody.getHash());
        privateFile.setFileKey(MD5Util.md5(UUID.randomUUID().toString()) + "_" + MD5Util.md5(UUID.randomUUID().toString()));
        SysFile privateFileDb = fileDao.save(privateFile);
        fileRelationService.save(fileType, objId, privateFileDb.getId());
        return privateFile;
    }

    public SysFile getPrivateFileByFileKey(String fileKey) {
        if (StringUtils.isBlank(fileKey)) {
            return null;
        }
        SysFile privateFile = fileDao.findByFileKey(fileKey);
        return privateFile;
    }

    public SysFile getPrivateFileByFileId(String fileId) {
        if (StringUtils.isBlank(fileId)) {
            return null;
        }
        SysFile privateFile = fileDao.findOne(fileId);
        return privateFile;
    }

    public List<SysFile> getPrivateFileListByFileKeys(Set<String> fileKeys) {
        List<SysFile> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(fileKeys)) {
            List<SysFile> privateFileList = fileDao.findByFileKeyIn(fileKeys);
            for (SysFile privateFile : privateFileList) {
                result.add(privateFile);
            }
        }
        return result;
    }

    public Map<String, SysFile> getPrivateFileMapByFileKeys(Set<String> fileKeys) {
        List<SysFile> privateFileList = getPrivateFileListByFileKeys(fileKeys);
        Map<String, SysFile> result = new HashMap<>();
        for (SysFile privateFile : privateFileList) {
            result.put(privateFile.getFileKey(), privateFile);
        }
        return result;
    }

    public List<SysFile> getPrivateFileListByIdsAndUsername(Set<Long> ids, String username) {
        List<SysFile> privateFileList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(ids) && SysUtil.isNotEmpty(username)) {
            List<SysFile> privateFiles = fileDao.findByIdInAndUsername(ids, username);
            privateFileList.addAll(privateFiles);
        }
        return privateFileList;
    }

    public Map<String, SysFile> getPrivateFileMapByIdsAndUsername(Set<Long> ids, String username) {
        List<SysFile> privateFileList = getPrivateFileListByIdsAndUsername(ids, username);
        Map<String, SysFile> result = new HashMap<>();
        for (SysFile privateFile : privateFileList) {
            result.put(privateFile.getId(), privateFile);
        }
        return result;
    }

    public SysFile savePrivateFile(String username, ReturnBody returnBody, String filePathPrefix, String fileName, Integer seq) {
        SysFile privateFile = new SysFile();
        User user = null;
        if (SysUtil.isNotEmpty(username)) {
            user = userDao.findByUsername(username);
        }
        if (user == null) {
            throw new WorldValidateException("用户不存在");
        }
        privateFile.setSeq(seq);
        privateFile.setUsername(user.getUsername());
        privateFile.setFileName(fileName);
        privateFile.setLocalFilePathPrefix(QiniuUtil.getLocalPrivateFileFolder());
        privateFile.setLocalFilePath(filePathPrefix + fileName);
        QiniuConfig privateBucketConfig = QiniuUtil.getBucketConfig(AppConstant.QINIU_PRIVATE_BUCKET_CONFIG);
        privateFile.setQiniuFileBucket(privateBucketConfig.getBucketName());
        privateFile.setQiniuFilePath(filePathPrefix + fileName);

        privateFile.setMimeType(returnBody.getMimeType());
        privateFile.setFileSize(returnBody.getFsize());
        privateFile.setFileHash(returnBody.getHash());
        privateFile.setFileKey(MD5Util.md5(UUID.randomUUID().toString()) + "_" + MD5Util.md5(UUID.randomUUID().toString()));
        SysFile privateFileDb = fileDao.save(privateFile);
        return privateFileDb;
    }

    public Result uploadPrivateFile(MultipartFile file) {
        Result result = new Result();
        try {
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
            SysFile privateFile = savePrivateFile(SessionUtil.getUserName(), returnBody, QiniuUtil.getLocalPrivateFileFolder(), filePathPrefix, fileName);
            result.setSuccess(true);
            UploadFileResponse data = new UploadFileResponse(privateFile);
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    public String uploadByBytes(String username, byte[] bytes, String filePathPrefix, String fileName) {
        ReturnBody returnBody = QiniuUtil.bucketFileUploadByByteArr(bytes, filePathPrefix + fileName, AppConstant.QINIU_PRIVATE_BUCKET_CONFIG);
        SysFile privateFile = savePrivateFile(username, returnBody, QiniuUtil.getLocalPrivateFileFolder(), filePathPrefix, fileName);

        return privateFile.getDownLoadFullUrl();
    }
}