package com.lostcanteen.deliciouscanteen;

/**
 * Created by yw199 on 2017/11/10.
 */

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FTP {
    private String hostName ;
    private int serverPort ;
    private String userName ;
    private String password ;
    private FTPClient ftpClient ;

    public FTP() {
        this.hostName = "ftp.sinacloud.com";
        this.serverPort = 10021;
        this.userName = "j3wk0354w1";
        this.password = "53j4yy4yimwix40hy1hy05kwj414mmk112j22j5k";
        this.ftpClient = new FTPClient();
    }

    public  void uploadSingleFile(File singleFile, String remotePath,
                                  UploadProgressListener listener) throws IOException {

        // 上传之前初始化
        this.uploadBeforeOperate(remotePath, listener);

        boolean flag;
        flag = uploadingSingle(singleFile, listener);
        /*if (flag) {
            listener.onUploadProgress(MainActivity.FTP_UPLOAD_SUCCESS, 0,
                    singleFile);
        } else {
            listener.onUploadProgress(MainActivity.FTP_UPLOAD_FAIL, 0,
                    singleFile);
        }*/

        // 上传完成之后关闭连接
        this.uploadAfterOperate(listener);
    }


    /**
     * 上传单个文件.
     *
     * @param localFile
     *            本地文件
     * @return true上传成功, false上传失败
     * @throws IOException
     */
    private boolean uploadingSingle(File localFile,
                                    UploadProgressListener listener) throws IOException {
        boolean flag = true;
        // 不带进度的方式
        // // 创建输入流
        // InputStream inputStream = new FileInputStream(localFile);
        // // 上传单个文件
        // flag = ftpClient.storeFile(localFile.getName(), inputStream);
        // // 关闭文件流
        // inputStream.close();

        // 带有进度的方式
        BufferedInputStream buffIn = new BufferedInputStream(
                new FileInputStream(localFile));
        ProgressInputStream progressInput = new ProgressInputStream(buffIn,
                listener, localFile);
        flag = ftpClient.storeFile(localFile.getName(), progressInput);
        buffIn.close();

        return flag;
    }

    /**
     * 上传文件之前初始化相关参数
     *
     * @param remotePath
     *            FTP目录
     * @param listener
     *            监听器
     * @throws IOException
     */
    private void uploadBeforeOperate(String remotePath,
                                     UploadProgressListener listener) throws IOException {

        this.openConnect();
        // 打开FTP服务
        /*try {

            listener.onUploadProgress(MainActivity.FTP_CONNECT_SUCCESSS, 0,
                    null);
        } catch (IOException e1) {
            e1.printStackTrace();
            listener.onUploadProgress(MainActivity.FTP_CONNECT_FAIL, 0, null);
            return;
        }*/

        // 设置模式
        ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
        // FTP下创建文件夹
        ftpClient.makeDirectory(remotePath);
        // 改变FTP目录
        ftpClient.changeWorkingDirectory(remotePath);
        // 上传单个文件

    }

    /**
     * 上传完成之后关闭连接
     *
     * @param listener
     * @throws IOException
     */
    private void uploadAfterOperate(UploadProgressListener listener)
            throws IOException {
        this.closeConnect();
        //listener.onUploadProgress(MainActivity.FTP_DISCONNECT_SUCCESS, 0, null);
    }


    // -------------------------------------------------------打开关闭连接------------------------------------------------

    /**
     * 打开FTP服务.
     *
     * @throws IOException
     */
    public void openConnect() throws IOException {
        // 中文转码
        ftpClient.setControlEncoding("UTF-8");
        int reply; // 服务器响应值
        // 连接至服务器
        ftpClient.connect(hostName, serverPort);
        // 获取响应值
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            ftpClient.disconnect();
            throw new IOException("connect fail: " + reply);
        }
        // 登录到服务器
        ftpClient.login(userName, password);
        // 获取响应值
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            ftpClient.disconnect();
            throw new IOException("connect fail: " + reply);
        } else {
            // 获取登录信息
            FTPClientConfig config = new FTPClientConfig(ftpClient
                    .getSystemType().split(" ")[0]);
            config.setServerLanguageCode("zh");
            ftpClient.configure(config);
            // 使用被动模式设为默认
            ftpClient.enterLocalPassiveMode();
            // 二进制文件支持
            ftpClient
                    .setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        }
    }

    /**
     * 关闭FTP服务.
     *
     * @throws IOException
     */
    public void closeConnect() throws IOException {
        if (ftpClient != null) {
            // 退出FTP
            ftpClient.logout();
            // 断开连接
            ftpClient.disconnect();
        }
    }

    // ---------------------------------------------------上传、下载、删除监听---------------------------------------------

    /*
     * 上传进度监听
     */
    public interface UploadProgressListener {
        public void onUploadProgress(String currentStep, long uploadSize, File file);
    }

    /*
     * 下载进度监听
     */
    public interface DownLoadProgressListener {
        public void onDownLoadProgress(String currentStep, long downProcess, File file);
    }

    /*
     * 文件删除监听
     */
    public interface DeleteFileProgressListener {
        public void onDeleteProgress(String currentStep);
    }



}
