import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class Parser {

    public static String parse(String name) {

        String result = "";
        InputStream in = null;
        HSSFWorkbook wb = null;
        try {
            in = new FileInputStream(name);
            wb = new HSSFWorkbook(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                int cellType = cell.getCellType();
                switch (cellType) {
                    case Cell.CELL_TYPE_STRING:
                        String s = cell.getStringCellValue();
                        if(isNumeric(s))
                            result += s + "\n";
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        Double sn = cell.getNumericCellValue();
                        result += sn.longValue() + "\n";
                        break;

                    case Cell.CELL_TYPE_FORMULA:
                        result += cell.getNumericCellValue() + "\n";
                        break;
                }
            }
        }
        return result;
    }
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

}
