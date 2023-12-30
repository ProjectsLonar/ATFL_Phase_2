package com.lonar.atflMobileInterfaceService.ftp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.lonar.atflMobileInterfaceService.common.Validation;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;

/**
 * An example program that demonstrates how to list files and directories on a
 * FTP server using Apache Commons Net API.
 * 
 * @author www.codejava.net
 */
public class FTPUtil {

	public Map<String, HashMap<Date, LtJobLogs>> getFileDetails(String server, String port1, String user, String pass,
			String filePath) {
		Map<String, HashMap<Date, LtJobLogs>> fileDetailsMap = new HashMap<String, HashMap<Date, LtJobLogs>>();
		FTPClient ftpClient = new FTPClient();
		try {
			int port = Integer.parseInt(port1);

			ftpClient.connect(server, port);

			showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Connect failed");
			}
			ftpClient.enterLocalPassiveMode();
			boolean success = ftpClient.login(user, pass);
			showServerReply(ftpClient);

			if (!success) {
				System.out.println("Could not login to the server");
				// return;
			}

			FTPFile[] files1 = ftpClient.listFiles(filePath);
			fileDetailsMap = printFileDetails(files1);

		} catch (IOException ex) {
			System.out.println("Oops! Something wrong happened");
			ex.printStackTrace();
		} finally {
			// logs out and disconnects from server
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return fileDetailsMap;
	}

	public FTPFile[] getImagesList(String server, String port1, String user, String pass, String filePath) {

		FTPClient ftpClient = new FTPClient();

		FTPFile[] files = null;

		try {
			int port = Integer.parseInt(port1);

			ftpClient.connect(server, port);
			showServerReply(ftpClient);

			int replyCode = ftpClient.getReplyCode();

			if (!FTPReply.isPositiveCompletion(replyCode)) {
				return null;
			}
			ftpClient.enterLocalPassiveMode();
			boolean success = ftpClient.login(user, pass);
			showServerReply(ftpClient);
			if (!success) {
				System.out.println("Could not login to the server");
				return null;
			}

			files = ftpClient.listFiles(filePath);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return files;
	}

	public Boolean deleteFile(String server, String port2, String user, String pass, String filePath) {
		Boolean delete = false;
		FTPClient ftpClient = new FTPClient();
		try {
			int port = 21;
			ftpClient.connect(server, port);
			showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Connect failed");
				// return;
			}
			ftpClient.enterLocalPassiveMode();
			boolean success = ftpClient.login(user, pass);
			showServerReply(ftpClient);

			if (!success) {
				System.out.println("Could not login to the server");
				// return;
			}
			System.out.println("FTP Delete File Nikhil====================================>");
			delete = ftpClient.deleteFile(filePath);

		} catch (IOException ex) {
			System.out.println("Oops! Something wrong happened");
			ex.printStackTrace();
		} finally {
			// logs out and disconnects from server
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return delete;
	}

	public Boolean downloadFile(String server, String port2, String user, String pass, String srcFTPFilePath,
			String destFilePath) {
		Boolean save = false;
		FTPClient ftpClient = new FTPClient();
		try {
			// server = "14.140.146.186";
			int port = Integer.parseInt(port2);
			// user = "ftp_siebmob";
			// pass = "!ncep@!10n";

			ftpClient.connect(server, port);
			showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Connect failed");
				// return;
			}
			ftpClient.enterLocalPassiveMode();
			boolean success = ftpClient.login(user, pass);
			showServerReply(ftpClient);

			if (!success) {
				System.out.println("Could not login to the server");
				// return;
			}
			FileOutputStream fos = new FileOutputStream(destFilePath);
			save = ftpClient.retrieveFile(srcFTPFilePath, fos);

		} catch (IOException ex) {
			System.out.println("Oops! Something wrong happened");
			ex.printStackTrace();
		} finally {
			// logs out and disconnects from server
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return save;
	}

	public Boolean downloadImages(String server, String port2, String user, String pass, String srcFTPFilePath,
			String destFilePath) {
		Boolean save = false;
		FTPClient ftpClient = new FTPClient();
		try {
			// server = "14.140.146.186";
			int port = Integer.parseInt(port2);
			// user = "ftp_siebmob";
			// pass = "!ncep@!10n";

			ftpClient.connect(server, port);
			showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Connect failed");
				// return;
			}
			ftpClient.enterLocalPassiveMode();
			boolean success = ftpClient.login(user, pass);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			showServerReply(ftpClient);

			if (!success) {
				System.out.println("Could not login to the server");
				// return;
			}
			FileOutputStream fos = new FileOutputStream(destFilePath);
			save = ftpClient.retrieveFile(srcFTPFilePath, fos);

		} catch (IOException ex) {
			System.out.println("Oops! Something wrong happened");
			ex.printStackTrace();
		} finally {
			// logs out and disconnects from server
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return save;
	}

	public Boolean uploadFile(String server, String port1, String user, String pass, String remoteFilePath,
			String localFileIS) {
		Boolean save = false;
		FTPClient ftpClient = new FTPClient();
		try {
			int port = Integer.parseInt(port1);
			ftpClient.connect(server, port);
			showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Connect failed");
			}
			ftpClient.enterLocalPassiveMode();
			boolean success = ftpClient.login(user, pass);
			showServerReply(ftpClient);

			if (!success) {
				System.out.println("Could not login to the server");
			}
			InputStream is = new FileInputStream(localFileIS);
			save = ftpClient.storeFile(remoteFilePath, is);

		} catch (IOException ex) {
			System.out.println("Oops! Something wrong happened");
			ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return save;
	}

	private Map<String, HashMap<Date, LtJobLogs>> printFileDetails(FTPFile[] files) {
		Map<String, HashMap<Date, LtJobLogs>> fileDetailsMap = new HashMap<String, HashMap<Date, LtJobLogs>>();
		for (FTPFile file : files) {
			if (!file.isDirectory()
					&& (file.getName().substring(file.getName().length() - 4).toUpperCase().equals(".CSV"))) {

				LtJobLogs ltJobeLogs = new LtJobLogs();
				ltJobeLogs.setFileName(file.getName());
				ltJobeLogs.setFileSize("" + file.getSize());

				String dateTime = file.getName().substring((file.getName().length() - 18),
						(file.getName().length() - 4));

				if (file.getName().contains("Distributors")) {
					ltJobeLogs.setJobType("Distributors");
					ltJobeLogs.setLogsStatus("CSV File Init");
					ltJobeLogs.setStartDate(Validation.getCurrentDateTime());
					ltJobeLogs.setEndDate(Validation.getCurrentDateTime());
					try {
						ltJobeLogs.setCsvDate(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (fileDetailsMap.get("Distributors") != null) {
						HashMap<Date, LtJobLogs> distributorsMap = fileDetailsMap.get("Distributors");
						try {
							distributorsMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Distributors", distributorsMap);
					} else {
						HashMap<Date, LtJobLogs> distributorsMap = new HashMap<Date, LtJobLogs>();
						try {
							distributorsMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Distributors", distributorsMap);
					}
				} else if (file.getName().contains("Positions")) {
					ltJobeLogs.setJobType("Positions");
					ltJobeLogs.setLogsStatus("CSV File Init");
					ltJobeLogs.setStartDate(Validation.getCurrentDateTime());
					ltJobeLogs.setEndDate(Validation.getCurrentDateTime());
					try {
						ltJobeLogs.setCsvDate(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (fileDetailsMap.get("Positions") != null) {
						HashMap<Date, LtJobLogs> positionsMap = fileDetailsMap.get("Positions");
						try {
							positionsMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Positions", positionsMap);
					} else {
						HashMap<Date, LtJobLogs> positionsMap = new HashMap<Date, LtJobLogs>();
						try {
							positionsMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Positions", positionsMap);
					}
				} else if (file.getName().contains("Employee")) {
					ltJobeLogs.setJobType("Employee");
					ltJobeLogs.setLogsStatus("CSV File Init");
					ltJobeLogs.setStartDate(Validation.getCurrentDateTime());
					ltJobeLogs.setEndDate(Validation.getCurrentDateTime());
					try {
						ltJobeLogs.setCsvDate(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (fileDetailsMap.get("Employee") != null) {
						HashMap<Date, LtJobLogs> employeeMap = fileDetailsMap.get("Employee");
						try {
							employeeMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Employee", employeeMap);
					} else {
						HashMap<Date, LtJobLogs> employeeMap = new HashMap<Date, LtJobLogs>();
						try {
							employeeMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Employee", employeeMap);
					}
				} else if (file.getName().contains("Outlets")) {
					ltJobeLogs.setJobType("Outlets");
					ltJobeLogs.setLogsStatus("CSV File Init");
					ltJobeLogs.setStartDate(Validation.getCurrentDateTime());
					ltJobeLogs.setEndDate(Validation.getCurrentDateTime());
					try {
						ltJobeLogs.setCsvDate(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (fileDetailsMap.get("Outlets") != null) {
						HashMap<Date, LtJobLogs> outletsMap = fileDetailsMap.get("Outlets");
						try {
							outletsMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Outlets", outletsMap);
					} else {
						HashMap<Date, LtJobLogs> outletsMap = new HashMap<Date, LtJobLogs>();
						try {
							outletsMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Outlets", outletsMap);
					}
				} else if (file.getName().contains("Products")) {
					ltJobeLogs.setJobType("Products");
					ltJobeLogs.setLogsStatus("CSV File Init");
					ltJobeLogs.setStartDate(Validation.getCurrentDateTime());
					ltJobeLogs.setEndDate(Validation.getCurrentDateTime());
					try {
						ltJobeLogs.setCsvDate(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (fileDetailsMap.get("Products") != null) {
						HashMap<Date, LtJobLogs> productsMap = fileDetailsMap.get("Products");
						try {
							productsMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Products", productsMap);
					} else {
						HashMap<Date, LtJobLogs> productsMap = new HashMap<Date, LtJobLogs>();
						try {
							productsMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Products", productsMap);
					}
				} else if (file.getName().contains("PriceList")) {
					ltJobeLogs.setJobType("PriceList");
					ltJobeLogs.setLogsStatus("CSV File Init");
					ltJobeLogs.setStartDate(Validation.getCurrentDateTime());
					ltJobeLogs.setEndDate(Validation.getCurrentDateTime());
					try {
						ltJobeLogs.setCsvDate(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (fileDetailsMap.get("PriceList") != null) {
						HashMap<Date, LtJobLogs> priceListMap = fileDetailsMap.get("PriceList");
						try {
							priceListMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("PriceList", priceListMap);
					} else {
						HashMap<Date, LtJobLogs> priceListMap = new HashMap<Date, LtJobLogs>();
						try {
							priceListMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("PriceList", priceListMap);
					}
				} else if (file.getName().contains("Inv")) {
					ltJobeLogs.setJobType("Inv");
					ltJobeLogs.setLogsStatus("CSV File Init");
					ltJobeLogs.setStartDate(Validation.getCurrentDateTime());
					ltJobeLogs.setEndDate(Validation.getCurrentDateTime());
					try {
						ltJobeLogs.setCsvDate(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (fileDetailsMap.get("Inv") != null) {
						HashMap<Date, LtJobLogs> invMap = fileDetailsMap.get("Inv");
						try {
							invMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Inv", invMap);
					} else {
						HashMap<Date, LtJobLogs> invMap = new HashMap<Date, LtJobLogs>();
						try {
							invMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Inv", invMap);
					}
				}else if (file.getName().contains("Out")) {
					ltJobeLogs.setJobType("Order Import");
					ltJobeLogs.setLogsStatus("CSV File Init");
					ltJobeLogs.setStartDate(Validation.getCurrentDateTime());
					ltJobeLogs.setEndDate(Validation.getCurrentDateTime());
					try {
						ltJobeLogs.setCsvDate(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (fileDetailsMap.get("Out") != null) {
						HashMap<Date, LtJobLogs> priceListMap = fileDetailsMap.get("Out");
						try {
							priceListMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Out", priceListMap);
					} else {
						HashMap<Date, LtJobLogs> odrerOutMap = new HashMap<Date, LtJobLogs>();
						try {
							odrerOutMap.put(new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateTime), ltJobeLogs);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						fileDetailsMap.put("Out", odrerOutMap);
					}
				}
			}

		} // End of for loop
		return fileDetailsMap;
	}

//	private static void printNames(String files[]) {
//		if (files != null && files.length > 0) {
//			for (String aFile : files) {
//				System.out.println(aFile);
//			}
//		}
//	}

	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}
}