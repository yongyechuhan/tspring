package com.liuxin.tspring.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

/**
 * @author: yangzhi
 * @date : 2019-03-21 Excel操作工具类
 */
@Slf4j
public class ExcelOprUtils {

	/**
	 * 读取xls模板
	 * 
	 * @param fileName
	 * @return
	 */
	public static Workbook readReportTemplate(String fileName) {
		Workbook workbook = null;
		try {
//			ClassPathResource cpr = new ClassPathResource("template/" + fileName);
			FileSystemResource cpr =
					new FileSystemResource(new File("E://report/exportExcelTemplate.xlsx"));
			workbook = new XSSFWorkbook(new FileInputStream(cpr.getFile()));
		} catch (Exception e) {
			log.error("==>读取商户交易汇总报表模板失败，", e);
		}
		return workbook;
	}

	/**
	 * 关闭Workbook
	 * 
	 * @param workbook
	 * @return
	 */
	public static void close(Workbook workbook) {
		if (null != workbook) {
			try {
				workbook.close();
			} catch (Exception e) {
				log.error("==>关闭Workbook时发生异常");
			}
		}
	}

	/**
	 * 生成Excel文件
	 * 
	 * @param workbook
	 * @param file
	 *            全路径文件名
	 */
	public static boolean genExcelReport(Workbook workbook, String file) {
		OutputStream os = null;
		try {
			File path = new File(file).getParentFile();
			if (!path.exists()) {
				path.mkdirs();
			}
			os = new FileOutputStream(file);
			workbook.write(os);
			return true;
		} catch (IOException e) {
			log.error("==>生成【{}】Excel报表发生异常，", e);
			return false;
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					log.error("==>Excel写入操作完成，但关闭输出流时发生异常，", e);
				}
			}
			close(workbook);
		}
	}

	/**
	 * 向指定行中写入数据
	 * 
	 * @param sheet
	 * @param rownum
	 * @param data
	 * @param template
	 */
	public static void writeRow(Sheet sheet, int rownum, TreeMap<Integer, Object> data, Row template) {
		if (sheet.getRow(rownum) == null) {
			sheet.createRow(rownum);
		}
		ExcelOprUtils.writeRow(data, sheet.getRow(rownum), template);
	}

	/**
	 * 向指定一行中写入数据
	 * 
	 * @param data
	 *            待写入的数据及索引
	 * @param now
	 *            当前行对象
	 * @param template
	 *            模板行对象
	 */
	public static void writeRow(TreeMap<Integer, Object> data, Row now, Row template) {
		now.setRowStyle(template.getRowStyle());
		now.setHeight((short) 500);
        //1-n
        short lastCellNum = template.getLastCellNum();
        for (int i = 0; i < lastCellNum; i++) {
            Object value = data.get(i);
            if (value == null) {
                // 空白单元格补 0
                value = 0.00;
            }
            writeCell(value, i, now, template, (now != template));
        }

        // 清楚Map中的数据
		data.clear();
	}

	/**
	 * 向指定单元格中写入数据
	 * 
	 * @param val
	 *            需要写入的值
	 * @param cellIndex
	 *            单元格索引
	 * @param now
	 *            当前行
	 * @param template
	 *            模板行
	 */
	public static void writeCell(Object val, int cellIndex, Row now, Row template, boolean needCrearted) {
		Cell cell = null;
		if (needCrearted) {
			cell = now.createCell(cellIndex);
			cell.setCellStyle(template.getCell(cellIndex).getCellStyle());
		} else {
			cell = template.getCell(cellIndex);
		}

		if (val instanceof String) {
			cell.setCellValue((String) val);
		} else if (val instanceof Double) {
			cell.setCellValue((Double) val);
		} else if (val instanceof Integer) {
			cell.setCellValue((Integer) val);
		} else if (val instanceof Long) {
			cell.setCellValue((Long) val);
		} else if (val instanceof Calendar) {
			cell.setCellValue((Calendar) val);
		} else if (val instanceof Date) {
			cell.setCellValue((Date) val);
		} else if (val instanceof RichTextString) {
			cell.setCellValue((RichTextString) val);
		}
	}
}
