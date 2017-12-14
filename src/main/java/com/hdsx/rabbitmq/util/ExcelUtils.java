package com.hdsx.rabbitmq.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

    private static String file = "D:\\test.xlsx";
    private static String tableCode = "SGT_SFL";
    private static String tableName = "示范路";

    private static List<String> fieldCode = new ArrayList<>();
    private static List<String> fieldType = new ArrayList<>();
    private static List<String> fieldRemark = new ArrayList<>();

    private static int fieldCode_num = 0;
    private static int fieldType_num = 2;
    private static int fieldRemark_num = 1;

    public static void main(String[] args) {
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String, String>> list = null;
        String cellData = null;
        wb = readExcel(file);
        if (wb != null) {
            //用来存放表中数据
            list = new ArrayList<Map<String, String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i < rownum; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    fieldCode.add((String) getCellFormatValue(row.getCell(fieldCode_num)));
                    fieldType.add((String) getCellFormatValue(row.getCell(fieldType_num)));
                    fieldRemark.add((String) getCellFormatValue(row.getCell(fieldRemark_num)));
                } else {
                    break;
                }
            }

            StringBuffer create = new StringBuffer();
            StringBuffer reamrk = new StringBuffer();
            create.append("create table " +tableCode+"(\r\n");
            reamrk.append("comment on table "+tableCode +" is '"+tableName+"';\r\n");
            for(int i = 0;i<fieldCode.size();i++){
                if("".equals(fieldCode.get(i).trim()) || fieldCode.get(i)==null) return;
                create.append(fieldCode.get(i).toLowerCase()+"   "+ fieldType.get(i)+",\r\n");
                if (!"".equals(fieldRemark.get(i).trim()))
                    reamrk.append("comment on column " + tableCode + "." + fieldCode.get(i).toLowerCase() + " is'" + fieldRemark.get(i).trim() + "';\r\n");
            }

            int i = create.lastIndexOf(",");
            String substring = create.substring(0, i);
            substring = substring + (")");
            System.out.println(substring);
            System.out.println(reamrk.toString());
        }

    }

    //读取excel
    public static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        File file = null;
        FileInputStream is = null;
        try {
            file = new File(filePath); // 创建文件对象
            is = new FileInputStream(file); // 文件流
            if (".xls".equals(extString)) {
                return new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return new XSSFWorkbook(is);
            } else {
                return null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wb;
    }

    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }

}
