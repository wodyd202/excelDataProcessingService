package com.ljy.exceldataprocessingservice.service;

import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelFormException;
import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelType;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelEntity;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelReadMetaData;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelWriteMetaData;
import com.monitorjbl.xlsx.StreamingReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelDataProcessingService {

    public <T> Collection<T> readExcel(ExcelReadMetaData<T> metaData) {
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

    @Async("excelWriteExecutor")
    public <T> void writeExcel(ExcelWriteMetaData<T> metaData, Consumer<byte[]> consumer) {
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            Workbook workbook = new SXSSFWorkbook(xssfWorkbook, 1000);
            Sheet sheet = workbook.createSheet(metaData.getSheetName());
            ExcelSheetDataWriter dataWriter = new ExcelSheetDataWriter(sheet, metaData);
            dataWriter.execute();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            byte[] result = bos.toByteArray();
            workbook.close();
            bos.close();
            consumer.accept(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
