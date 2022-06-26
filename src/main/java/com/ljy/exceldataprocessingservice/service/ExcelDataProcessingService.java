package com.ljy.exceldataprocessingservice.service;

import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelFormException;
import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelType;
import com.ljy.exceldataprocessingservice.service.exception.NoProccessExcelWriteException;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelEntity;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelReadMetaData;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelSheetMetaData;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelWriteMetaData;
import com.monitorjbl.xlsx.StreamingReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

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
            throw new InvalidExcelFormException(e, InvalidExcelType.INVALID_EXCEL);
        }
    }

    public <T> byte[] writeExcel(ExcelWriteMetaData<T> metaData) {
        ExcelEntity excelEntity = metaData.getDataType().getAnnotation(ExcelEntity.class);
        try (
             Workbook workbook = createWorkbook(excelEntity);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()
            ){
            writeExcel(metaData, workbook);
            workbook.write(bos);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new NoProccessExcelWriteException(e);
        }
    }

    private Workbook createWorkbook(ExcelEntity excelEntity) {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        Workbook workbook = new SXSSFWorkbook(xssfWorkbook, excelEntity.rowAccessWindowSize());
        return workbook;
    }

    private <T> void writeExcel(ExcelWriteMetaData<T> metaData, Workbook workbook) {
        ExcelSheetMetaData excelSheetMetaData = metaData.getSheetMetaData();
        Sheet sheet = workbook.createSheet(excelSheetMetaData.getValue());
        ExcelSheetDataWriter<T> dataWriter = new ExcelSheetDataWriter<>(sheet, metaData);
        log.info("start excel write, meta data : {}", metaData);
        dataWriter.execute();
        log.info("end excel write");
    }
}
