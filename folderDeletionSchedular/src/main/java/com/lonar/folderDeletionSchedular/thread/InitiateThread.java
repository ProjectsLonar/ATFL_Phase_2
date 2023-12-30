package com.lonar.folderDeletionSchedular.thread;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lonar.folderDeletionSchedular.model.LtJobeImportExport;
import com.lonar.folderDeletionSchedular.model.LtJobeSchedule;
import com.lonar.folderDeletionSchedular.model.Status;
import com.lonar.folderDeletionSchedular.model.Validation;
import com.lonar.folderDeletionSchedular.services.LtFolderDeletionService;
import com.lonar.folderDeletionSchedular.services.LtJobeService;

@Component
public class InitiateThread {

	@Autowired
	LtJobeService ltJobeService;
	
	@Autowired
	LtFolderDeletionService ltFolderDeletionService;

	public static Timer timerImport = new Timer(true);

	private static final Logger logger = LoggerFactory.getLogger(InitiateThread.class);
	public static Map<String, HashMap<Long, LtJobeImportExport>> jobImportExportMap = new HashMap<String, HashMap<Long, LtJobeImportExport>>();
	public static Map<String, LtJobeSchedule> scheduleJobMap = new HashMap<String, LtJobeSchedule>();

	@PostConstruct
	public void startGetDataAndImportAndExport() throws IOException {
		System.out.println("in strtget");
		try {
		//	setAllConfigData();
			setJobImportExport();
			setScheduleJob();
			setImportExportTimer();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setImportExportTimer() {
		try {
			for (Map.Entry<String, LtJobeSchedule> scheduleJobEntry : scheduleJobMap.entrySet()) {

				LtJobeSchedule ltJobeSchedule = scheduleJobEntry.getValue();

				String scheduleType = ltJobeSchedule.getScheduleType();

				ImportTimerTask importTimerTask = new ImportTimerTask(ltJobeService, ltFolderDeletionService);

				if (scheduleJobEntry.getKey().equals("FOLDER_DELETION_IMPORT")) {
					System.out.println("if valie type");
					
					switch (scheduleType) {

					case "DAILY":

						System.out.println("Valid schedule type" + scheduleType);

						Long dayInMS = ltJobeSchedule.getDays() * 24 * 60 * 60 * 1000;

						Long delay = dailyToMS(ltJobeSchedule.getTime());

						System.out.println("Import delay time today" + delay);

						if (delay < 0) {
							System.out.println("Import delay time for today is gone" + delay);
							// delay = dayInMS + delay;
							System.out.println("Import delay time schdule for next day" + delay);
						}

						timerImport.scheduleAtFixedRate(importTimerTask, delay, dayInMS);

						break;
						
					case "HOURLY":
						System.out.println("Valid schedule type" + scheduleType);

						dayInMS = getMS(ltJobeSchedule.getHours());

						delay = dailyToMS(ltJobeSchedule.getTime());
						if (delay == 0) {
							System.out.println("Import delay time for today is gone" + delay);
							delay = dayInMS;
							System.out.println("Import delay time schdule for next HOUR" + delay);
						}

						timerImport.scheduleAtFixedRate(importTimerTask, delay, dayInMS);

						break;
					default:
						System.out.println("Invalid schedule type" + scheduleType);
						break;
					}

				}
			}
		} catch (Exception e) {
			logger.error("Error Description Initiate Thread", e);
			e.printStackTrace();
		}

	}

	public Long getMS(String hours) {

		int hour = Integer.parseInt(hours.substring(0, 2));
		int minute = Integer.parseInt(hours.substring(3));
		long millisMinut = minute * 60 * 1000;
		long millisHours = hour * 60 * 60 * 1000;
		long mainmillis = millisHours + millisMinut;
		return mainmillis;
	}

	public Long dailyToMS(String dailyTime) {
		try {
			String format = "MM/dd/yyyy hh:mm a";
			String dateFormat = "MM/dd/yyyy";

			SimpleDateFormat sdf = new SimpleDateFormat(format);
			SimpleDateFormat sdfDate = new SimpleDateFormat(dateFormat);

			Date dateObj1 = Validation.getCurrentDateTime();
			
			//Date dateObj1 = new Date();
			Date dateObj2 = sdf.parse(sdfDate.format(dateObj1) + " " + dailyTime);

			Long diff = dateObj2.getTime() - dateObj1.getTime();
//			if (diff <= 0) {
//				diff = (long) 0;
//			}
			return diff;
		} catch (Exception e) {
			logger.error("Error Description Initiate Thread", e);
			e.printStackTrace();
		}
		return (long) 0;
	}

	private void setScheduleJob() {
		try {
			// get All Job Schedule Data
			Status scheduleJobStatus = ltJobeService.getAllJobeSchedule();
			List<LtJobeSchedule> scheduleJobList = (List<LtJobeSchedule>) scheduleJobStatus.getData();
			for (Iterator<LtJobeSchedule> iterator = scheduleJobList.iterator(); iterator.hasNext();) {
				LtJobeSchedule ltJobeSchedule = (LtJobeSchedule) iterator.next();
				scheduleJobMap.put(ltJobeSchedule.getJobType(), ltJobeSchedule);
			}
		} catch (Exception e) {
			logger.error("Error Description Initiate Thread", e);
			e.printStackTrace();
		}

	}

	private void setJobImportExport() {
		try {
			// get All Jobe Import Export Data
			Status jobImportExportStatus = ltJobeService.getAllJobeImportExport();
			List<LtJobeImportExport> jobImportExportList = (List<LtJobeImportExport>) jobImportExportStatus.getData();

			for (Iterator<LtJobeImportExport> iterator = jobImportExportList.iterator(); iterator.hasNext();) {
				LtJobeImportExport ltJobeImportExport = (LtJobeImportExport) iterator.next();
				if (jobImportExportMap.get(ltJobeImportExport.getType()) != null) {
					HashMap<Long, LtJobeImportExport> jobImportExportSeqMap = jobImportExportMap
							.get(ltJobeImportExport.getType());
					jobImportExportSeqMap.put(ltJobeImportExport.getSequence(), ltJobeImportExport);
					jobImportExportMap.put(ltJobeImportExport.getType(), jobImportExportSeqMap);
				} else {
					HashMap<Long, LtJobeImportExport> jobImportExportSeqMap = new HashMap<Long, LtJobeImportExport>();
					jobImportExportSeqMap.put(ltJobeImportExport.getSequence(), ltJobeImportExport);
					jobImportExportMap.put(ltJobeImportExport.getType(), jobImportExportSeqMap);
				}

			}
		} catch (Exception e) {
			logger.error("Error Description Initiate Thread", e);
			e.printStackTrace();
		}
	}

}
