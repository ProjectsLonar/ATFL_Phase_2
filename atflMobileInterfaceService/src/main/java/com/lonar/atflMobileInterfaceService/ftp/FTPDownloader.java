package com.lonar.atflMobileInterfaceService.ftp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPDownloader {

    FTPClient ftp = null;

    public FTPDownloader(String host, String user, String pwd) throws Exception {
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftp.connect(host);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.login(user, pwd);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
    }

    public void downloadFile(String remoteFilePath, String localFilePath) {
        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
            this.ftp.retrieveFile(remoteFilePath, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    }

    public static void main123(String[] args) {
        try {
            downloadFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String downloadFile() throws Exception {
		FTPDownloader ftpDownloader =
		    new FTPDownloader("14.140.146.186", "ftp_siebmob", "!ncep@!10n");
		ftpDownloader.downloadFile("/sdevappl/sblmobapp/Main/Incremental/outlet111.CSV", "outlet111.CSV");
		System.out.println("FTP File downloaded successfully");
		ftpDownloader.disconnect();
		return "FTP File downloaded successfully";
	}
    
    
}


