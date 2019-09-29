package com.kzh.sys.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtil {
    /**
     * 导出Excel
     *
     * @param title       表格标题名
     * @param headers     表格属性列名数组
     * @param fields      表格属性列名所对应的对象属性数组
     * @param data        需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的 javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param datePattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    public static HSSFWorkbook exportExcel(String title, String[] headers, String[] fields, List<Object> data, String datePattern) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        int sheetMaxCount = 60000;
//	      int sheetMaxCount = 10000;

        int sheetcount = data.size() % sheetMaxCount == 0 ? data.size() / sheetMaxCount : ((data.size() / sheetMaxCount) + 1);
        for (int sheetIndex = 0; sheetIndex < sheetcount; sheetIndex++) {
            if (data.size() < 1) {
                break;
            }
            List<Object> dataset = null;
            if (data.size() > sheetMaxCount) {
                dataset = data.subList(0, sheetMaxCount);
                data = data.subList(sheetMaxCount, data.size());
            } else {
                dataset = data;
            }


            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet(title + "-" + (sheetIndex + 1));
            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth((short) 15);
            // 声明一个画图的顶级管理器
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            //产生表格标题行
            HSSFRow row = sheet.createRow(0);
            for (short i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            //遍历集合数据，产生数据行
            HSSFFont font3 = workbook.createFont();
            for (int index = 0; index < dataset.size(); index++) {
                row = sheet.createRow((index + 1));//需要考虑第一行被Header占用
                Object t = dataset.get(index);
                //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
                Field[] fieldsArray = t.getClass().getDeclaredFields();
                short cellIndex = 0;

                for (String f : fields) {
                    String fieldName = "";
                    boolean hasField = false;
                    for (short i = 0; i < fieldsArray.length; i++) {
                        Field field = fieldsArray[i];
                        fieldName = field.getName();
                        if (fieldName.equals(f)) {
                            hasField = true;
                            break;
                        }
                    }
                    if (!hasField) {
                        continue;
                    }
                    HSSFCell cell = row.createCell(cellIndex);//单元列右移一格
                    cell.setCellStyle(style2);
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try {
                        Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        //判断值的类型后进行强制类型转换
                        String textValue = null;
                        if (value instanceof Boolean) {
                            boolean bValue = (Boolean) value;
                            textValue = "True";
                            if (!bValue) {
                                textValue = "False";
                            }
                        } else if (value instanceof Date) {
                            Date date = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
                            textValue = sdf.format(date);
                        } else if (value instanceof byte[]) {
                            // 有图片时，设置行高为60px;
                            row.setHeightInPoints(60);
                            // 设置图片所在列宽度为80px,注意这里单位的一个换算
                            sheet.setColumnWidth(cellIndex, (short) (35.7 * 80));
                            // sheet.autoSizeColumn(i);
                            byte[] bsValue = (byte[]) value;
                            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
                            anchor.setAnchorType(2);
                            patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                        } else {
                            //其它数据类型都当作字符串简单处理
                            textValue = value == null ? "" : value.toString();
                        }
                        //如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                        if (textValue != null) {
                            Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                            Matcher matcher = p.matcher(textValue);
                            if (matcher.matches()) {
                                //是数字当作double处理
                                cell.setCellValue(Double.parseDouble(textValue));
                            } else {
                                HSSFRichTextString richString = new HSSFRichTextString(textValue);
                                font3.setColor(HSSFColor.BLUE.index);
                                richString.applyFont(font3);
                                cell.setCellValue(richString);
                            }
                        }
                        cellIndex++;
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } finally {
                        //清理资源
                    }
                }
            }
        }
        return workbook;
    }

    /**
     * 导入excel
     *
     * @param in
     * @param ignoreRows   从哪一行开始读数据 (第一行为标题，不取)
     * @param ignoreSheets 读取到多少行，-1是所有都读取
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    @SuppressWarnings({"deprecation"})
    public static List<String[]> importExcel(BufferedInputStream in, int ignoreRows, int ignoreSheets)
            throws FileNotFoundException, IOException {
        List<String[]> result = new ArrayList<String[]>();
        int rowSize = 0;
        // 打开HSSFWorkbook
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        HSSFCell cell = null;
        try {
            fs = new POIFSFileSystem(in);
            wb = new HSSFWorkbook(fs);
            for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                if (ignoreSheets != -1 && sheetIndex + 1 > ignoreSheets)
                    break;

                HSSFSheet st = wb.getSheetAt(sheetIndex);
                // 第一行为标题，不取
                for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
                    HSSFRow row = st.getRow(rowIndex);
                    if (row == null) {
                        //空白行也记录
                        result.add(new String[rowSize]);
                        continue;
                    }
                    //没有指定索引的那一行，则不记录
                    if (row.getLastCellNum() == -1) {
                        result.add(null);
                        continue;
                    }
                    int tempRowSize = row.getLastCellNum() + 1;
                    if (tempRowSize > rowSize) {
                        rowSize = tempRowSize;
                    }
                    String[] values = new String[rowSize];
                    Arrays.fill(values, "");
                    for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
                        String value = "";
                        cell = row.getCell(columnIndex);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case HSSFCell.CELL_TYPE_STRING:
                                    value = cell.getStringCellValue();
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        Date date = cell.getDateCellValue();
                                        if (date != null) {
                                            value = new SimpleDateFormat("yyyy-MM-dd")
                                                    .format(date);
                                        } else {
                                            value = "";
                                        }
                                    } else {
                                        value = new DecimalFormat("0").format(cell
                                                .getNumericCellValue());
                                    }
                                    break;
                                case HSSFCell.CELL_TYPE_FORMULA:
                                    // 导入时如果为公式生成的数据则无值
                                    if (!cell.getStringCellValue().equals("")) {
                                        value = cell.getStringCellValue();
                                    } else {
                                        value = cell.getNumericCellValue() + "";
                                    }
                                    break;
                                case HSSFCell.CELL_TYPE_BLANK:
                                    break;
                                case HSSFCell.CELL_TYPE_BOOLEAN:
                                    value = (cell.getBooleanCellValue() == true ? "Y"
                                            : "N");
                                    break;
                                default:
                                    value = "";
                            }
                        }
                        values[columnIndex] = rightTrim(value);
                    }
                    result.add(values);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb = null;
            fs = null;
            if (in != null)
                in.close();
        }
        return result;
    }

    /**
     * 去掉字符串右边的空格
     *
     * @param str 要处理的字符串
     * @return 处理后的字符串
     */
    private static String rightTrim(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        for (int i = length - 1; i >= 0; i--) {
            if (str.charAt(i) != 0x20) {
                break;
            }
            length--;
        }
        return str.substring(0, length);
    }

    /**
     * 导入excel
     *
     * @param in
     * @param ignoreRows   从哪一行开始读数据 (第一行为标题，不取)
     * @param ignoreSheets 读取到多少行，-1是所有都读取
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    @SuppressWarnings({"deprecation"})
    public static List<String[]> importExcel(BufferedInputStream in, int ignoreRows, int ignoreSheets, String dateFormat)
            throws FileNotFoundException, IOException {
        List<String[]> result = new ArrayList<String[]>();
        int rowSize = 0;
        // 打开HSSFWorkbook
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        HSSFCell cell = null;
        try {
            fs = new POIFSFileSystem(in);
            wb = new HSSFWorkbook(fs);
            for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                if (ignoreSheets != -1 && sheetIndex + 1 > ignoreSheets)
                    break;

                HSSFSheet st = wb.getSheetAt(sheetIndex);
                // 第一行为标题，不取
                for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
                    HSSFRow row = st.getRow(rowIndex);
                    if (row == null) {
                        //空白行也记录
                        result.add(new String[rowSize]);
                        continue;
                    }
                    //没有指定索引的那一行，则不记录
                    if (row.getLastCellNum() == -1) {
                        result.add(null);
                        continue;
                    }
                    int tempRowSize = row.getLastCellNum() + 1;
                    if (tempRowSize > rowSize) {
                        rowSize = tempRowSize;
                    }
                    String[] values = new String[rowSize];
                    Arrays.fill(values, "");
                    for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
                        String value = "";
                        cell = row.getCell(columnIndex);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case HSSFCell.CELL_TYPE_STRING:
                                    value = cell.getStringCellValue();
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        Date date = cell.getDateCellValue();
                                        if (date != null) {
                                            value = new SimpleDateFormat(dateFormat)
                                                    .format(date);
                                        } else {
                                            value = "";
                                        }
                                    } else {
                                        value = new DecimalFormat("0").format(cell
                                                .getNumericCellValue());
                                    }
                                    break;
                                case HSSFCell.CELL_TYPE_FORMULA:
                                    // 导入时如果为公式生成的数据则无值
                                    if (!cell.getStringCellValue().equals("")) {
                                        value = cell.getStringCellValue();
                                    } else {
                                        value = cell.getNumericCellValue() + "";
                                    }
                                    break;
                                case HSSFCell.CELL_TYPE_BLANK:
                                    break;
                                case HSSFCell.CELL_TYPE_BOOLEAN:
                                    value = (cell.getBooleanCellValue() == true ? "Y"
                                            : "N");
                                    break;
                                default:
                                    value = "";
                            }
                        }
                        values[columnIndex] = rightTrim(value);
                    }
                    result.add(values);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb = null;
            fs = null;
            if (in != null)
                in.close();
        }
        return result;
    }
}