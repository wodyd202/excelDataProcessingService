package com.ljy.exceldataprocessingservice.service;

import com.ljy.exceldataprocessingservice.testobj.BasicExcelFormData_1;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelReadMetaData;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExcelDataProcessingServiceTest {
    private ExcelDataProcessingService excelDataProcessingService = new ExcelDataProcessingService();

    @Test
    void 엑셀_내의_데이터_조회_1() throws IOException {
        // given
        ClassPathResource classPathResource = new ClassPathResource("fixture/basic-form-1.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        ExcelReadMetaData<BasicExcelFormData_1> metaData = new ExcelReadMetaData<>(inputStream, 2, 0, BasicExcelFormData_1.class, ArrayList.class);

        // when
        Collection<BasicExcelFormData_1> testExcelObjects = excelDataProcessingService.readData(metaData);

        // then
        assertEquals(4, testExcelObjects.size());
    }
}
