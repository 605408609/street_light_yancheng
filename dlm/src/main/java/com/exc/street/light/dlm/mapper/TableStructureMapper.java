package com.exc.street.light.dlm.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.vo.ec.TableStructure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TableStructureMapper extends BaseMapper<TableStructure> {

    List<TableStructure> getTableStructure(@Param("table") String table);
}
