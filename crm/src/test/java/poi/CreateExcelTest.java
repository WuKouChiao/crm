package poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;

/**
 * Created with IntelliJ IDEA.
 * Description: poi测试类
 *
 * @author: WGQ
 * @date: 2023.06.12
 * @email: wgqcn@foxmail.com
 */
public class CreateExcelTest {
    public static void main(String[] args) throws Exception{
        // 创建HSSFWorkbook对象, 对应一个excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 使用wb创建HSSFSheet对象, 对应excel文件中的一页
        HSSFSheet sheet = wb.createSheet("学生列表");
        // 使用sheet创建HSSFRow对象, 对应excel文件中的一行, 起始为0
        HSSFRow row = sheet.createRow(0);
        // 使用row创建HSSFCell对象, 对应一行中的列, 起始为0
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("学号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("年龄");

        for (int i = 1; i < 11; i++) {
            row = sheet.createRow(i);
            // 使用row创建HSSFCell对象, 对应一行中的列, 起始为0
            cell = row.createCell(0);
            cell.setCellValue("学号"+i);
            cell = row.createCell(1);
            cell.setCellValue("姓名"+i);
            cell = row.createCell(2);
            cell.setCellValue("年龄"+i);
        }
        FileOutputStream os = new FileOutputStream("D:\\WorkSpace\\crm\\crm\\src\\test\\java\\poi\\student.xls");
        wb.write(os);
        wb.close();
        os.close();

        System.out.println("=======ok======");
    }
}
