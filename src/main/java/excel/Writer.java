package excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Writer {
	
	private final String filePath;
	private final XSSFWorkbook workbook;
	private final XSSFSheet sheet;
	private int rownum;
	
	public Writer(String filePath) {
		this.filePath = filePath;
		this.workbook = new XSSFWorkbook();
		this.sheet = workbook.createSheet();
		this.rownum = 0;
	}
	
	public void addRow(String key, String rels) {
		XSSFRow row = sheet.createRow(rownum++);
		XSSFCell keyCell = row.createCell(0);
		XSSFCell relCell = row.createCell(1);
		keyCell.setCellValue(key);
		relCell.setCellValue(rels);
	}
	
	public void out() {
		
		try (FileOutputStream out = new FileOutputStream(new File(filePath))) {
			workbook.write(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
