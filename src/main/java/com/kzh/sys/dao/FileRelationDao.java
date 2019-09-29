package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.FileRelation;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface FileRelationDao extends GenericRepository<FileRelation, String> {
    List<FileRelation> findByObjId(String objId);

    List<FileRelation> findByObjIdIn(Collection<String> objIds);

    FileRelation findByFileId(String fileId);
}
