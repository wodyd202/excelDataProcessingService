package com.ljy.exceldataprocessingservice.service;

import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelFormException;
import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelType;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelReadMetaData;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class ExcelRowDataMapper<T> {

    private final ExcelReadMetaData<T> metaData;

    private final ExcelSheetHeader<T> excelSheetHeader;

    private final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    public ExcelRowDataMapper(ExcelSheetHeader<T> excelSheetHeader, ExcelReadMetaData<T> metaData) {
        this.excelSheetHeader = excelSheetHeader;
        this.metaData = metaData;
    }

    public T mapFrom(Row row) {
        T instance = newInstance();
        int cellIdx = 0;
        for (Cell cell : row) {
            verifyValidCell(cellIdx, cell);
            String headerTitle = excelSheetHeader.get(cellIdx);
            bindingCellToField(instance, headerTitle, cell);
            cellIdx++;
        }
        return instance;
    }

    private void verifyValidCell(int cellIdx, Cell cell) {
        if(cellIdx != cell.getColumnIndex()) {
            String message = String.format("엑셀 양식이 올바르지 않습니다. [%d 행]을 다시 확인해주세요.", cell.getRowIndex() + 1);
            throw new InvalidExcelFormException(message, InvalidExcelType.INVALID_EXCEL_BODY);
        }
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

    public void bindingCellToField(T instance, String headerTitle, Cell cell) {
        Field field = excelSheetHeader.getColumFieldMap().get(headerTitle);
        field.setAccessible(true);

        if (isNumberTypeCell(cell)) {
            setNumberValue(cell, instance, field);
        }
        if (isStringTypeCell(cell)) {
            setStringValue(cell, instance, field);
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
            throw new InvalidExcelFormException(message, InvalidExcelType.NO_MATCH_CELL_TYPE);
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
            } else if(isDateTypeField(field)) {
                field.set(instance, SDF.parse(cell.getStringCellValue()));
            }
        } catch (Exception e) {
            String message = String.format("[%d] 번째 행, [%d] 번째 열 데이터 타입이 일치하지 않습니다.", cell.getRowIndex() + 1, cell.getColumnIndex());
            throw new InvalidExcelFormException(message, InvalidExcelType.NO_MATCH_CELL_TYPE);
        }
    }

    private boolean isStringTypeCell(Cell cell) {
        return cell.getCellType() == CellType.STRING;
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

    private boolean isDateTypeField(Field field) {
        return field.getType() == Date.class;
    }

    private boolean isStringType(Field field) {
        return field.getType() == String.class;
    }
}
