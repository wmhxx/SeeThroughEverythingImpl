package wmhxx.tool.poi;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * excel的导出 测试下拉列表
 *
 * @author :WangMengHe
 * @since :2021-10-14 15:01
 **/
public class DownloadFilePoi {

    private static String[] SEX = {"男", "女"};

    private static String[] TYPE = {"流氓","郑洪亮"};


    public static void main(String[] args) {
        File outputFile = new File("/Users/wangmh/Desktop/" + System.currentTimeMillis() + ".xlsx");
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFile);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet excelSheet = workbook.createSheet();
            HSSFRow row2 = excelSheet.createRow(0);
            row2.createCell(0).setCellValue("性别");
            row2.createCell(1).setCellValue("类型");
            //demo 单独下拉列表
            toSheet(workbook, excelSheet, SEX, 'A', 1, 200);
            toSheet(workbook, excelSheet, TYPE, 'B', 1, 200);

            // 隐藏存储下拉列表数据的sheet；可以注释掉该行以便查看、理解存储格式
            for (int i = 1; i < workbook.getNumberOfSheets(); i++) {
                workbook.setSheetHidden(i, true);
            }

            workbook.write(outputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(outputStream!=null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("oj8k");
    }






    /**
     * 给sheet页，添加下拉列表
     *
     * @param workbook    excel文件，用于添加Name
     * @param targetSheet 级联列表所在sheet页
     * @param options     级联数据 比如性别【男、女】 【流氓、郑洪亮什么的】多级下拉列表
     * @param column      下拉列表所在列 从'A'开始 A->B依次类推
     * @param fromRow     下拉限制开始行 设置你的起始行数
     * @param endRow      下拉限制结束行 设置你的结束行数
     */
    public static void toSheet(Workbook workbook, Sheet targetSheet, Object[] options, char column, int fromRow, int endRow) {
        String hiddenSheetName = "sheet" + workbook.getNumberOfSheets();
        Sheet optionsSheet = workbook.createSheet(hiddenSheetName);
        String nameName = column + "_parent";
        int rowIndex = 0;
        for (Object option : options) {
            int columnIndex = 0;
            Row row = optionsSheet.createRow(rowIndex++);
            Cell cell = row.createCell(columnIndex++);
            cell.setCellValue(option.toString());
        }
        Name name = workbook.createName();
        name.setNameName(nameName);
        name.setRefersToFormula(hiddenSheetName + "!$A$1:$A$" + options.length);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(nameName);
        CellRangeAddressList regions = new CellRangeAddressList(fromRow, endRow, (int) column - 'A', (int) column - 'A');
        targetSheet.addValidationData(new HSSFDataValidation(regions, constraint));
    }



}

