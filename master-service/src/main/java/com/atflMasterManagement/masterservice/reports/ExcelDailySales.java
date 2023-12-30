package com.atflMasterManagement.masterservice.reports;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.atflMasterManagement.masterservice.dto.DailySalesResponseDto;

public class ExcelDailySales {
	static Double totalDbc;
	static Double totalTls;
	static Double totalValue;
	static DecimalFormat df = new DecimalFormat("0.00");
	
	 static SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	 static SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy");
	 static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 2020-07-31

	public static void headerData(XSSFWorkbook workbook, XSSFSheet sheet, List<DailySalesResponseDto> dailySalesResponseDtoList
			 )
			throws FileNotFoundException, IOException {
		int rowIndex = 0;
		//++rowIndex;
		insertReportHeader(sheet, rowIndex, workbook);
		totalDbc = 0D;
		totalTls = 0D;
		totalValue = 0D;
		int srNo = 0;
		for (Iterator iterator = dailySalesResponseDtoList.iterator(); iterator.hasNext();) {
			DailySalesResponseDto dailySalesResponseDto = (DailySalesResponseDto) iterator.next();
			++rowIndex;
			++srNo;
		    insertReportData(sheet, rowIndex, dailySalesResponseDto, srNo, workbook);
			totalDbc = Double.parseDouble(dailySalesResponseDto.getDbc()) + totalDbc ;
			totalTls = Double.parseDouble(dailySalesResponseDto.getTls()) + totalTls ;
			totalValue = Double.parseDouble(dailySalesResponseDto.getSale()) + totalValue ;
		}

		++rowIndex;
		insertTotal(sheet, rowIndex, totalDbc,totalTls,totalValue, workbook);
	}


	private static int insertReportHeader(XSSFSheet sheet, int rowIndex, XSSFWorkbook workbook) {
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
		cell.setCellValue("Date");
		cell.setCellStyle(styleTablehead);
		cell = row.createCell(1);
		cell.setCellValue("DBC");
		cell.setCellStyle(styleTablehead);
		cell = row.createCell(2);
		cell.setCellValue("TLS");
		cell.setCellStyle(styleTablehead);
		cell = row.createCell(3);
		cell.setCellValue("Total Value");
		cell.setCellStyle(styleTablehead);
			
		return rowIndex;
	}

	public static CellStyle setAlignRight(XSSFWorkbook workbook) {
		CellStyle styleTableRow = workbook.createCellStyle();
		styleTableRow = workbook.createCellStyle();
		styleTableRow.setBorderBottom(BorderStyle.MEDIUM);
		styleTableRow.setBorderTop(BorderStyle.MEDIUM);
		styleTableRow.setBorderLeft(BorderStyle.MEDIUM);
		styleTableRow.setBorderRight(BorderStyle.MEDIUM);
		styleTableRow.setAlignment(HorizontalAlignment.RIGHT);
		return styleTableRow;
	}

	private static int insertReportData(XSSFSheet sheet, int rowIndex, DailySalesResponseDto dailySalesResponseDto,
			int srNo, XSSFWorkbook workbook) {
		Row row = null;
		Cell cell = null;
		row = sheet.createRow(rowIndex);

		CellStyle styleTableRow = workbook.createCellStyle();
		styleTableRow.setBorderBottom(BorderStyle.MEDIUM);
		styleTableRow.setBorderTop(BorderStyle.MEDIUM);
		styleTableRow.setBorderLeft(BorderStyle.MEDIUM);
		styleTableRow.setBorderRight(BorderStyle.MEDIUM);
		
		cell = row.createCell(0);
		cell.setCellStyle(styleTableRow);
		if (dailySalesResponseDto.getDate() != null) {
			cell.setCellValue(dailySalesResponseDto.getDate());
		}

		cell = row.createCell(1);
		cell.setCellStyle(styleTableRow);
		if (dailySalesResponseDto.getDbc() != null) {
			cell.setCellValue(dailySalesResponseDto.getDbc());
		}
		cell = row.createCell(2);
		cell.setCellStyle(styleTableRow);
		if (dailySalesResponseDto.getTls() != null) {
			cell.setCellValue(dailySalesResponseDto.getTls());
		}
		cell = row.createCell(3);
		cell.setCellStyle(styleTableRow);
		if (dailySalesResponseDto.getSale() != null) {
			cell.setCellValue(dailySalesResponseDto.getSale());
		}
		
		return rowIndex;
	}


	private static int insertTotal(XSSFSheet sheet, int rowIndex, Double totalDbc, Double totalTls, Double totalValue, Workbook workbook) {

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
		cell.setCellValue("MTD");
		cell.setCellStyle(styleTablehead);
		cell = row.createCell(1);
		cell.setCellValue(totalDbc);
		cell.setCellStyle(styleTablehead);
		cell = row.createCell(2);
		cell.setCellValue(totalTls);
		cell.setCellStyle(styleTablehead);
		cell = row.createCell(3);
		cell.setCellValue(totalValue);
		cell.setCellStyle(styleTablehead);
		return rowIndex;
	}

}