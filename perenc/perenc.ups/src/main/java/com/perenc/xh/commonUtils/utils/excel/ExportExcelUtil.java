package com.perenc.xh.commonUtils.utils.excel;

import org.apache.commons.collections4.MapUtils;
import org.apache.poi.hssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/20 10:08
 **/
public class ExportExcelUtil {

    public static void exportExcel(String sheetTitle, List<LinkedHashMap<String, Object>> dataSet, OutputStream out) {
        exportExcel(sheetTitle, null, dataSet, out, "yyyy-MM-dd");
    }

    public static void exportExcel(String[] headers, List<LinkedHashMap<String, Object>> dataSet, OutputStream out) {
        exportExcel("客户订单信息", headers, dataSet, out, "yyyy-MM-dd");
    }

    public static void exportExcel(String[] headers, List<LinkedHashMap<String, Object>> dataSet, OutputStream out, String pattern) {
        exportExcel("客户订单信息", headers, dataSet, out, pattern);
    }

    public static void exportExcel(String title, String[] headers, List<LinkedHashMap<String, Object>> dataSet, OutputStream out, String pattern) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为22个字节
        sheet.setDefaultColumnWidth(22);

        HSSFCellStyle headerStyle = workbook.createCellStyle(); //标题样式
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont headerFont = workbook.createFont();    //标题字体
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setFontHeightInPoints((short)14);
        headerStyle.setFont(headerFont);


        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.U_SINGLE);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
        comment.setAuthor("leno");

        //产生表格标题行
        HSSFRow row;
        for (int rowNum = 0; rowNum < dataSet.size(); rowNum ++){
            row = sheet.createRow(rowNum);
            Map<String, Object> map = dataSet.get(rowNum);
            int cellNum = 0;
            for (String key :  map.keySet()){
                HSSFCell cell = row.createCell(cellNum);
                cell.setCellValue(MapUtils.getString(map, key));
                cell.setCellStyle(rowNum == 0 ? headerStyle : style);
                cellNum++;
            }
        }

        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        // 测试
//        List<LinkedHashMap<String, Object>> dataSet = new ArrayList<>();
//
//        LinkedHashMap<String, Object> student0 = new LinkedHashMap<>();
//        student0.put("number", "学号");
//        student0.put("name", "姓名");
//        student0.put("age", "年龄");
//        student0.put("sex", "性别");
//        dataSet.add(student0);
//
//        LinkedHashMap<String, Object> student = new LinkedHashMap<>();
//        student.put("number", 10000001);
//        student.put("name", "张三");
//        student.put("age", 20);
//        student.put("sex", "男");
//        dataSet.add(student);
//        LinkedHashMap<String, Object> student1 = new LinkedHashMap<>();
//        student1.put("number", 20000002);
//        student1.put("name", "李四");
//        student1.put("age", 24);
//        student1.put("sex", "女");
//        dataSet.add(student1);
//        LinkedHashMap<String, Object> student2 = new LinkedHashMap<>();
//        student2.put("number", 30000003);
//        student2.put("name", "王五");
//        student2.put("age", 22);
//        student2.put("sex", "男");
//        dataSet.add(student2);
//        // 测试图书
//        try {
//            OutputStream out = new FileOutputStream("g:/1.xls");
//            exportExcel("用户信息",dataSet, out);
//            out.close();
//            System.out.println("excel导出成功！");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
