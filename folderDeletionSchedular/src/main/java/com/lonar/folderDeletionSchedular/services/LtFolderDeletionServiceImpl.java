package com.lonar.folderDeletionSchedular.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.lonar.folderDeletionSchedular.FolderDeletionSchedularApplication;
import com.lonar.folderDeletionSchedular.util.UtilsMaster;

import javassist.bytecode.stackmap.TypeData.ClassName;

@Service
public class LtFolderDeletionServiceImpl implements LtFolderDeletionService {
	//Map<String, String> map = new HashMap<String, String>();

	@Override
	public boolean deleteFolderContains() {
		Map<String, String> map = getProperitesMap();
		System.out.println(map.toString());
		boolean successFlag = false;
		int count = 0;
		if (!map.isEmpty()) {
			for (Entry<String, String> entry : map.entrySet()) {
				boolean deleteFlag = getFileMap(entry.getValue());
				if (deleteFlag) {
					count++;
				}
			}
		}
		if (count > 0) {
			successFlag = true;
		}
		return successFlag;
	}

	public Map<String, String> getProperitesMap() {
		Map<String, String> map = new HashMap<String, String>();
		Properties properties = new Properties();

		try {
			String path = System.getProperty("user.dir") + "/config.properties";
			File file = new File(path);
			if (file.exists()) {
				FileInputStream fileInputStream = new FileInputStream(file);
				properties.load(fileInputStream);
				fileInputStream.close();
				// properties.load(getClass().getClassLoader().getResourceAsStream("queries/config.properties"));
				for (Entry<Object, Object> entry : properties.entrySet()) {
					map.put((String) entry.getKey(), (String) entry.getValue());

				}
			} else {
				System.out.println("file not exist");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static boolean deleteDirectory(File[] fileList, int maxDay) {
		boolean flag = false;
		Date dt = new Date(fileList[0].lastModified());
		Date maxDate = DateUtils.addDays(dt, -maxDay);

		for (File subfile : fileList) {
			Date fileModifiedDate = new Date(subfile.lastModified());
			if (maxDate.after(fileModifiedDate)) {
				// file need to delete
				flag = subfile.delete();
			}
			/*
			 * if (subfile.isDirectory()) { // deleteDirectory(subfile); }
			 */

			// flag = subfile.delete();
		}
		return flag;
	}

	public boolean getFileMap(String filePath) {
		String maxday = filePath.substring(filePath.indexOf("$") + 1, filePath.lastIndexOf("$"));
		String replacedStr = filePath.substring(0, filePath.indexOf("$"));
		boolean success = sortFileByModificationDate(replacedStr, Integer.parseInt(maxday));
		return success;
	}

	public boolean sortFileByModificationDate(String filePath, int maxDay) {
		boolean successFlag = false;
		int count = 0;
		File file1 = new File(filePath);
		if (file1.exists()) {
			File[] fileList = file1.listFiles();
			if (fileList.length > 0) {
				Arrays.sort(fileList, new Comparator<File>() {
					public int compare(File f1, File f2) {
						return Long.compare(f2.lastModified(), f1.lastModified());
					}
				});

				boolean deleteFlag = deleteDirectory(fileList, maxDay);
				if (deleteFlag) {
					count++;
				}
			}
			if (count > 0) {
				successFlag = true;
			} else {
				successFlag = false;
			}

		}
		return successFlag;
	}
}
