package com.exc.street.light.pb.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FtpUtil {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    // ftp配置相关信息，可封装成一个对象，也可以做成配置文件
    private static FTPClient ftpClient = null;
    private static String hostname = "192.168.0.100";
    private static int port = 53100;
    private static String username = "zhangsan";
    private static String password = "123456";
    private static boolean isLogin = false;

	//FTP文件上传
	public static boolean uploadMP3File(String hostname, String username, String password, Integer port,
                                        String fileName, String originfilename) throws SocketException, IOException {

        InputStream inputStream = null;
        //把文件转化为流
        inputStream = new FileInputStream(new File(originfilename));
		//实例化ftpClient
		FTPClient ftpClient = new FTPClient();
		//设置登陆超时时间,默认是20s
		ftpClient.setDataTimeout(20000);
		//1.连接服务器
		ftpClient.connect(hostname, port);
		//2.登录（指定用户名和密码）
		boolean bool = ftpClient.login(username, password);
		//是否成功登录服务器
		if (!bool) {
			logger.info("登录失败！");
			if (ftpClient.isConnected()) {
				// 断开连接
				ftpClient.disconnect();
			}
			return false;
		}
		// 设置字符编码
		ftpClient.setControlEncoding("UTF-8");
		//文件上传路径为默认根目录
        //文件上传
        boolean uploadFlag = ftpClient.storeFile(fileName, inputStream);

		return uploadFlag;
	}

    //FTP文件上传
    public static String upload(String hostname, String username, String password, Integer port,
                                String targetPath, String suffix, InputStream inputStream) throws SocketException, IOException {
        //实例化ftpClient
        FTPClient ftpClient = new FTPClient();
        //设置登陆超时时间,默认是20s
        ftpClient.setDataTimeout(12000);
        //1.连接服务器
        ftpClient.connect(hostname, port);
        //2.登录（指定用户名和密码）
        boolean b = ftpClient.login(username, password);
        if (!b) {
            System.out.println("登录超时");
            if (ftpClient.isConnected()) {
                // 断开连接
                ftpClient.disconnect();
            }
        }
        // 设置字符编码
        ftpClient.setControlEncoding("UTF-8");
        //基本路径，一定存在
        String basePath = "/";
        String[] pathArray = targetPath.split("/");
        for (String path : pathArray) {
            basePath += path + "/";
            //3.指定目录 返回布尔类型 true表示该目录存在
            boolean dirExsists = ftpClient.changeWorkingDirectory(basePath);
            //4.如果指定的目录不存在，则创建目录
            if (!dirExsists) {
                //此方式，每次，只能创建一级目录
                boolean flag = ftpClient.makeDirectory(basePath);
                if (flag) {
                    System.out.println("创建成功！");
                }
            }
        }
        //重新指定上传文件的路径
        ftpClient.changeWorkingDirectory(targetPath);
        //5.设置上传文件的方式
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        //使用uuid，保存文件名唯一性
        String uuid = UUID.randomUUID().toString();
        /**
         * 6.执行上传
         * remote 上传服务后，文件的名称
         * local 文件输入流
         * 上传文件时，如果已经存在同名文件，会被覆盖
         */
        boolean uploadFlag = ftpClient.storeFile(uuid + suffix, inputStream);
        if (uploadFlag)
            System.out.println("上传成功！");
        return uuid + suffix;
    }

    public FtpUtil() {
        ftpClient = new FTPClient();
        // 连接FTP服务器
        try {
            ftpClient.connect(hostname, port);
            //设置文件传输类型,common-net版本低于2.2 只能在登录之前设置fileType，否则报连接超时
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 登录FTP服务器
            ftpClient.login(username, password);
            // 验证FTP服务器是否登录成功
            int replyCode = ftpClient.getReplyCode();
            logger.info("replyCode:" + replyCode);
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.info("登录失败！");
            } else {
                logger.info("登录成功！");
                isLogin = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FtpUtil newInstance() {
        return new FtpUtil();
    }


    /**
     * 下载ftp目录下的所有文件到指定目录
     *
     * @param localpath       当前目录
     * @param ftpDir          ftp服务器的目录
     * @param isCreateRootDir 是否创建指定的根目录
     *                        , List excelList
     */
    public void downloadDirFiles(String localpath, String ftpDir, boolean isCreateRootDir) {
        if (isLogin) {
            if (!isCreateRootDir) {
                isCreateRootDir = true;
            } else {
                localpath += "/" + ftpDir;
                localpath = localpath.replace("//", "/");
            }
            File dir = new File(localpath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                ftpClient.changeWorkingDirectory(ftpDir);
                FTPFile[] ftpFiles = ftpClient.listFiles();

                for (FTPFile file : ftpFiles) {
                    //防止中文乱码
                    String fileName = new String(file.getName().getBytes("iso8859-1"), "UTF-8");
                    logger.info("file:" + fileName);

                    if (file.isFile()) {
                        File targetFile = new File(localpath + "/" + fileName);
//						String targetFilePath =  targetFile.getAbsolutePath();
                        logger.info("targetFile：" + targetFile);
                        FileOutputStream os = new FileOutputStream(targetFile);

                        boolean flag = ftpClient.retrieveFile(fileName, os);
                        logger.info("下载文件-fileName：" + fileName + ",下载是否成功：" + flag);
                        IOUtils.closeQuietly(os);
//						if(targetFilePath.contains(".xls") || targetFilePath.contains(".xlsx")){
//							excelList.add(targetFilePath);
//						}
                    }
                    if (file.isDirectory()) {
                        // 目录 ,递归调用
                        downloadDirFiles(localpath, fileName + "/", isCreateRootDir);
                    }
                }
                //当前目录下载完成，返回上级目录
                if (!"".equals(ftpDir))
                    ftpClient.changeWorkingDirectory("..");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("登录ftp失败，请检查登录配置！");
        }
    }


    /**
     * 递归删除指定目录的所有文件，包括文件夹
     *
     * @param ftpDir
     * @return int  成功删除的记录
     */
    public int deleteDirFiles(String ftpDir) {
        int count = 0;
        try {
            ftpClient.changeWorkingDirectory(ftpDir);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                //防止中文乱码
                String fileName = new String(file.getName().getBytes("iso8859-1"), "UTF-8");
                if (file.isFile()) {
                    boolean f = ftpClient.deleteFile(fileName);
                    if (f) {
                        logger.info("删除文件-成功：" + fileName);
                    } else {
                        logger.info("删除文件-失败：" + fileName);
                        count++;
                    }
                } else if (file.isDirectory()) {
                    //递归调用
                    deleteDirFiles(fileName + "/");
                    boolean f = ftpClient.removeDirectory(fileName);
                    if (f) {
                        logger.info("删除文件夹-成功：" + fileName);
                        count++;
                    } else {
                        logger.info("删除文件夹-失败：" + fileName);
                    }
                }
            }
            //当前目录删除完成，返回上级目录
            if (!"".equals(ftpDir)) {
                ftpClient.changeWorkingDirectory("..");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }


    /**
     * 读取指定目录下的所有文件文件名
     *
     * @param pathname
     * @return
     * @throws IOException
     */

    public static List getFileList(String pathname) {
        List fileLists = new ArrayList();
        if (isLogin) {
            // 获得指定目录下所有文件名
            FTPFile[] ftpFiles = null;
            try {
                ftpFiles = ftpClient.listFiles(pathname);
                for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
                    FTPFile file = ftpFiles[i];
                    if (file.isFile()) {
                        fileLists.add(new String(file.getName().getBytes("iso8859-1"), "UTF-8"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("登录ftp失败，请检查登录配置！");
        }
        logger.info("fileLists:" + fileLists.toString());
        return fileLists;
    }


    /**
     * 退出登录
     */
    public void logout() {
        if (ftpClient.isConnected()) {
            try {
                if (ftpClient != null)
                    ftpClient.logout();
                logger.info("退出登录！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        uploadMP3File("192.168.111.170", "websvr", "webcast", 2125, "4fc7af10878811ea8054b42e9907f6b0.mp3", "D:\\360安全浏览器下载\\一生有你 - 测试上传.mp3");

//        //指定ftp目录
//        String ftpPath = "/";
//        FtpUtil ftpUtil = FtpUtil.newInstance();
//        getFileList("/");
//// 	        ftpUtil.downloadDirFiles("D:\\downFtpPath",ftpPath,true);
//
//        String fileName = "小海鱼.mov";
//        File targetFile = new File("/home" + "/" + fileName);
//        logger.info("targetFile：" + targetFile);
//        OutputStream os = null;
//        try {
//            os = new FileOutputStream(targetFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        boolean flag;
//        try {
////				ftpClient.storeFile(fileName, os);
//            flag = ftpClient.retrieveFile(fileName, os);
//            if (flag) {
//                logger.info("文件：fileName；" + "下载成功");
//            } else {
//                logger.info("文件：fileName；" + "下载失败");
//            }
//
//            IOUtils.closeQuietly(os);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////	        ftpUtil.deleteDirFiles(pathname);
//        ftpUtil.logout();
    }

}
