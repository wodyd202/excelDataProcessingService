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
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleExcelDataProcessingService implements ExcelDataProcessingService {

    @Override
    public <T> Collection<T> readExcel(ExcelReadMetaData<T> metaData) {
        ExcelEntity excelEntity = metaData.getMapClassType().getAnnotation(ExcelEntity.class);
        try (Workbook workbook = StreamingReader.builder()
                .rowCacheSize(excelEntity.rowCacheSize())
                .bufferSize(excelEntity.bufferSize())
                .open(metaData.getExcelInputStream())) {

            log.info("start excel read, meta data : {}", metaData);
            Sheet sheet = workbook.getSheetAt(metaData.getStandardSheet());
            ExcelSheetDataReader<T> dataReader = new ExcelSheetDataReader<>(sheet, metaData);
            Collection<T> result = dataReader.execute(metaData.getCollectionType());
            log.info("end excel read");
            return result;
        } catch (IOException e) {
            throw new InvalidExcelFormException(e, InvalidExcelType.INVALID_EXCEL);
        }
    }

    @Override
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
        return new SXSSFWorkbook(xssfWorkbook, excelEntity.rowAccessWindowSize());
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
