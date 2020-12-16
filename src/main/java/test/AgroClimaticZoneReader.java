package test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class AgroClimaticZoneReader {
	public static void main(String[] args) {
		try {
			DB db = new DBConnectivity().getDatabase();
			DBCollection table = db.getCollection("agroClimaticZone");
			table.drop();
			
			FileInputStream file = new FileInputStream(new File("D:/ExcelSheetForUploading/Agroclimaticzone.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(1);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) 
            {
                Row row = rowIterator.next();
                BasicDBObject document = new BasicDBObject();
                document.put("reasonName",row.getCell(1).toString());
                document.put("state",row.getCell(2).toString());
                document.put("district",row.getCell(3).toString());
                table.insert(document);
            }
            System.out.println("Data is inserted");
            file.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
