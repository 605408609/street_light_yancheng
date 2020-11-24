package com.exc.street.light.dlm.service;


import com.exc.street.light.resource.vo.ec.TableStructure;

import java.util.List;

public interface TableStructureService {

    /**
     * 获取表结构信息
     * @param table
     * @return
     */
    List<TableStructure> getTableStructure(String table);
}
