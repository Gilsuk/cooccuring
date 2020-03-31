package excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Parser implements Iterable<String> {

	private final String filePath;

	public Parser(String filePath) {
		this.filePath = encode(filePath);
	}

	@Override
	public Iterator<String> iterator() {
		return new ResultSupplier(parse());
	}

	private String encode(String filePath) {
		try {
			byte[] encodedFilePath = filePath.getBytes("ISO-8859-1");
			filePath = new String(encodedFilePath);
		} catch (UnsupportedEncodingException e) { e.printStackTrace(); }

		return filePath;
	}
	
	private List<String> parse() {

		List<String> result = new ArrayList<String>();

		try (FileInputStream fis = new FileInputStream(filePath);
				XSSFWorkbook wb = new XSSFWorkbook(fis)) {

			XSSFSheet sheet = wb.getSheetAt(0);

			for (Row row: sheet)
				for (Cell cell: row)
					if (cell.getColumnIndex() == 2)
						result.add(cell.getStringCellValue());

		} catch (IOException e) { e.printStackTrace(); }

		return result;
	}

}

class ResultSupplier implements Iterator<String> {
	
	private final List<String> list;
	private final int length;
	private int index = 1; // to skip first line :: HACK
	
	ResultSupplier(List<String> result) {
		this.list = result;
		this.length = list.size();
	}

	@Override
	public boolean hasNext() {
		return index == length ? false : true;
	}

	@Override
	public String next() {
		return list.get(index++);
	}
	
}