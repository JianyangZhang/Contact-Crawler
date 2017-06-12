package crawler.service;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadHunterAccounts {

	public static void main(String[] args) {
		try {
			FileInputStream file = new FileInputStream(new File("/Users/Wentao/Documents/HunterAccounts.xlsx"));
			
			//Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook wb = new XSSFWorkbook(file);
			
			//Get first/desired sheet from the workbook
	        XSSFSheet sheet = wb.getSheetAt(0);
	        
	        //Iterate through each rows one by one
	        Iterator<Row> rowIterator = sheet.iterator();
	        
	        while (rowIterator.hasNext()){
	        	Row row = rowIterator.next();
	        	 //For each row, iterate through all the columns
	            Iterator<Cell> cellIterator = row.cellIterator();
	            
	            while (cellIterator.hasNext()){
	            	Cell cell = cellIterator.next();
	            	switch (cell.getCellType()) 
	                {
	                    case Cell.CELL_TYPE_NUMERIC:
	                        System.out.print(cell.getNumericCellValue() + "\t");
	                        break;
	                    case Cell.CELL_TYPE_STRING:
	                        System.out.print(cell.getStringCellValue() + "\t");
	                        break;
	                };
	                
	            	 
	            }
	            System.out.println("");
	        	
	        	
	        }
	        
		    
	        file.close();
		    
		} catch(Exception ioe) {
		    ioe.printStackTrace();
		}

	}

}
