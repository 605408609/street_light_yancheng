import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.em.util.HttpConnectionPools;
import org.apache.http.message.BasicHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)  //测试启动器，并加载spring boot测试注解
//标记该类为spring boot单元测试类，并加载项目的applicationContext上下文环境
@SpringBootTest
class MfpApplicationTests {
    private static Logger logger = LoggerFactory.getLogger(MfpApplicationTests.class);
    @Autowired
    private HttpConnectionPools httpConnectionPools;
//    @Test
//    void HttpClientTest() {
//        logger.debug("===========>");
//        String url = "http://127.0.0.1:8080/iot/api/files/ts1/videos";
////        {"files":[{"fileId":0,"filePath":"/programV/1014红色国旗1845679.mp4","progress":0,"scheduledTime":0,"sendType":0,"sn":"45679"}],"type":1,"userId":"1"}
//        Parameters requestObject = new Parameters();
//        List<ReturnData> filesList = new ArrayList<>();
//        String filePath = null;
//
//
//        // 文件对象
//        ReturnData files = new ReturnData();
//        // 文件路径名称
//        filePath = "/programV/1014红色国旗1845679.mp4";
//        files.setFilePath(filePath);
//        files.setSn("45679");
//        filesList.add(files);
//
//        requestObject.setFiles(filesList);
//        requestObject.setUserId("1");
//        requestObject.setType(1);
//
//        // create custom http headers for httpclient
//        List<Header> defaultHeaders = Arrays.asList(
//                new BasicHeader("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicGhvbmUiOiIxNTk4ODg4ODg4OCIsImlhdCI6MTYwMzA4MDkyOCwiZXhwIjoxNjAzMTY3MzI4fQ.j1o_c7vPJUpgziab8kyZc16a5tUY8HkRcFTfdgoFIlc"));
//
//        BasicHeader basicHeader = new BasicHeader("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicGhvbmUiOiIxNTk4ODg4ODg4OCIsImlhdCI6MTYwMzA4MDkyOCwiZXhwIjoxNjAzMTY3MzI4fQ.j1o_c7vPJUpgziab8kyZc16a5tUY8HkRcFTfdgoFIlc");
//
//
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicGhvbmUiOiIxNTk4ODg4ODg4OCIsImlhdCI6MTYwMzA4MDkyOCwiZXhwIjoxNjAzMTY3MzI4fQ.j1o_c7vPJUpgziab8kyZc16a5tUY8HkRcFTfdgoFIlc";
////        String post = HttpConnectionPoolUtil.post(url, requestObject, token);
//        String post = HttpConnectionPoolUtil.post(url, requestObject, basicHeader);
//        logger.debug("============> post:{}",post);
//    }
//
//    @Test
//    void jsonTest(){
//
//        Parameters progressInfo = new Parameters();
//        progressInfo.setType(ConstantsProcessUtil.WEBSOCKET_PROGRESS_TYPE_CLOSE_PAGE);
//        ReturnData data = new ReturnData();
//        data.setUserId("1");
//        progressInfo.setData(data);
////        System.out.println("jackson ===> "+JacksonUtil.toJsonString(progressInfo));
//        System.out.println("fastjson ===> "+ JSONObject.toJSONString(progressInfo));
//
//     String json1 =    "{\"returnCode\":0,\"returnMsg\":{\"errCode\":\"\",\"errDesc\":\"\"}}";
//    String json2 = " {\"code\":401,\"message\":\"请进行登录认证后再访问\"}";
//
//        Result result = JSONObject.parseObject(json2, Result.class);
//        Integer code = result.getCode();
//        Integer returnCode = result.getReturnCode();
//
//        if (code!=null && code == 401){
//            logger.error("token失效");
//        }
//
//        if (returnCode!=null && returnCode == 0){
//            logger.info("节目下发成功,json:{}",json2);
//        }else {
//            logger.error("节目下发异常,json:{}",json2);
//        }


//        Result jsonObject2 = JSONObject.parseObject(json2, Result.class);
//        Integer code2 = jsonObject2.getCode();
//        Object returnCode2 = jsonObject2.getReturnCode();
//        logger.debug("jsonObject2:{},Code:{}, ReturnCode:{}",jsonObject2,code2,returnCode2);
//
//        logger.debug("jackson1:===> {}",JacksonUtil.getCode(json1));
//        logger.debug("jackson2:===> {}",JacksonUtil.getCode(json2));
//    }

    //        @Test
//    void ffmpegTest() {
////            String ip = "148.70.214.239";
//            String ip = "192.168.10.162";
////        	 String ip = "192.168.111.129";
//            String cmd = AssemblyLinuxUtil.getCheckFfmpeg();
//            String result = ShellUtil.exec("检测服务器是否安装FFmpeg软件?", ip, cmd);
//            logger.debug("ffmpegTest result:=====> "+result);
//        }
//
//    @Test
//    void rsyncTest() {
////        String ip = "148.70.214.239";
//        String ip = "192.168.111.165";
//
//        String title = "检测rsync服务,从远程(ip:"+ip+")同步到本地";
//        ConfServer confServer = new ConfServer();
//        confServer.setSshPort(22);
//        confServer.setCentosUsername("root");
//        confServer.setFtpOriginPath("/mnt");
//
//        String preRsync = ShellUtil.getPreRsync(confServer);
////        String cmd = "rsync -av --progress -e 'ssh -p 22' "+username+"root@192.168.111.165:/mnt /mnt";
//        String cmd = preRsync + confServer.getCentosUsername() + "@" + ip + ":" + confServer.getFtpOriginPath() + " " + confServer.getFtpOriginPath();
//        String result = ShellUtil.exec(title,ip,cmd);
//        logger.debug("rsyncTest result:=====> {}",result);
//    }

//    @Test
//    void sshTest() {
//        List<ProcessingServerDTO> listProcess = confServer.getListProcess();
//        String cmd = FfmpegUtil.getFfmpegVision();
//        for (ProcessingServerDTO processingServerDTO: listProcess){
//            String host = processingServerDTO.getIp();
//            String result = JschUtil.exe(host, cmd, confRemote);
//            if (null != result){
//                FfmpegUtil.getFmpegInstallState(result,host,cmd);
//            }
//        }
//
//        String host = confServer.getFtpIp();
//        String result = JschUtil.exe(host, cmd, confRemote);
//        if (null != result){
//            FfmpegUtil.getFmpegInstallState(result,host,cmd);
//        }
//    }

    @Test
    void sshTest() {
        Float f = 1.21f;
        List<String> numList = new ArrayList<>();
        numList.add("y60-720-30497");
        String prototype = "温度："+f+",湿度："+f+",气压："+f+",质量："+f;
        Map<String,String> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isAll",0);
        jsonObject.put("num",-1);
        jsonObject.put("align","top");
        jsonObject.put("direction","left");
        jsonObject.put("html","<i style='background:#000;color:#FFF'>" + prototype + "</i>");
        jsonObject.put("prototype", prototype);
        jsonObject.put("interval",50);
        jsonObject.put("step",1);
        jsonObject.put("numList",numList);
        System.out.println("11111111发送到显示屏数据：{}"+jsonObject.toString());

        String s = jsonObject.toJSONString();
//        Properties initProp = new Properties(System.getProperties());
//        System.out.println("当前系统编码:" + initProp.getProperty("file.encoding"));
//        System.out.println("当前系统语言:" + initProp.getProperty("user.language"));
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        System.out.println("11111111发送到显示屏数据：{}"+jsonObject.toString());
//        JSONObject registerResult = JSON.parseObject(HttpUtil.post("http://192.168.111.110:60024" + "/api/ir/screen/subtitle/send", s, headMap));
//        System.out.println("2222222222显示屏返回：{}"+registerResult.toString());



        String url = "http://192.168.111.110:60024" + "/api/ir/screen/subtitle/send";

        BasicHeader basicHeader = new BasicHeader("Content-Type","application/json;charset=UTF-8");

        String jsonResult = httpConnectionPools.post(url, jsonObject, basicHeader);
        System.out.println("2222222222显示屏返回：{}"+jsonResult);
    }
}
