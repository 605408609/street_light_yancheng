package com.exc.street.light.dlm.service.impl;


import com.exc.street.light.dlm.mapper.TableStructureMapper;
import com.exc.street.light.dlm.service.TableStructureService;
import com.exc.street.light.resource.vo.ec.TableStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TableStructureServiceImpl implements TableStructureService {

    @Autowired
    TableStructureMapper tableStructureMapper;

    @Override
    public List<TableStructure> getTableStructure(String table) {
        return tableStructureMapper.getTableStructure(table);
    }
}
