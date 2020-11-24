package com.exc.street.light.resource.vo.ec;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class TableStructure {

    /*
    表名
     */
    private String tableName;

    /*
    字段名
     */
    private String columnName;

    /*
    可否为空
     */
    private String nullable;

    /*
    字段类型
     */
    private String dataType;

    /*
    字段类型（长度）
     */
    private String columnType;



}
