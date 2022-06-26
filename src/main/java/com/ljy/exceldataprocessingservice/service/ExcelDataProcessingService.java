package com.ljy.exceldataprocessingservice.service;

import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelFormException;
import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelType;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelEntity;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelReadMetaData;
import com.monitorjbl.xlsx.StreamingReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelDataProcessingService {

    public <T> Collection<T> readData(ExcelReadMetaData<T> metaData) {
        ExcelEntity excelEntity = metaData.getMapClassType().getAnnotation(ExcelEntity.class);
        try (Workbook workbook = StreamingReader.builder()
                .rowCacheSize(excelEntity.rowCacheSize())
                .bufferSize(excelEntity.bufferSize())
                .open(metaData.getExcelInputStream())) {

            log.info("start excel read, meta data : {}", metaData);
            Sheet sheet = workbook.getSheetAt(0);
            ExcelSheetDataReader<T> dataReader = new ExcelSheetDataReader<>(sheet, metaData);
            Collection<T> result = dataReader.execute(metaData.getCollectionType());
            log.info("end excel read");
            return result;

        } catch (IOException e) {
            log.error("invalid excel form exception", e);
            throw new InvalidExcelFormException(e, InvalidExcelType.INVALID_EXCEL);
        }
    }
}
