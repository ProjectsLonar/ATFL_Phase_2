package com.lonar.atflMobileInterfaceService.thread;

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

import com.lonar.atflMobileInterfaceService.common.Validation;
import com.lonar.atflMobileInterfaceService.model.LtConfigurartion;
import com.lonar.atflMobileInterfaceService.model.LtJobeImportExport;
import com.lonar.atflMobileInterfaceService.model.LtJobeSchedule;
import com.lonar.atflMobileInterfaceService.model.Status;
import com.lonar.atflMobileInterfaceService.service.LtJobeService;
import com.lonar.atflMobileInterfaceService.service.LtMastOrderService;
import com.lonar.atflMobileInterfaceService.service.LtReadCSVFilesServices;

@Component
public class InitiateThread {

	@Autowired
	LtJobeService ltJobeService;

	@Autowired
	LtReadCSVFilesServices ltReadCSVFilesServices;

	@Autowired
	LtMastOrderService ltMastOrderService;

	public static Timer timerImport = new Timer(true);
	public static Timer timerImportHrs = new Timer(true);
	public static Timer timerExport = new Timer(true);
	
	private static final Logger logger = LoggerFactory.getLogger(InitiateThread.class);

	public static Map<String, LtConfigurartion> configMap = new HashMap<String, LtConfigurartion>();
	public static Map<String, HashMap<Long, LtJobeImportExport>> jobImportExportMap = new HashMap<String, HashMap<Long, LtJobeImportExport>>();
	public static Map<String, LtJobeSchedule> scheduleJobMap = new HashMap<String, LtJobeSchedule>();

	@PostConstruct
	public void startGetDataAndImportAndExport() throws IOException {
		try {
			setAllConfigData();
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
				ImportTimerTask importTimerTask = new ImportTimerTask(ltJobeService, ltReadCSVFilesServices);
				ExportTimerTask exportTimerTask = new ExportTimerTask(ltMastOrderService, ltJobeService);
				ImportHourseTask importHourseTask = new ImportHourseTask(ltJobeService, ltReadCSVFilesServices);
				
				if (scheduleJobEntry.getKey().equals("IMPORT")) {
					
					switch (scheduleType) {
					
					case "DAILY":

						Long dayInMS = ltJobeSchedule.getDays() * 24 * 60 * 60 * 1000;

						Long delay = dailyToMS(ltJobeSchedule.getTime());

						if (delay < 0) {
							System.out.println("Import delay time for today is gone" + delay);
						//	delay = dayInMS + delay;
							System.out.println("Import delay time schdule for next day" + delay);
						}

						timerImport.scheduleAtFixedRate(importTimerTask, delay, dayInMS);

						break;
						
					case "HOURLY":
						
						dayInMS = getMS(ltJobeSchedule.getHours());
						
						delay = dailyToMS(ltJobeSchedule.getTime());
						
						if (delay < 0) {
							// delay = dayInMS;
						}

						timerImport.scheduleAtFixedRate(importHourseTask, delay, dayInMS);

						break;
						
					default:
						System.out.println("Invalid schedule type" + scheduleType);
						break;
					}

				} else if (scheduleJobEntry.getKey().equals("EXPORT")) {
					
					switch (scheduleType) {
					case "DAILY":

						Long dayInMS = ltJobeSchedule.getDays() * 24 * 60 * 60 * 1000;

						Long delay = dailyToMS(ltJobeSchedule.getTime());
						
						if (delay < 0) {
							//delay = dayInMS + delay;
						}

						timerExport.scheduleAtFixedRate(exportTimerTask, delay, dayInMS);

						break;
					case "HOURLY":
						
						System.out.println("Valid schedule type" + scheduleType);
						
						dayInMS = getMS(ltJobeSchedule.getHours());

						delay = dailyToMS(ltJobeSchedule.getTime());
						
						if (delay < 0) {
							//delay = dayInMS;
						}
						timerExport.scheduleAtFixedRate(exportTimerTask, delay, dayInMS);
						break;
					default:
						System.out.println("Invalid schedule type" + scheduleType);
						break;
					}

				}else if (scheduleJobEntry.getKey().equals("IMPORT-HRS")) {
					
					switch (scheduleType) {
					
					case "DAILY":

						Long dayInMS = ltJobeSchedule.getDays() * 24 * 60 * 60 * 1000;

						Long delay = dailyToMS(ltJobeSchedule.getTime());

						if (delay < 0) {
							System.out.println("Import delay time for today is gone" + delay);
						//	delay = dayInMS + delay;
							System.out.println("Import delay time schdule for next day" + delay);
						}

						timerImportHrs.scheduleAtFixedRate(importTimerTask, delay, dayInMS);

						break;
						
					case "HOURLY":
						
						dayInMS = getMS(ltJobeSchedule.getHours());
						
						delay = dailyToMS(ltJobeSchedule.getTime());
						
						if (delay == 0) {
							// delay = dayInMS;
						}

						timerImportHrs.scheduleAtFixedRate(importHourseTask, delay, dayInMS);

						break;
						
					default:
						System.out.println("Invalid schedule type" + scheduleType);
						break;
					}

				}
			}
		} catch (Exception e) {
			logger.error("Error Description Initiate Thread",e);
			e.printStackTrace();
		}

	}

	public Long getMS(String hours) {

		int hour = Integer.parseInt(hours.substring(0, 2));
		int minute = Integer.parseInt(hours.substring(3));

		long millisMinut = minute * 60 * 1000; // min convert to millis
		long millisHours = hour * 60 * 60 * 1000; // hour convert to millis
		long mainmillis = millisHours + millisMinut;
		return mainmillis;
	}

	public Long dailyToMS(String dailyTime) {
		try {
			// dailyTime="11:30 AM";

			String format = "MM/dd/yyyy hh:mm a";
			String dateFormat = "MM/dd/yyyy";

			SimpleDateFormat sdf = new SimpleDateFormat(format);
			SimpleDateFormat sdfDate = new SimpleDateFormat(dateFormat);

			Date dateObj1 = Validation.getCurrentDateTime(); // new Date();
			Date dateObj2 = sdf.parse(sdfDate.format(dateObj1) + " " + dailyTime);
			System.out.println(dateObj1);
			System.out.println(dateObj2 + "\n");

			Long diff = dateObj2.getTime() - dateObj1.getTime();
			if (diff <= 0) {
				diff = (long) 0;
			}
			return diff;
		} catch (Exception e) {
			logger.error("Error Description Initiate Thread",e);
			e.printStackTrace();
		}
		return (long) 0;
	}

	private void setScheduleJob() {
		try {
			// get All Job Schedule Data
			Status scheduleJobStatus = ltJobeService.getAllJobeSchedule();
			List<LtJobeSchedule> scheduleJobList = (List<LtJobeSchedule>) scheduleJobStatus.getData();
			for (Iterator iterator = scheduleJobList.iterator(); iterator.hasNext();) {
				LtJobeSchedule ltJobeSchedule = (LtJobeSchedule) iterator.next();
				scheduleJobMap.put(ltJobeSchedule.getJobType(), ltJobeSchedule);
			}
		} catch (Exception e) {
			logger.error("Error Description Initiate Thread",e);
			e.printStackTrace();
		}

	}

	private void setJobImportExport() {
		try {
			// get All Jobe Import Export Data
			Status jobImportExportStatus = ltJobeService.getAllJobeImportExport();
			List<LtJobeImportExport> jobImportExportList = (List<LtJobeImportExport>) jobImportExportStatus.getData();

			for (Iterator iterator = jobImportExportList.iterator(); iterator.hasNext();) {
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
			logger.error("Error Description Initiate Thread",e);
			e.printStackTrace();
		}
	}

	private void setAllConfigData() {
		try {
			// get config data
			Status configStatus = ltJobeService.getAllConfiguration();
			List<LtConfigurartion> configList = (List<LtConfigurartion>) configStatus.getData();
			for (Iterator iterator = configList.iterator(); iterator.hasNext();) {
				LtConfigurartion ltConfigurartion = (LtConfigurartion) iterator.next();
				configMap.put(ltConfigurartion.getKey(), ltConfigurartion);
			}
		} catch (Exception e) {
			logger.error("Error Description Initiate Thread",e);
			e.printStackTrace();
		}
	}

}
