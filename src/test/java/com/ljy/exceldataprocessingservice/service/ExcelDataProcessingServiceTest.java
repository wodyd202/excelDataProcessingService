package com.ljy.exceldataprocessingservice.service;

import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelFormException;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelReadMetaData;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelWriteMetaData;
import com.ljy.exceldataprocessingservice.testobj.BasicExcelFormData_1;
import com.ljy.exceldataprocessingservice.testobj.BasicExcelFormData_2;
import com.ljy.exceldataprocessingservice.testobj.BasicExcelFormData_3;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExcelDataProcessingServiceTest {
    private ExcelDataProcessingService excelDataProcessingService = new ExcelDataProcessingService();

    @Test
    void 엑셀_내의_데이터_조회_1() throws IOException {
        // given
        ClassPathResource classPathResource = new ClassPathResource("fixture/data-type-form.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        ExcelReadMetaData<BasicExcelFormData_2> metaData = new ExcelReadMetaData<>(inputStream, 2, 0, BasicExcelFormData_2.class, ArrayList.class);

        // when
        Collection<BasicExcelFormData_2> testExcelObjects = excelDataProcessingService.readExcel(metaData);

        // then
        assertEquals(4, testExcelObjects.size());
    }

    @Test
    void 엑셀_내의_데이터_조회_2() throws IOException {
        // given
        ClassPathResource classPathResource = new ClassPathResource("fixture/basic-form.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        ExcelReadMetaData<BasicExcelFormData_1> metaData = new ExcelReadMetaData<>(inputStream, 2, 0, BasicExcelFormData_1.class, ArrayList.class);

        // when
        Collection<BasicExcelFormData_1> testExcelObjects = excelDataProcessingService.readExcel(metaData);

        // then
        assertEquals(4, testExcelObjects.size());
    }

    @Test
    void 대용량_614_106건_데이터_조회() throws IOException {
        // given
        ClassPathResource classPathResource = new ClassPathResource("fixture/big-file-form.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        ExcelReadMetaData<BasicExcelFormData_1> metaData = new ExcelReadMetaData<>(inputStream, 2, 0, BasicExcelFormData_1.class, HashSet.class);

        // when
        Collection<BasicExcelFormData_1> testExcelObjects = excelDataProcessingService.readExcel(metaData);

        // then
        assertEquals(614_106, testExcelObjects.size());
    }

    @Test
    void 데이터가_잘못된_경우_에러_7번_행_컬럼이_비워져있음() throws Exception {
        // given
        ClassPathResource classPathResource = new ClassPathResource("fixture/invalid-form_1.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        ExcelReadMetaData<BasicExcelFormData_1> metaData = new ExcelReadMetaData<>(inputStream, 2, 0, BasicExcelFormData_1.class, ArrayList.class);

        // when
        assertThrows(InvalidExcelFormException.class, () -> excelDataProcessingService.readExcel(metaData));
    }

    @Test
    void 데이터가_잘못된_경우_에러_5번_행_컬럼이_비워져있음() throws Exception {
        // given
        ClassPathResource classPathResource = new ClassPathResource("fixture/invalid-form_2.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        ExcelReadMetaData<BasicExcelFormData_1> metaData = new ExcelReadMetaData<>(inputStream, 2, 0, BasicExcelFormData_1.class, ArrayList.class);

        // when
        assertThrows(InvalidExcelFormException.class, () -> excelDataProcessingService.readExcel(metaData));
    }

    @Test
    void 엑셀_파일_생성() {
        // given
        List<BasicExcelFormData_3> datas = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            datas.add(new BasicExcelFormData_3("test " + i, "test" + i, "test" + i, "test" + i));
        }
        ExcelWriteMetaData<BasicExcelFormData_3> metaData = new ExcelWriteMetaData<>("시트 타이틀", "시트 이름", datas, BasicExcelFormData_3.class);

        // when
        excelDataProcessingService.writeExcel(metaData, (bytes -> {
            ExcelReadMetaData<BasicExcelFormData_3> readMetaData = new ExcelReadMetaData<>(new ByteArrayInputStream(bytes), 2, 0, BasicExcelFormData_3.class, ArrayList.class);
            Collection<BasicExcelFormData_3> result = excelDataProcessingService.readExcel(readMetaData);
            assertEquals(100_000, result.size());
        }));
    }
}
