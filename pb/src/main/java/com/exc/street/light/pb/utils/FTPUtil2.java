package com.exc.street.light.pb.utils;

import org.apache.commons.io.monitor.FileEntry;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FTPUtil2 {
    /**
     * 本地字符编码
     */
    private static String LOCAL_CHARSET = "GBK";
    // FTP协议里面，规定文件名编码为iso-8859-1
    private static String SERVER_CHARSET = "ISO8859-1";

    /**
     * 文件上传
     *
     * @param ip         ftp服务器的ip地址
     * @param port       ftp服务器的端口
     * @param username   用户名
     * @param password   密码
     * @param uploadPath 目标路径
     * @param file       被上传的文件
     * @return 上传成功返回true
     */
    public static boolean upload(String ip, int port, String username, String password, String uploadPath, File file) {
        boolean result = false;
        FTPClient ftpClient = getFTPClient(ip, port, username, password);
        if (ftpClient == null) {
            file.delete();
            return result;
        }
        try {
            if (uploadPath.startsWith("/")) {
                uploadPath = uploadPath.substring(1);
            }
            String[] paths = uploadPath.split("/");
            for (String path : paths) {
                if (!ftpClient.changeWorkingDirectory(path)) {
                    ftpClient.makeDirectory(path);
                }
                ftpClient.changeWorkingDirectory(path);
            }
            String fileName = file.getName();
            fileName = new String(fileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            result = ftpClient.storeFile(fileName, in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(ftpClient);
        }
        return result;
    }

    /**
     * 单文件下载
     *
     * @param ip           ftp服务器的ip地址
     * @param port         ftp服务器的端口
     * @param username     用户名
     * @param password     密码
     * @param response     响应对象
     * @param fileName     被下载的文件名
     * @param downloadPath 被下载文件在ftp上的路径
     * @return 下载成功返回true
     */
    public static boolean download(String ip, int port, String username, String password, HttpServletResponse response, String fileName, String downloadPath) {
        boolean result = false;
        FTPClient ftpClient = getFTPClient(ip, port, username, password);
        if (ftpClient == null) {
            return result;
        }
        try {
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
            ftpClient.changeWorkingDirectory(downloadPath);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.getName().equals(fileName)) {
                    BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                    result = ftpClient.retrieveFile(new String(file.getName().getBytes(LOCAL_CHARSET), SERVER_CHARSET), out);
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(ftpClient);
        }
        return result;
    }

    /**
     * 删除文件
     *
     * @param ip         ftp服务器的ip地址
     * @param port       ftp服务器的端口
     * @param username   用户名
     * @param password   密码
     * @param fileName   被删除的文件名
     * @param serverPath 被删除文件在ftp上的路径
     * @return 删除成功返回true
     */
    public static boolean remove(String ip, int port, String username, String password, String fileName, String serverPath) {
        boolean result = false;
        FTPClient ftpClient = getFTPClient(ip, port, username, password);
        if (ftpClient == null) {
            return result;
        }
        try {
            if (!serverPath.endsWith("/")) {
                serverPath += "/";
            }
            ftpClient.changeWorkingDirectory(serverPath);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.getName().equals(fileName)) {
                    result = ftpClient.deleteFile(serverPath + new String(file.getName().getBytes(LOCAL_CHARSET), SERVER_CHARSET));
                    break;
                }
            }
            boolean flag = removeDirectory(ftpClient, serverPath);
            if (!(result && flag)) {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(ftpClient);
        }
        return result;
    }


    /**
     * 网页直接显示ftp上的图片
     *
     * @param ip         ftp服务器的ip地址
     * @param port       ftp服务器的端口
     * @param username   用户名
     * @param password   密码
     * @param response   响应对象
     * @param fileName   图片名称
     * @param serverPath ftp上的路径
     */
    public static void showImage(String ip, int port, String username, String password, HttpServletResponse response, String fileName, String serverPath) {
        FTPClient ftpClient = getFTPClient(ip, port, username, password);
        BufferedInputStream in;
        BufferedOutputStream out;
        if (ftpClient == null) {
            return;
        }
        try {
            response.setContentType("image/*");
            out = new BufferedOutputStream(response.getOutputStream());
            ftpClient.changeWorkingDirectory(serverPath);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.getName().equals(fileName)) {
                    in = new BufferedInputStream(ftpClient.retrieveFileStream(new String(file.getName().getBytes(LOCAL_CHARSET), SERVER_CHARSET)));
                    if (in == null) {
                        return;
                    }
                    byte[] buff = new byte[1024 * 10];
                    int len;
                    while ((len = in.read(buff)) != -1) {
                        out.write(buff, 0, len);
                    }
                    in.close();
                    out.close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(ftpClient);
        }
    }

    public static void main(String[] args) {
        try {
            FTPClient ftp = new FTPClient();
            //ftp.connect("192.168.111.11",2125);
            ftp.connect("129.204.186.242",2125);
            System.out.println("ftp = " + ftp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    获取连接
     */
    private static FTPClient getFTPClient(String ip, int port, String username, String password) {
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(ip, port);
            ftp.login(username, password);
            int replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftp.disconnect();
                return null;
            }
            // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            int command = ftp.sendCommand("OPTS UTF8", "ON");
            if (FTPReply.isPositiveCompletion(command)) {
                LOCAL_CHARSET = "UTF-8";
            }
            //设置ftp连接超时，单位毫秒，默认是0，即无限超时
            ftp.setDataTimeout(120000);
            ftp.setControlEncoding(LOCAL_CHARSET);
            // 设置被动模式
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftp;
    }

    /*
    关闭连接
     */
    private static boolean close(FTPClient ftpClient) {
        if (ftpClient == null) {
            return false;
        }
        try {
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /*
    递归删除文件夹
     */
    private static boolean removeDirectory(FTPClient ftpClient, String serverPath) {
        boolean result = true;
        try {
            FTPFile[] files = ftpClient.listFiles();
            if (files.length == 0) {
                if (serverPath.endsWith("/")) {
                    serverPath = serverPath.substring(0, serverPath.lastIndexOf("/"));
                }
                String[] paths = new String[2];
                paths[0] = serverPath.substring(0, serverPath.lastIndexOf("/"));
                paths[1] = serverPath.substring(serverPath.lastIndexOf("/") + 1);
                if (ftpClient.changeToParentDirectory()) {
                    ftpClient.removeDirectory(paths[1]);
                    if (!paths[0].isEmpty()) {
                        result = removeDirectory(ftpClient, paths[0]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
