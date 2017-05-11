package com.daisy.utils;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * excel 大文件导出
 * @author PC
 *
 */
public class ExcelExport {
	private SXSSFWorkbook wkbook;
	private Sheet sheet;
	private int indexRowIndex=0;
	private static final int SHEET_MAX_ROW_NUMBER=1000000;//每个sheet最大多少行
	
	public ExcelExport() {
		wkbook = new SXSSFWorkbook(300);
		sheet = wkbook.createSheet();
	}
	
	/**
	 * 添加一行数据
	 * @param cells
	 */
	public synchronized void addRow(String...cells)
	{
		try {
			Row row = sheet.createRow(indexRowIndex);
			for(int i = 0;i < cells.length;i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(cells[i]);
			}
			indexRowIndex++;
			//分页,也就是分sheet
			if(indexRowIndex%SHEET_MAX_ROW_NUMBER==0)
			{
				indexRowIndex=0;
				sheet=wkbook.createSheet();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出到某个流中.并结束整个过程
	 * @param output
	 */
	public synchronized void OutPut(OutputStream output)
	{
		try {
			wkbook.write(output);
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			wkbook.dispose();//删除临时文件并摧毁该对象
			wkbook=null;
		}
	}
}
