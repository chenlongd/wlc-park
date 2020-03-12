package com.perenc.xh.commonUtils.utils.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/20 10:05
 **/
public class ReadExcleUtil {
    /**
     * 对外提供读取excel 的方法
     * */
    public static List<List<Object>> readExcel(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
//        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
                .substring(fileName.lastIndexOf(".") + 1);
        InputStream inputStream = file.getInputStream();
        if ("xls".equals(extension)) {
            return read2003Excel(inputStream);
        } else if ("xlsx".equals(extension)) {
            return read2007Excel(inputStream);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }
    /**
     * 读取 office 2003 excel
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static List<List<Object>> read2003Excel(InputStream file)
            throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();
        HSSFWorkbook hwb = new HSSFWorkbook(file);
        HSSFSheet sheet = hwb.getSheetAt(0);
        Object value = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        int counter = 0;
        for (int i = sheet.getFirstRowNum(); counter < sheet
                .getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            } else {
                counter++;
            }
            List<Object> linked = new LinkedList<Object>();
            int b = row.getLastCellNum();
            if(b <= 8){
                b = 8;
            }
            for (int j = row.getFirstCellNum(); j < b; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    if(i>0){
                        value="";
                    }
                }
                DecimalFormat df = new DecimalFormat("0");// 格式化 number String 字符
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
                DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
                switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            value = df.format(cell.getNumericCellValue());
                        } else if ("General".equals(cell.getCellStyle()
                                .getDataFormatString())) {
                            value = nf.format(cell.getNumericCellValue());
                        } else {
                            value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                    .getNumericCellValue()));
                        }
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_BLANK:
                        value = "";
                        break;
                    default:
                        value = cell.toString();
                }
//                if (value == null || "".equals(value)) {
//                    continue;
//                }
                if (value == null) {
                    continue;
                }
                linked.add(value);
            }
            list.add(linked);
        }
        return list;
    }
    /**
     * 读取Office 2007 excel
     * */
    private static List<List<Object>> read2007Excel(InputStream file)
            throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();
        // 构造 XSSFWorkbook 对象，strPath 传入文件路径
        XSSFWorkbook xwb = new XSSFWorkbook(file);
        // 读取第一章表格内容
        XSSFSheet sheet = xwb.getSheetAt(0);
        Object value = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        int counter = 0;
        for (int i = sheet.getFirstRowNum(); counter < sheet
                .getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            } else {
                counter++;
            }
            List<Object> linked = new LinkedList<Object>();
            int b = row.getLastCellNum();
            if(b <= 8){
                b = 8;
            }
            for (int j = row.getFirstCellNum(); j < b; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    if(i>0){
                        value="";
                    }
                }else {
                    DecimalFormat df = new DecimalFormat("0");// 格式化 number String 字符
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
                    DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_STRING:
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle()
                                    .getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else {
                                value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                        .getNumericCellValue()));
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            value = cell.getBooleanCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            value = "";
                            break;
                        default:
                            value = cell.toString();
                    }
//                if (value == null || "".equals(value)) {
//                    continue;
//                }
                    if (value == null) {
                        continue;
                    }
                }
                linked.add(value);
            }
            list.add(linked);
        }
        return list;
    }
//    public static void main(String[] args) {
//        try {
//            List<List<Object>> readExcel = readExcel(new File("D:\\testExcel.xlsx"));
//            for(List<Object> excel : readExcel){
//                System.out.println("读取数据"+excel);
//            }
//            // readExcel(new File("D:\\test.xls"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
