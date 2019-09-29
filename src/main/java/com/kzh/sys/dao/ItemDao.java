package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDao extends GenericRepository<Item, String> {

}
