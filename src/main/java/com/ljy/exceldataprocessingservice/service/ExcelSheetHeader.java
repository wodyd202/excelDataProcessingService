package com.ljy.exceldataprocessingservice.service;

import com.ljy.exceldataprocessingservice.service.metadata.ExcelColum;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelReadMetaData;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelWriteMetaData;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Getter

public class ExcelSheetHeader<T> {
    private Map<Integer, String> sheetHeaderMap = new HashMap<>();

    private Map<String, Field> columFieldMap = new HashMap<>();

    public ExcelSheetHeader(ExcelReadMetaData<T> metaData, Sheet targetSheet) {
        setSheetHeaderMap(metaData, targetSheet);
        setColumFieldMap(metaData);
    }

    public ExcelSheetHeader(ExcelWriteMetaData<T> metaData) {
        setSheetHeaderMap(metaData);
        setColumFieldMap(metaData);
    }

    private void setSheetHeaderMap(ExcelWriteMetaData<T> metaData) {
        this.sheetHeaderMap = Stream.of(metaData.getDataType().getDeclaredFields())
        .filter(field -> {
            field.setAccessible(true);
            return field.getAnnotation(ExcelColum.class) != null;
        })
        .collect(toMap(field -> field.getAnnotation(ExcelColum.class).order(), field -> field.getAnnotation(ExcelColum.class).headerName()));
    }

    private void setColumFieldMap(ExcelWriteMetaData<T> metaData) {
        this.columFieldMap = Stream.of(metaData.getDataType().getDeclaredFields())
        .filter(field -> {
            field.setAccessible(true);
            return field.getAnnotation(ExcelColum.class) != null;
        })
        .collect(toMap(field -> field.getAnnotation(ExcelColum.class).headerName(), field -> field));
    }

    private void setSheetHeaderMap(ExcelReadMetaData<T> metaData, Sheet targetSheet) {
        int headerRowIdx = metaData.getStandardRow() - 1;
        Row headerRow = getHeaderRow(headerRowIdx, targetSheet);
        int cellIdx = 0;
        for (Cell cell : headerRow) {
            this.sheetHeaderMap.put(cellIdx++, cell.getStringCellValue());
        }
    }

    private Row getHeaderRow(int headerRowIdx, Sheet targetSheet) {
        Row headerRow = null;
        for (Row row : targetSheet) {
            headerRow = row;
            if(headerRowIdx == 0) {
                break;
            }
            headerRowIdx--;
        }
        return headerRow;
    }

    private void setColumFieldMap(ExcelReadMetaData<T> metaData) {
        for (Field field : metaData.getMapClassType().getDeclaredFields()) {
            ExcelColum annotation = field.getAnnotation(ExcelColum.class);
            if (annotation != null) {
                this.columFieldMap.put(annotation.headerName(), field);
            }
        }
    }

    public String get(int cellIdx) {
        return sheetHeaderMap.get(cellIdx);
    }

}
