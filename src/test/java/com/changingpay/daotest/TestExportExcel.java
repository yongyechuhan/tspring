package com.changingpay.daotest;

import com.alibaba.fastjson.JSON;
import com.changingpay.base.LoadTestConfig;
import com.liuxin.tspring.dao.TableColumnsMapper;
import com.liuxin.tspring.dao.TablesModelMapper;
import com.liuxin.tspring.model.TableColumns;
import com.liuxin.tspring.model.TablesModel;
import com.liuxin.tspring.util.ExcelOprUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.TreeMap;

public class TestExportExcel extends LoadTestConfig{

    private Logger logger = LoggerFactory.getLogger(TestExportExcel.class);

    @Autowired
    private TableColumnsMapper tableColumnsMapper;

    @Autowired
    private TablesModelMapper tablesModelMapper;

    @Test
    public void testExportSingleExcel(){
        String tableSchema = "test_settle_db";
        TableColumns param = new TableColumns();
        param.setTableSchema(tableSchema);
        param.setTableName("MER_INFO");
        List<TableColumns> tableColumns = tableColumnsMapper.select(param);
        //读取模板文件
        Workbook workbook = ExcelOprUtils.readReportTemplate("exportExcelTemplate.xlsx");
        Sheet newSheet = workbook.cloneSheet(0);
        int columnDataStartRow = 6;
        Row template = newSheet.getRow(columnDataStartRow);
        int startIndex = 10;
        for(TableColumns tableColumn : tableColumns) {
            int columnNum = 0;
            TreeMap<Integer, Object> rowData = new TreeMap<>();
            rowData.put(columnNum++, tableColumn.getOrdinalPosition());
            rowData.put(columnNum++, tableColumn.getColumnComment());
            rowData.put(columnNum++, tableColumn.getColumnName());
            rowData.put(columnNum++, tableColumn.getDataType().toUpperCase());
            rowData.put(columnNum++, tableColumn.getColumnType().
                    replace(tableColumn.getDataType(), "")
                    .replace("(", "").replace(")", ""));
            if ("PRI".equals(tableColumn.getColumnKey())) {
                rowData.put(columnNum++, getCharacter());
            } else {
                rowData.put(columnNum++, "");
            }
            rowData.put(columnNum++, "");
            if ("YES".equals(tableColumn.getIsNullable())) {
                rowData.put(columnNum++, getCharacter());
            } else {
                rowData.put(columnNum++, "");
            }
            rowData.put(columnNum++, StringUtils.isEmpty(tableColumn.getColumnDefault()) ? "" : tableColumn.getColumnDefault());
            rowData.put(columnNum++, "");
            if ("MUL".equals(tableColumn.getColumnKey())) {
                rowData.put(startIndex++, getCharacter());
            }
            for (int i = 10; i < template.getLastCellNum(); i++) {
                if (!rowData.containsKey(i)) {
                    rowData.put(i, "");
                }
            }
            ExcelOprUtils.writeRow(newSheet, columnDataStartRow++, rowData, template);
        }
        ExcelOprUtils.genExcelReport(workbook, "E://report/test2.xlsx");
    }

    @Test
    public void testExportExcel(){
        String tableSchema = "test_settle_db";
        TablesModel tparam = new TablesModel();
        tparam.setTableSchema(tableSchema);
        List<TablesModel> tableModels = tablesModelMapper.select(tparam);
        //读取模板文件
        Workbook workbook = ExcelOprUtils.readReportTemplate("exportExcelTemplate.xlsx");
        int sheetNum = 1;
        if(!CollectionUtils.isEmpty(tableModels)){
            for(TablesModel tablesModel : tableModels){
                TableColumns param = new TableColumns();
                String tableName = tablesModel.getTableName();
                param.setTableSchema(tableSchema);
                param.setTableName(tableName);
                List<TableColumns> tableColumns = tableColumnsMapper.select(param);
                if(CollectionUtils.isEmpty(tableColumns) || tableColumns.size() < 1){
                    logger.info("不能被正确解析的表格{}", JSON.toJSONString(tableName));
                    return;
                }
                generatorExcel(workbook, tableColumns, tableName.toUpperCase(), tableName.toUpperCase(), sheetNum++);
            }
        }
        ExcelOprUtils.genExcelReport(workbook, "E://report/test.xlsx");
    }

    private void generatorExcel(Workbook workbook, List<TableColumns> tableColumns,
                                String cnTableName, String enTableName, int sheetNum){
        Sheet newSheet = workbook.cloneSheet(0);
        workbook.setSheetName(sheetNum, enTableName);
        Cell tbCNName = newSheet.getRow(1).getCell(1);
        tbCNName.setCellValue(cnTableName);
        Cell tbENName = newSheet.getRow(2).getCell(1);
        tbENName.setCellValue(enTableName);
        Cell tbAbsDesc = newSheet.getRow(3).getCell(1);
        tbAbsDesc.setCellValue(cnTableName);

        int columnDataStartRow = 6;
        int startIndex = 10;
        Row template = newSheet.getRow(columnDataStartRow);
        for(TableColumns tableColumn : tableColumns){
            int columnNum = 0;
            TreeMap<Integer, Object> rowData = new TreeMap<>();
            rowData.put(columnNum++, tableColumn.getOrdinalPosition());
            rowData.put(columnNum++, tableColumn.getColumnComment());
            rowData.put(columnNum++, tableColumn.getColumnName());
            rowData.put(columnNum++, tableColumn.getDataType().toUpperCase());
            rowData.put(columnNum++, tableColumn.getColumnType().
                    replace(tableColumn.getDataType(),"")
                    .replace("(","").replace(")",""));
            if("PRI".equals(tableColumn.getColumnKey())){
                rowData.put(columnNum++, getCharacter());
            } else {
                rowData.put(columnNum++, "");
            }
            rowData.put(columnNum++,"");
            if("YES".equals(tableColumn.getIsNullable())){
                rowData.put(columnNum++, getCharacter());
            } else {
                rowData.put(columnNum++, "");
            }
            rowData.put(columnNum++, StringUtils.isEmpty(tableColumn.getColumnDefault()) ? "" : tableColumn.getColumnDefault());
            rowData.put(columnNum++, tableColumn.getColumnComment());
            if("MUL".equals(tableColumn.getColumnKey())){
                rowData.put(startIndex++, getCharacter());
            }
            for(int i = 10; i < template.getLastCellNum(); i++){
                if(!rowData.containsKey(i)){
                    rowData.put(i, "");
                }
            }
            ExcelOprUtils.writeRow(newSheet, columnDataStartRow++, rowData, template);
        }
    }

    private static String getCharacter(){
        return "○";
    }
}
