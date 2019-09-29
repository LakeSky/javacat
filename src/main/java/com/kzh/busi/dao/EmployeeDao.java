package com.kzh.busi.dao;

import com.kzh.busi.model.Employee;
import com.kzh.sys.core.dao.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDao extends GenericRepository<Employee, String> {

}
