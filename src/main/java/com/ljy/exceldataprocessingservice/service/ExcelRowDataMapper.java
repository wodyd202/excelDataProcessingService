package com.ljy.exceldataprocessingservice.service;

import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelFormException;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelReadMetaData;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

@Getter
public class ExcelRowDataMapper<T> {
    private int notSetCount = 0;

    private final ExcelReadMetaData<T> metaData;

    private final ExcelSheetHeader<T> excelSheetHeader;

    public ExcelRowDataMapper(ExcelSheetHeader<T> excelSheetHeader, ExcelReadMetaData<T> metaData) {
        this.excelSheetHeader = excelSheetHeader;
        this.metaData = metaData;
    }

    public T execute(Row row) {
        T instance = newInstance();
        int cellIdx = 0;
        for (Cell cell : row) {
            String headerTitle = excelSheetHeader.get(cellIdx);
            set(instance, headerTitle, cell);
            cellIdx++;
        }
        return instance;
    }

    private T newInstance() {
        try {
            Constructor<T> constructor = metaData.getMapClassType().getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void set(T instance, String headerTitle, Cell cell) {
        Field field = excelSheetHeader.getColumFieldMap().get(headerTitle);
        field.setAccessible(true);

        if (isNumberTypeCell(cell)) {
            setNumberValue(cell, instance, field);
        }
        if (isStringTypeCell(cell)) {
            setStringValue(cell, instance, field);
        }
        if (isBlankCell(cell)) {
            notSetCount++;
        }
    }

    private void setStringValue(Cell cell, T instance, Field field) {
        try {
            if (isStringType(field)) {
                field.set(instance, cell.getStringCellValue());
            }
            if (field.getType().isEnum()) {
                field.set(instance, Enum.valueOf((Class<? extends Enum>) field.getType(), cell.getStringCellValue()));
            }
        } catch (Exception e) {
            String message = String.format("[%d] 번째 행, [%d] 번째 열 데이터 타입이 일치하지 않습니다.", cell.getRowIndex(), cell.getColumnIndex());
            throw new InvalidExcelFormException(message);
        }
    }

    private void setNumberValue(Cell cell, T instance, Field field) {
        try {
            if (isIntegerTypeField(field)) {
                field.setInt(instance, (int) cell.getNumericCellValue());
            } else if (isLongTypeField(field)) {
                field.setLong(instance, (long) cell.getNumericCellValue());
            } else if (isDoubleTypeField(field)) {
                field.setDouble(instance, cell.getNumericCellValue());
            }
        } catch (Exception e) {
            String message = String.format("[%d] 번째 행, [%d] 번째 열 데이터 타입이 일치하지 않습니다.", cell.getRowIndex(), cell.getColumnIndex());
            throw new InvalidExcelFormException(message);
        }
    }

    private boolean isStringTypeCell(Cell cell) {
        return cell.getCellType() == CellType.STRING;
    }

    private boolean isBlankCell(Cell cell) {
        return cell.getCellType() == CellType.BLANK;
    }

    private boolean isNumberTypeCell(Cell cell) {
        return cell.getCellType() == CellType.NUMERIC;
    }

    private boolean isIntegerTypeField(Field field) {
        return field.getType() == int.class || field.getType() == Integer.class;
    }

    private boolean isLongTypeField(Field field) {
        return field.getType() == long.class || field.getType() == Long.class;
    }

    private boolean isDoubleTypeField(Field field) {
        return field.getType() == double.class || field.getType() == Double.class;
    }

    private boolean isStringType(Field field) {
        return field.getType() == String.class;
    }
}
