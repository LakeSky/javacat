package com.kzh.sys.service.sys;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.dao.FileRelationDao;
import com.kzh.sys.enums.FileType;
import com.kzh.sys.model.FileRelation;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.util.SysUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class FileRelationService extends BaseService<FileRelation> {
    @Resource
    private FileRelationDao fileRelationdao;

    @Override
    public GenericRepository getDao() {
        return fileRelationdao;
    }

    public Result save(FileType fileType, String objId, String fileId) {
        if (SysUtil.isEmpty(objId)) {
            return new Result(false, "对象ID为空");
        }
        if (SysUtil.isEmpty(fileId)) {
            return new Result(false, "文件ID为空");
        }

        FileRelation uploadRelation = new FileRelation();
        uploadRelation.setFileType(fileType);
        uploadRelation.setObjId(objId);
        uploadRelation.setFileId(fileId);
        fileRelationdao.save(uploadRelation);

        return new Result(true, "保存成功");
    }

    public Result del(String fileId) {
        if (SysUtil.isEmpty(fileId)) {
            return new Result(false, "id不能为空");
        }
        FileRelation uploadRelation = fileRelationdao.findByFileId(fileId);
        if (uploadRelation != null) {
            fileRelationdao.delete(uploadRelation);
        }

        return new Result(true, "成功");
    }
}
