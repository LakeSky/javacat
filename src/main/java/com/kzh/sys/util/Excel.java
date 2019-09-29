package com.kzh.sys.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Excel {
    public static HSSFWorkbook simpleExportExcel(List<String[]> list) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        HSSFRow row;
        HSSFCell cell;
        if (CollectionUtil.isNotEmpty(list)) {
            int length = list.get(0).length;
            for (int i = 0; i < length; i++) {
                sheet.setColumnWidth(i, 20 * 256);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i);
            String[] strs = list.get(i);
            for (int j = 0; j < strs.length; j++) {
                cell = row.createCell(j);
                cell.setCellValue(strs[j]);
            }
        }
        return wb;
    }

    public static HSSFWorkbook simpleExportExcel(List<String[]> list, int[] columnWidth) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        HSSFRow row;
        HSSFCell cell;
        for (int i = 0; i < columnWidth.length; i++) {
            sheet.setColumnWidth(i, columnWidth[i] * 256);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i);
            String[] strs = list.get(i);
            for (int j = 0; j < strs.length; j++) {
                cell = row.createCell(j);
                cell.setCellValue(strs[j]);
            }
        }
        return wb;
    }

    public static List obtainFirstSheetFirstColumn(File file) throws Exception {
        List list = new ArrayList();
        Workbook workBook = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            workBook = new XSSFWorkbook(fis);
        } catch (Exception ex) {
            workBook = new HSSFWorkbook(new FileInputStream(file));
        }
        Sheet sheet = workBook.getSheetAt(0);
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(0);
            list.add(getValue(cell));
        }

        return list;
    }

    public static List<String[]> obtainFirstSheetAllColumn(File file, boolean skipFirstRow) throws Exception {
        List<String[]> list = new ArrayList<String[]>();
        Workbook workBook = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            workBook = new XSSFWorkbook(fis);
        } catch (Exception ex) {
            workBook = new HSSFWorkbook(new FileInputStream(file));
        }
        Sheet sheet = workBook.getSheetAt(0);
        int i = 0;
        if (skipFirstRow) {
            i = 1;
        }
        for (; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String[] strs;
            if (row == null) {
                strs = new String[sheet.getRow(i + 1).getLastCellNum() + 1];
            } else {
                strs = new String[row.getLastCellNum() + 1];
                for (int j = 0; j <= row.getLastCellNum(); j++) {
                    strs[j] = row.getCell(j) == null ? "" : row.getCell(j).toString();
                }
            }
            if (!SysUtil.isEmpty(strs)) {
                list.add(strs);
            }
        }
        return list;
    }

    public static List<String[]> obtainFirstSheetAllColumn(InputStream inputStream, boolean skipFirstRow) throws Exception {
        List<String[]> list = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        Workbook workBook = null;
        try {
            workBook = new HSSFWorkbook(inputStream);
        } catch (Exception ex) {
            workBook = new XSSFWorkbook(inputStream);
        }
        Sheet sheet = workBook.getSheetAt(0);
        int i = 0;
        Row firstRow = sheet.getRow(0);
        if (skipFirstRow) {
            i = 1;
        }
        for (; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String[] strs;
            if (row != null) {
                strs = new String[firstRow.getLastCellNum() + 2];
                for (int j = 0; j <= firstRow.getLastCellNum(); j++) {
                    if (row.getCell(j) == null) {
                        strs[j] = row.getCell(j) == null ? "" : row.getCell(j).toString();
                    } else {
                        switch (row.getCell(j).getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:// 数字  
                                strs[j] = row.getCell(j) == null ? "" : df.format(row.getCell(j).getNumericCellValue());
                                break;
                            default:
                                strs[j] = row.getCell(j) == null ? "" : row.getCell(j).toString();
                                break;
                        }
                    }
                }
                if (!SysUtil.isEmpty(strs)) {
                    list.add(strs);
                }
            }
        }
        return list;
    }

    public static List<String> obtainFirstSheetColumn(File file, boolean skipFirstRow, int columnIndex) throws Exception {
        List<String> list = new ArrayList<String>();
        Workbook workBook = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            workBook = new XSSFWorkbook(fis);
        } catch (Exception ex) {
            workBook = new HSSFWorkbook(new FileInputStream(file));
        }
        Sheet sheet = workBook.getSheetAt(0);
        int i = 0;
        if (skipFirstRow) {
            i = 1;
        }
        for (; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            list.add(row.getCell(columnIndex) == null ? "" : row.getCell(columnIndex).toString());
        }
        return list;
    }

    private static String getValue(Cell cell) {
        if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }
}
