package com.ljy.exceldataprocessingservice.service;

import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelFormException;
import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelType;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelData;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelDataValidationResult;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelReadMetaData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class ExcelSheetDataReader<T> {
    private final Sheet targetSheet;
    private final ExcelReadMetaData<T> readMetaData;
    private final ExcelSheetHeader<T> sheetHeader;

    private final ExcelRowDataMapper<T> excelRowDataMapper;

    public ExcelSheetDataReader(Sheet targetSheet, ExcelReadMetaData<T> readMetaData) {
        this.targetSheet = targetSheet;
        this.readMetaData = readMetaData;
        this.sheetHeader = new ExcelSheetHeader<>(readMetaData, targetSheet);
        this.excelRowDataMapper = new ExcelRowDataMapper<>(sheetHeader, readMetaData);
    }

    public Collection execute(Class<? extends Collection> collectionType) {
        Collection collection = getNewCollection(collectionType);
        int readCount = readMetaData.getStandardRow();
        for(Row row : targetSheet) {
            T obj = excelRowDataMapper.mapFrom(row);
            ExcelData excelData = (ExcelData) obj;
            if(excelData.isEmpty()) {
                continue;
            }
            ExcelDataValidationResult validationResult = excelData.validate();
            if (validationResult.hasError()) {
                String message = String.format("[%d 행] %s", readCount, validationResult.getMessage());
                throw new InvalidExcelFormException(message, InvalidExcelType.INVALID_DATA);
            }
            collection.add(obj);
            readCount++;
        }
        return collection;
    }

    private Collection getNewCollection(Class<? extends Collection> collectionType) {
        try {
            return collectionType.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
