package com.bjpowernode.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * Created with IntelliJ IDEA.
 * Description: excel文件工具类
 *
 * @author: WGQ
 * @date: 2023.07.16
 * @email: wgqcn@foxmail.com
 */
public class HSSFUtile {
    /**
     * 从指定的HSSFCell对象中获取值
     * @param cell
     * @return
     */
    public static String getCellValueForStr(HSSFCell cell){
        String ret = "";
        if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
            ret = cell.getStringCellValue() + "";
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            ret = cell.getNumericCellValue() + "";
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
            ret = cell.getBooleanCellValue() + "";
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
            // 此类型为excel中的公式, 如sum/avg等
            ret = cell.getCellFormula();
        }else{
            ret = "";
        }
        return "";
    }
}
