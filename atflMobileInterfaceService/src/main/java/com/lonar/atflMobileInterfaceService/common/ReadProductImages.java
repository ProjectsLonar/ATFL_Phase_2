package com.lonar.atflMobileInterfaceService.common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.lonar.atflMobileInterfaceService.ftp.FTPUtil;
import com.lonar.atflMobileInterfaceService.thread.InitiateThread;

public class ReadProductImages {

	public FTPFile[] getAllProductImages() {
		FTPUtil ftpUtil = new FTPUtil();
		FTPFile fileDetails[] = ftpUtil.getImagesList(InitiateThread.configMap.get("Public_IP").getValue(),
				InitiateThread.configMap.get("FTP_Port").getValue(),
				InitiateThread.configMap.get("User_Name").getValue(),
				InitiateThread.configMap.get("Password").getValue(),
				InitiateThread.configMap.get("product_images_src_file_path").getValue());
		return fileDetails;
	}

	public String getProductImage(String productCode, FTPFile filesList[]) {

		FTPUtil ftpUtil = new FTPUtil();

		for (FTPFile file : filesList) {

			String[] arrOfStr = file.getName().toString().split("_");

			for (String fileProductCode : arrOfStr) {

				if (fileProductCode.equalsIgnoreCase(productCode)) {

					String pCode = "P_" + productCode;

					if (file.getName().toUpperCase().contains(pCode.toUpperCase())) {

						// Download file
						Boolean downloadBoolean = ftpUtil.downloadImages(
								InitiateThread.configMap.get("Public_IP").getValue(),
								InitiateThread.configMap.get("FTP_Port").getValue(),
								InitiateThread.configMap.get("User_Name").getValue(),
								InitiateThread.configMap.get("Password").getValue(),
								InitiateThread.configMap.get("product_images_src_file_path").getValue() + "/"
										+ file.getName(),
								InitiateThread.configMap.get("Product_Image_Des_File_Path").getValue() + "//"
										+ file.getName());

						// upload file from FTP

						Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
								InitiateThread.configMap.get("FTP_Port").getValue(),
								InitiateThread.configMap.get("User_Name").getValue(),
								InitiateThread.configMap.get("Password").getValue(),
								InitiateThread.configMap.get("product_images_upload_path").getValue() + "/"
										+ file.getName(),
								InitiateThread.configMap.get("Product_Image_Des_File_Path").getValue() + "/"
										+ file.getName());
						// delete file from FTP
						Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
								InitiateThread.configMap.get("FTP_Port").getValue(),
								InitiateThread.configMap.get("User_Name").getValue(),
								InitiateThread.configMap.get("Password").getValue(),
								InitiateThread.configMap.get("product_images_src_file_path").getValue() + "/"
										+ file.getName());

						if (deleteBoolean) {
							return file.getName();
						}
					}

				}
			}

		}

		return null;
	}

	public String getThumbnailImage(String productCode, FTPFile filesList[]) {

		FTPUtil ftpUtil = new FTPUtil();
		// productCode = "PT_" + productCode;

		for (FTPFile file : filesList) {

			String[] arrOfStr = file.getName().toString().split("_");

			for (String fileProductCode : arrOfStr) {

				if (fileProductCode.equalsIgnoreCase(productCode)) {

					String pCode = "PT_" + productCode;

					if (file.getName().toUpperCase().contains(pCode.toUpperCase())) {
						// Download file
						Boolean downloadBoolean = ftpUtil.downloadImages(
								InitiateThread.configMap.get("Public_IP").getValue(),
								InitiateThread.configMap.get("FTP_Port").getValue(),
								InitiateThread.configMap.get("User_Name").getValue(),
								InitiateThread.configMap.get("Password").getValue(),
								InitiateThread.configMap.get("product_images_src_file_path").getValue() + "/"
										+ file.getName(),
								InitiateThread.configMap.get("Product_Image_Des_File_Path").getValue() + "//"
										+ file.getName());

						// upload file from FTP

						Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
								InitiateThread.configMap.get("FTP_Port").getValue(),
								InitiateThread.configMap.get("User_Name").getValue(),
								InitiateThread.configMap.get("Password").getValue(),
								InitiateThread.configMap.get("product_images_upload_path").getValue() + "/"
										+ file.getName(),
								InitiateThread.configMap.get("Product_Image_Des_File_Path").getValue() + "/"
										+ file.getName());
						// delete file from FTP
						Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
								InitiateThread.configMap.get("FTP_Port").getValue(),
								InitiateThread.configMap.get("User_Name").getValue(),
								InitiateThread.configMap.get("Password").getValue(),
								InitiateThread.configMap.get("product_images_src_file_path").getValue() + "/"
										+ file.getName());
						if (deleteBoolean) {
							return file.getName();
						}
					}
				}
			}
		}
		return null;
	}

	public boolean archivedIncrementaFaildlImages() throws IOException {

		SimpleDateFormat dnt = new SimpleDateFormat("dd-MM-yyyy");
		Date date = Validation.getCurrentDateTime();
		boolean success = false;

		String dirPath = InitiateThread.configMap.get("Import_FTP_Image_Failed_Path").getValue() + "/"
				+ dnt.format(date);

		String localPath = InitiateThread.configMap.get("Import_Local_Image_Failed_Path").getValue() + "//"
				+ dnt.format(date);

		File directory = new File(localPath);
		if (!directory.exists()) {
			directory.mkdir();
		}
		if (directory.exists()) {
			success = checkDirectoryExists(dirPath);
			if (success) {
				// Transfer Data
				success = transferFailedImage(dirPath, localPath);
			} else {
				success = makeDirectory(dirPath);
				if (success) {
					// transfer Data
					success = transferFailedImage(dirPath, localPath);
				}
			}

		}
		return success;
	}

	boolean checkDirectoryExists(String dirPath) throws IOException {
		FTPClient ftpClient = new FTPClient();
		int returnCode;
		String server = InitiateThread.configMap.get("Public_IP").getValue();
		int port = Integer.parseInt(InitiateThread.configMap.get("FTP_Port").getValue());
		String user = InitiateThread.configMap.get("User_Name").getValue();
		String pass = InitiateThread.configMap.get("Password").getValue();
		try {
			ftpClient.connect(server, port);
			showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Operation failed. Server reply code: " + replyCode);
				return false;
			}
			boolean success = ftpClient.login(user, pass);
			showServerReply(ftpClient);
			if (!success) {
				System.out.println("Could not login to the server");
				return success;
			}

			ftpClient.changeWorkingDirectory(dirPath);
			returnCode = ftpClient.getReplyCode();
			// logs out
			ftpClient.logout();
			ftpClient.disconnect();
			if (returnCode == 550) {
				return false;
			}
		} catch (IOException ex) {
			System.out.println("Oops! Something wrong happened");
			ex.printStackTrace();
		}
		return true;
	}

	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}

	public boolean makeDirectory(String dirPath) {
		String server = InitiateThread.configMap.get("Public_IP").getValue();
		int port = Integer.parseInt(InitiateThread.configMap.get("FTP_Port").getValue());
		String user = InitiateThread.configMap.get("User_Name").getValue();
		String pass = InitiateThread.configMap.get("Password").getValue();
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(server, port);
			showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Operation failed. Server reply code: " + replyCode);
				return false;
			}
			boolean success = ftpClient.login(user, pass);
			showServerReply(ftpClient);
			if (!success) {
				System.out.println("Could not login to the server");
				return success;
			}
			// Creates a directory
			String dirToCreate = dirPath;
			success = ftpClient.makeDirectory(dirToCreate);
			showServerReply(ftpClient);
			if (success) {
				System.out.println("Successfully created directory: " + dirToCreate);
			} else {
				System.out.println("Failed to create directory. See server's reply.");
			}
			// logs out
			ftpClient.logout();
			ftpClient.disconnect();
			return success;
		} catch (IOException ex) {
			System.out.println("Oops! Something wrong happened");
			ex.printStackTrace();
		}
		return false;
	}

	boolean transferFailedImage(String dirPath, String localPath) {

		FTPFile productImagesList[] = getAllProductImages();
		FTPUtil ftpUtil = new FTPUtil();
		boolean success = false;
		for (FTPFile file : productImagesList) {

			if (file.getName().toUpperCase().endsWith(".JPG")) {

				// Download file
				Boolean downloadBoolean = ftpUtil.downloadImages(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/" + file.getName(),
						localPath + "/" + file.getName());

				// upload file from FTP
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(), dirPath + "/" + file.getName(),
						localPath + "/" + file.getName());

				// delete file from FTP
				if (uploadBoolean) {

					Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
							InitiateThread.configMap.get("FTP_Port").getValue(),
							InitiateThread.configMap.get("User_Name").getValue(),
							InitiateThread.configMap.get("Password").getValue(),
							InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/" + file.getName());

					if (deleteBoolean) {
						success = true;
					}

				}

			}

		}
		return success;

	}

	public static void main1(String args[]) throws IOException {
		ReadProductImages obj = new ReadProductImages();
		FTPFile filesList[] = obj.getAllProductImages();
		for (FTPFile file : filesList) {
			System.out.println("File name: " + file.getName());
			System.out.println("File path: " + file.getLink());
			System.out.println("Size :" + file.getLink());
			System.out.println(" ");
		}
	}
}
