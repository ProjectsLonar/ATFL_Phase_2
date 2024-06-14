package com.atflMasterManagement.masterservice.reports;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.atflMasterManagement.masterservice.common.DateTimeClass;

public class ExcelReportOutlet {
	
	static Double totalRevenu;
	static DecimalFormat df = new DecimalFormat("0.00");
	 static SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy");
	 static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 2020-07-31

	public static void headerData(SXSSFWorkbook workbook, Sheet sheet, ExcelReportDto excelReportDto,
			List<ExcelDataOutlet> outletReportDataList, String logoPath)
			throws FileNotFoundException, IOException {
		System.out.println("In header data "+outletReportDataList.size());
		int rowIndex = 0;
		//imageAdd(workbook, sheet, logoPath, rowIndex);//Remove for NNF
		//rowIndex = 5;//Remove for NNF
		rowIndex = insertHeaderFilters(excelReportDto, outletReportDataList, sheet, rowIndex, workbook);
		++rowIndex;
		insertReportHeader(sheet, rowIndex, workbook);
		totalRevenu = 0D;
		int srNo = 0;
		
		CellStyle styleTableRow = workbook.createCellStyle();
		styleTableRow.setBorderBottom(BorderStyle.MEDIUM);
		styleTableRow.setBorderTop(BorderStyle.MEDIUM);
		styleTableRow.setBorderLeft(BorderStyle.MEDIUM);
		styleTableRow.setBorderRight(BorderStyle.MEDIUM);
		
		CellStyle styleTableRowRight = workbook.createCellStyle();
		styleTableRowRight = workbook.createCellStyle();
		styleTableRowRight.setBorderBottom(BorderStyle.MEDIUM);
		styleTableRowRight.setBorderTop(BorderStyle.MEDIUM);
		styleTableRowRight.setBorderLeft(BorderStyle.MEDIUM);
		styleTableRowRight.setBorderRight(BorderStyle.MEDIUM);
		styleTableRowRight.setAlignment(HorizontalAlignment.RIGHT);
		
		for (Iterator iterator = outletReportDataList.iterator(); iterator.hasNext();) {
			ExcelDataOutlet excelDataOutlet = (ExcelDataOutlet) iterator.next();
			++rowIndex;
			++srNo;
			insertReportData(sheet, rowIndex, excelDataOutlet, srNo,workbook,styleTableRow,styleTableRowRight);
			totalRevenu = totalRevenu + excelDataOutlet.getAmount();
		}
		++rowIndex;
		insertTotalRevenu(sheet, rowIndex, totalRevenu, workbook);
		++rowIndex;
		++rowIndex;
		inserDisclaimer(sheet, rowIndex, workbook);
	}
	
	private static int inserDisclaimer(Sheet sheet, int rowIndex, SXSSFWorkbook workbook) {
	//	Font fontTotal =  workbook.createFont();
//		fontTotal.setFontHeight((short) 12);
	//	fontTotal.setBold(false);
		
		CellStyle styleAlign = workbook.createCellStyle();
	//	styleAlign.setFont(fontTotal);
		styleAlign.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		styleAlign.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleAlign.setAlignment(HorizontalAlignment.LEFT);
		
		Row row = null;
		Cell cell = null;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("Disclaimer");
		cell.setCellStyle(styleAlign);
		cell = row.createCell(1);
		cell.setCellStyle(styleAlign);
		cell.setCellValue("The amount displayed here is approximate and might vary in invoice");
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 1, 6));
		return rowIndex;
	}

	private static int insertHeaderFilters(ExcelReportDto excelReportDto,
			List<ExcelDataOutlet> outletReportDataList, Sheet sheet, int rowIndex,SXSSFWorkbook workbook) {
		CellStyle styleTitle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeight((short) 16);
		font.setUnderline(Font.U_SINGLE);
		font.setBold(true);
		styleTitle.setFont(font);
		
		Row row = null;
		Cell cell = null;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(1);
		cell.setCellValue("Outletwise Order Summary Report");
		//cell.setCellStyle(styleTitle);
		sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,1,2));
		
		CellStyle styleTable1 = workbook.createCellStyle();
		//Font fontTable1 = workbook.createFont();
		//fontTable1.setFontHeight((short) 12);
		//fontTable1.setBold(true);
		//styleTable1.setFont(fontTable1);
		
		CellStyle styleTableNumber = workbook.createCellStyle();
		styleTableNumber.setAlignment(HorizontalAlignment.RIGHT);
		
		rowIndex++;
		rowIndex++;
		row = sheet.createRow(rowIndex);
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("1");
		cell.setCellStyle(styleTableNumber);
		cell = row.createCell(1);
		cell.setCellValue("Distributor Name or Code");
		cell.setCellStyle(styleTable1);
		cell = row.createCell(2);
		
		if(excelReportDto.getDistributorId() != null) {
			if (!outletReportDataList.isEmpty()) {
				cell.setCellValue(outletReportDataList.get(0).getDistributorName());
			}
		}else {
			cell.setCellValue("All");
		}

		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("2");
		cell.setCellStyle(styleTableNumber);
		cell = row.createCell(1);
		cell.setCellValue("Outlet");
		cell.setCellStyle(styleTable1);
		cell = row.createCell(2);
		if(excelReportDto.getOutletId() != null) {
			if (!outletReportDataList.isEmpty()) {
				cell.setCellValue(outletReportDataList.get(0).getOutletName());
			}
		}else {
			cell.setCellValue("All");
		}

		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("3");
		cell.setCellStyle(styleTableNumber);
		cell = row.createCell(1);
		cell.setCellValue("From Date");
		cell.setCellStyle(styleTable1);
		cell = row.createCell(2);
		//cell.setCellValue(excelReportDto.getFromDate());
		try {
			String formDateStr = DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate());
			Date formDate = sdf.parse(formDateStr);
			//Date formDate = sdf.parse(excelReportDto.getFromDate());
			cell.setCellValue(dt1.format(formDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("4");
		cell.setCellStyle(styleTableNumber);
		cell = row.createCell(1);
		cell.setCellValue("To Date");
		cell.setCellStyle(styleTable1);
		cell = row.createCell(2);
		//cell.setCellValue(excelReportDto.getToDate());
		try {
			String toDateStr = DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate());
			Date toDate = sdf.parse(toDateStr);
			//Date toDate = sdf.parse(excelReportDto.getToDate());
			cell.setCellValue(dt1.format(toDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("5");
		cell.setCellStyle(styleTableNumber);
		cell = row.createCell(1);
		cell.setCellValue("Status");
		cell.setCellStyle(styleTable1);
		cell = row.createCell(2);
		if(excelReportDto.getStatus() != null) {
			cell.setCellValue(excelReportDto.getStatus());
		}else {
			cell.setCellValue("All");
		}
		
		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("6");
		cell.setCellStyle(styleTableNumber);
		cell = row.createCell(1);
		cell.setCellValue("Report Generated Time");
		cell.setCellStyle(styleTable1);
		cell = row.createCell(2);
		String pattern = "dd-MMM-yyyy hh.mm aa"; //String pattern = "dd-MMM-yyyy HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		//String date = simpleDateFormat.format(new Date());
		String date = simpleDateFormat.format(DateTimeClass.getCurrentDateTime());
		cell.setCellValue(date);
		
		rowIndex++;
		row = sheet.createRow(rowIndex);
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell = row.createCell(2);

		return rowIndex;
	}

	private static int insertReportHeader(Sheet sheet, int rowIndex, SXSSFWorkbook workbook) {
		CellStyle styleTablehead = workbook.createCellStyle();
		Font fontTablehead = workbook.createFont();
		fontTablehead.setFontHeightInPoints((short) 12);
		fontTablehead.setColor(IndexedColors.WHITE.getIndex());
		fontTablehead.setBold(true);
		styleTablehead.setFont(fontTablehead);
		styleTablehead.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		styleTablehead.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleTablehead.setBorderBottom(BorderStyle.MEDIUM);
		styleTablehead.setBorderTop(BorderStyle.MEDIUM);
		styleTablehead.setBorderLeft(BorderStyle.MEDIUM);
		styleTablehead.setBorderRight(BorderStyle.MEDIUM);
        
		Row row = null;
		Cell cell = null;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("Sr No"); cell.setCellStyle(styleTablehead);
		cell = row.createCell(1);
		cell.setCellValue("Outlet Name"); cell.setCellStyle(styleTablehead);
		cell = row.createCell(2);
		cell.setCellValue("Outlet Code"); cell.setCellStyle(styleTablehead);
		cell = row.createCell(3);
		cell.setCellValue("Distributor Name");
		cell.setCellStyle(styleTablehead);
		cell = row.createCell(4);
		cell.setCellValue("Distributor Code");
		cell.setCellStyle(styleTablehead);
		cell = row.createCell(5);
		cell.setCellValue("Order No"); cell.setCellStyle(styleTablehead);
		cell = row.createCell(6);
		cell.setCellValue("Status"); cell.setCellStyle(styleTablehead);
		cell = row.createCell(7);
		cell.setCellValue("Order Date"); cell.setCellStyle(styleTablehead);
		cell = row.createCell(8);
		cell.setCellValue("Edible Oils"); cell.setCellStyle(styleTablehead);
		cell = row.createCell(9);
		cell.setCellValue("Ready to Cook"); cell.setCellStyle(styleTablehead);
		cell = row.createCell(10);
		cell.setCellValue("Ready to Eat"); cell.setCellStyle(styleTablehead);
		cell = row.createCell(11);
		cell.setCellValue("Spreads"); cell.setCellStyle(styleTablehead);
		cell = row.createCell(12);
		cell.setCellValue("Cereal Snacks"); cell.setCellStyle(styleTablehead);
		cell = row.createCell(13);
		cell.setCellValue("Chocolatey Confectionery"); cell.setCellStyle(styleTablehead);
		cell = row.createCell(14);
		cell.setCellValue("Order Amount"); cell.setCellStyle(styleTablehead);
		return rowIndex;
	}

	private static int insertReportData(Sheet sheet, int rowIndex, ExcelDataOutlet excelDataOutlet,
			int srNo,SXSSFWorkbook workbook,CellStyle styleTableRow,CellStyle styleTableRowRight) {
		
		
		String orderDate = null;
		try {
			SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MMM-yyyy");
		    Date date = sdfIn.parse(excelDataOutlet.getCreationDate());
		    orderDate =sdfOut.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Row row = null;
		Cell cell = null;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellStyle(styleTableRow);
		cell.setCellValue(srNo);
		
		cell = row.createCell(1);
		cell.setCellStyle(styleTableRow);
		if (excelDataOutlet.getOutletName() != null) {
			cell.setCellValue(excelDataOutlet.getOutletName());
		}
		cell = row.createCell(2);
		cell.setCellStyle(styleTableRow);
		if (excelDataOutlet.getOutletCode() != null) {
			cell.setCellValue(excelDataOutlet.getOutletCode());
		}
		
		cell = row.createCell(3);
		cell.setCellStyle(styleTableRow);
		if (excelDataOutlet.getDistributorName() != null) {
			cell.setCellValue(excelDataOutlet.getDistributorName());
		}
		
		cell = row.createCell(4);
		cell.setCellStyle(styleTableRow);
		if (excelDataOutlet.getDistributorCrmCode() != null) {
			cell.setCellValue(excelDataOutlet.getDistributorCrmCode());
		}
		
		cell = row.createCell(5);
		cell.setCellStyle(styleTableRow);
		if (excelDataOutlet.getOrderNumber() != null) {
			cell.setCellValue(excelDataOutlet.getOrderNumber());
		}
		cell = row.createCell(6);
		cell.setCellStyle(styleTableRow);
		if (excelDataOutlet.getStatus() != null) {
			cell.setCellValue(excelDataOutlet.getStatus());
		}
		cell = row.createCell(7);
		cell.setCellStyle(styleTableRow);
		if (excelDataOutlet.getCreationDate() != null) {
			cell.setCellValue(orderDate);
		}
		
		cell = row.createCell(8);
		cell.setCellStyle(styleTableRowRight);
		if (excelDataOutlet.getEdibleOils() != null) {
			cell.setCellValue(df.format(excelDataOutlet.getEdibleOils()));
		}
		
		cell = row.createCell(9);
		cell.setCellStyle(styleTableRowRight);
		if (excelDataOutlet.getReadyToCook() != null) {
			cell.setCellValue(df.format(excelDataOutlet.getReadyToCook()));
		}
		
		cell = row.createCell(10);
		cell.setCellStyle(styleTableRowRight);
		if (excelDataOutlet.getReadyToEat() != null) {
			cell.setCellValue(df.format(excelDataOutlet.getReadyToEat()));
		}
		
		cell = row.createCell(11);
		cell.setCellStyle(styleTableRowRight);
		if (excelDataOutlet.getSpreads() != null) {
			cell.setCellValue(df.format(excelDataOutlet.getSpreads()));
		}
		
		cell = row.createCell(12);
		cell.setCellStyle(styleTableRowRight);
		if (excelDataOutlet.getCerealSnacks() != null) {
			cell.setCellValue(df.format(excelDataOutlet.getCerealSnacks()));
		}
		
		cell = row.createCell(13);
		cell.setCellStyle(styleTableRowRight);
		if (excelDataOutlet.getChocolateyConfectionery() != null) {
			cell.setCellValue(df.format(excelDataOutlet.getChocolateyConfectionery()));
		}
				
		cell = row.createCell(14);
		cell.setCellStyle(styleTableRowRight);
		if (excelDataOutlet.getAmount() != null) {
			cell.setCellValue(df.format(excelDataOutlet.getAmount()));
		}
		return rowIndex;
	}

	private static int insertTotalRevenu(Sheet sheet, int rowIndex, Double totalRevenu, SXSSFWorkbook workbook) {
		CellStyle style = workbook.createCellStyle();
		///Font fontTotal = workbook.createFont();
		//fontTotal.setFontHeight((short) 12);
		//fontTotal.setBold(true);
		//style.setFont(fontTotal);
		style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		CellStyle styleAlign = workbook.createCellStyle();
		//styleAlign.setFont(fontTotal);
		styleAlign.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		styleAlign.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleAlign.setAlignment(HorizontalAlignment.RIGHT);
		
		styleAlign.setBorderBottom(BorderStyle.MEDIUM);
		styleAlign.setBorderTop(BorderStyle.MEDIUM);
		styleAlign.setBorderLeft(BorderStyle.MEDIUM);
		styleAlign.setBorderRight(BorderStyle.MEDIUM);
        
		Row row = null;
		Cell cell = null;
		row = sheet.createRow(rowIndex);
		
		for (int i = 0; i <= 11; ++i) {
		    cell = row.createCell(i);
		    cell.setCellStyle(styleAlign);
		    cell.setCellValue("Total ( ₹ )");
		}
		
		//cell = row.createCell(0); cell.setCellValue("Total ( ₹ )"); 
		//cell.setCellStyle(styleAlign);
		sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,0,13));
		cell = row.createCell(14);
		cell.setCellValue(df.format(totalRevenu)); 
		cell.setCellStyle(styleAlign);	
		return rowIndex;
	}

	public static int imageAdd(SXSSFWorkbook workbook, Sheet sheet, String logoPath, int rowIndex)
			throws FileNotFoundException, IOException {
		InputStream inputStream = new FileInputStream(logoPath);
		byte[] bytes = IOUtils.toByteArray(inputStream);
		int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		inputStream.close();
		CreationHelper helper = workbook.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(0);
		anchor.setRow1(0);
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		Dimension size = pict.getImageDimension();
		double scaledWidth = size.getWidth();
		double procentage = (1024.0d * 90d) / scaledWidth;
		double autosize = procentage / 100.0d;
		pict.resize(autosize);
		short h = (short) (pict.getImageDimension().getWidth());
		Row row = sheet.createRow(rowIndex);
		row.setHeight(h);
		sheet.addMergedRegion(new CellRangeAddress(0, 3, 0, 2));
		return rowIndex++;
	}
	
}