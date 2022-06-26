package com.ljy.exceldataprocessingservice.service;

import com.ljy.exceldataprocessingservice.service.metadata.ExcelColum;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelWriteMetaData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class ExcelSheetDataWriter<T> {
    private final Sheet targetSheet;
    private final ExcelWriteMetaData<T> metaData;

    public ExcelSheetDataWriter(Sheet targetSheet, ExcelWriteMetaData<T> metaData) {
        this.targetSheet = targetSheet;
        this.metaData = metaData;
    }

    public void execute() {
        ExcelSheetHeader<T> sheetHeader = new ExcelSheetHeader<>(metaData);

        drawTitle();
        drawHeader(sheetHeader);
        drawExcelBody(sheetHeader);
    }

    private void drawTitle() {
        Row titleRow = targetSheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(metaData.getSheetTitle());
    }

    private void drawHeader(ExcelSheetHeader<T> sheetHeader) {
        Row headerRow = targetSheet.createRow(1);

        Map<Integer, String> sheetHeaderMap = sheetHeader.getSheetHeaderMap();
        for (Integer order : sheetHeaderMap.keySet()) {
            Cell headerCell = headerRow.createCell(order);
            headerCell.setCellValue(sheetHeaderMap.get(order));
        }
    }

    private void drawExcelBody(ExcelSheetHeader<T> sheetHeader) {
        Map<Integer, String> sheetHeaderMap = sheetHeader.getSheetHeaderMap();
        Map<String, Field> columFieldMap = sheetHeader.getColumFieldMap();
        Collection<T> datas = metaData.getDatas();

        int idx = 2;
        for (T data : datas) {
            Row row = targetSheet.createRow(idx);
            for (Integer order : sheetHeaderMap.keySet()) {
                String header = sheetHeaderMap.get(order);
                Field field = columFieldMap.get(header);
                Cell cell = row.createCell(order);
                try {
                    if (field.getType() == Long.class || field.getType() == long.class) {
                        cell.setCellValue((long) field.get(data));
                    }
                    if (field.getType() == Integer.class || field.getType() == int.class) {
                        cell.setCellValue((int) field.get(data));
                    }
                    if (field.getType() == String.class) {
                        cell.setCellValue((String) field.get(data));
                    }
                    if (field.getType().isEnum()) {
                        cell.setCellValue(field.get(data).toString());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            idx++;
        }
    }
}
