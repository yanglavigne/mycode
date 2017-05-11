package com.daisy.exceltest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.daisy.utils.ExcelExport;


public class TestExcelExport{
    
	/**
	 * 测试导出大量数据到excel
	 * @param args
	 * @throws IOException
	 */
	public static void main( String[] args ) throws IOException{
    	long start = new Date().getTime();
    	ExcelExport ee=new ExcelExport();
    	String uuid = null;
    	for(int i = 0;i < 2000000;i++) {
    		uuid=UUID.randomUUID().toString();
    		ee.addRow(uuid,uuid,uuid,uuid,uuid,uuid,uuid,uuid,uuid,uuid);
		}
    	System.out.println(new Date().getTime()-start);
    	FileOutputStream out = new FileOutputStream("C:/Users/PC/Downloads/sxssf.xlsx");
    	ee.OutPut(out);
        System.out.println(new Date().getTime()-start);
    }
	
}
