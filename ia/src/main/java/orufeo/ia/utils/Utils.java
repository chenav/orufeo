package orufeo.ia.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Utils {

	private static String EXPORT_FILE_PATH="/Users/Chenav/Desktop/eemi_export.xlsx";
	
	public static void generateExcelFile(ArrayList<String[]> dataList) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Data History");

		int rowNum = 0;
		System.out.println("Creating excel");
		System.out.println("Nb de lignes:"+dataList.size());
		initHeaders(dataList);
		

		for (String[] line : dataList) {
			Row row = sheet.createRow(rowNum++);
			int colNum = 0;
			for (int i=0; i<line.length ;i++) {
				Cell cell = row.createCell(colNum++);

				//style for header
				if (1==rowNum) {
					CellStyle style = workbook.createCellStyle();
					style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
					style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					style.setAlignment(HorizontalAlignment.CENTER);
					Font font = workbook.createFont();
					font.setColor(IndexedColors.WHITE.getIndex());
					font.setBold(true);
					style.setFont(font);
					cell.setCellStyle(style);
				}
				
				cell.setCellValue((String) line[i]);
			}
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(EXPORT_FILE_PATH);
			workbook.write(fos);
			workbook.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Excel Done");

	}
	
	private static void initHeaders(ArrayList<String[]> dataList) {
		
		String[] headers = new String[6];
		headers[0] = "DATE";
		headers[1] = "MESSAGE";
		headers[2] = "SENTIMENT";
		headers[3] = "MAGNITUDE";
		headers[4] = "USERNAME";
		headers[5] = "USERID";
		
		dataList.add(0, headers);
	}
}
