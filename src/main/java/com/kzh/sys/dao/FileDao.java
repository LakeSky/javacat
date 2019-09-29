package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.SysFile;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by xinyu.qiu on 2017/2/25.
 */
@Repository
public interface FileDao extends GenericRepository<SysFile, String> {

    SysFile findByFileKey(String fileKey);

    List<SysFile> findByIdIn(Collection<String> fileIds);

    List<SysFile> findByFileKeyIn(Set<String> fileKey);

    List<SysFile> findByIdInAndUsername(Set<Long> ids, String username);

    List<SysFile> findByIdInOrderBySeqAsc(Collection<String> fileIds);
}
