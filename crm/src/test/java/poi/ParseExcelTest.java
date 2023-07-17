package poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;

/**
 * Created with IntelliJ IDEA.
 * Description: 使用apache-poi解析excel文件
 *
 * @author: WGQ
 * @date: 2023.07.16
 * @email: wgqcn@foxmail.com
 */
public class ParseExcelTest {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("D:\\WorkSpace\\crm\\crm\\src\\test\\java\\poi\\student.xls");
        HSSFWorkbook sheets = new HSSFWorkbook(fileInputStream);
        HSSFSheet sheetAt = sheets.getSheetAt(0);

        HSSFRow row = null;
        for (int i = 0; i <= sheetAt.getLastRowNum(); i++) {
            row = sheetAt.getRow(i);

            for (int j = 0; j < row.getLastCellNum(); j++) {
                HSSFCell cell = row.getCell(j);
                if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                    System.out.println(cell.getStringCellValue());
                }
            }
        }
        sheets.close();
        fileInputStream.close();
        System.out.println("====ok====");
    }
}
