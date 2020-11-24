/**
 * @filename: MyGenerator.java 2019-10-16
 * @project v0.0.1  V1.0
 * Copyright(c) 2018 BianPeng Co. Ltd.
 * All right reserved.
 */
package mdg;

import com.exc.street.light.resource.entity.BasisInfo;
import com.exc.street.light.resource.utils.EntityInfoUtil;
import com.exc.street.light.resource.utils.Generator;
import com.exc.street.light.resource.utils.MySqlToJavaUtil;
import freemarker.template.SimpleDate;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Copyright: Copyright (c) 2019
 *
 * <p>说明： 自动生成工具</P>
 * <p>源码地址：https://gitee.com/flying-cattle/mybatis-dsc-generator</P>
 */
public class MyGenerator {

    // 基础信息：项目名、作者、版本
//	public static final String PROJECT = "dlm";
//    public static final String AUTHOR = "Longshuangyang";
    public static final String VERSION = "V1.0";
    // 数据库连接信息：连接URL、用户名、秘密、数据库名
    public static final String URL = "jdbc:mysql://192.168.111.89:3306/street_light_3.0?useUnicode=true&characterEncoding=UTF-8&generateSimpleParameterMetadata=true&serverTimezone=Asia/Shanghai";
    public static final String NAME = "root";
    public static final String PASS = "Exc_led1";
    public static final String DATABASE = "street_light_3.0";
    // Controller 前缀
//    public static final String API_URL_PREFIX = "api/sl/sl/device";
    // 类信息：类名、对象名（一般是【类名】的首字母小些）、类说明、时间
    public static final String CLASSNAME = "sl_device";
    //	public static final String TABLE = "sl_device";
//    public static final String CLASSCOMMENT = "设备";
    public static final Date TIME = new Date();
    public static final String AGILE = new Date().getTime() + "";
    // 路径信息，分开路径方便聚合工程项目，微服务项目
//    public static final String ENTITY_URL = "com.exc.street.light.resource.entity.dlm";
//    public static final String CONTROLLER_URL = "com.exc.street.light.dlm.web";
//    public static final String SERVICE_URL = "com.exc.street.light.dlm.service";
//    public static final String SERVICE_IMPL_URL = "com.exc.street.light.dlm.service.impl";
//    public static final String DAO_URL = "com.exc.street.light.dlm.mapper";
//    public static final String XML_URL = "mapper";
    // 父类core路径
    public static final String PARENT_CORE_URL = "com.exc.street.light.resource.core";
    // 是否是Swagger配置
    public static final String IS_SWAGGER = "true";

    public static void main(String[] args) throws ParseException {
//        // 表名
        String[] tableArray = {"can_strategy_action"};
        // 项目编号
        String project = "electricity";

        // 作者
        String author = "Xiaok";
        // 类说明
        String classcomment = "路灯网关策略对应动作表";
        for (String table : tableArray) {
            start(table, project, author, classcomment);
        }
//        Integer volValue = 100;
//        Float maxVol = new Float(56);
//        Double v = volValue * maxVol * 0.01;
//        System.out.println(v.intValue());

    }

    public static void start(String TABLE, String PROJECT, String AUTHOR, String CLASSCOMMENT) {
        // 路径信息，分开路径方便聚合工程项目，微服务项目
        String ENTITY_URL = "com.exc.street.light.resource.entity." + PROJECT;
        String CONTROLLER_URL = "com.exc.street.light." + PROJECT + ".web";
        String SERVICE_URL = "com.exc.street.light." + PROJECT + ".service";
        String SERVICE_IMPL_URL = "com.exc.street.light." + PROJECT + ".service.impl";
        String DAO_URL = "com.exc.street.light." + PROJECT + ".mapper";
        String XML_URL = "mapper";
        // controller前缀
        String[] tableSplit = TABLE.split("_");
        String API_URL_PREFIX = "api/" + PROJECT;
        for (String s : tableSplit) {
            API_URL_PREFIX = API_URL_PREFIX + "/" + s;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(TIME);
        BasisInfo bi = new BasisInfo(PROJECT, AUTHOR, VERSION, URL, NAME, PASS, DATABASE, format, AGILE, ENTITY_URL,
                DAO_URL, XML_URL, SERVICE_URL, SERVICE_IMPL_URL, CONTROLLER_URL, IS_SWAGGER, API_URL_PREFIX, PARENT_CORE_URL);
        bi.setTable(TABLE);
        bi.setEntityName(MySqlToJavaUtil.getClassName(TABLE));
        bi.setObjectName(MySqlToJavaUtil.changeToJavaFiled(TABLE));
        bi.setEntityComment(CLASSCOMMENT);
        try {
            bi = EntityInfoUtil.getInfo(bi);
            // 生成文件存放位置
            String fileUrl = "D:\\Work1\\IDEA-workspace\\street_light\\" + PROJECT + "\\src\\main\\java\\";
            // xml文件存放位置
            String xmlFileUrl = "D:\\Work1\\IDEA-workspace\\street_light\\" + PROJECT + "\\src\\main\\resources\\";
            // Entity文件生成位置
            String entityFileUrl = "D:\\Work1\\IDEA-workspace\\street_light\\resource\\src\\main\\java\\";
            // 模板存放位置
            String freemarkerFileUrl = "D:\\Work1\\IDEA-workspace\\street_light\\resource\\src\\main\\java\\";
            //开始生成文件
            String aa1 = Generator.createEntity(entityFileUrl, bi).toString();
            String aa2 = Generator.createDao(fileUrl, bi).toString();
            String aa3 = Generator.createDaoImpl(xmlFileUrl, bi).toString();
            String aa4 = Generator.createService(fileUrl, bi).toString();
            String aa5 = Generator.createServiceImpl(fileUrl, bi).toString();
            String aa6 = Generator.createController(fileUrl, bi).toString();
            // 是否创建swagger配置文件
            String aa7 = Generator.createSwaggerConfig(fileUrl, bi).toString();

            System.out.println(aa1);
            System.out.println(aa2);
            System.out.println(aa3);
            System.out.println(aa4);
            System.out.println(aa5);
            System.out.println(aa6);
            System.out.println(aa7);

            //System.out.println(aa7);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
