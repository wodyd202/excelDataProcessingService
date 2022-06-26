package com.ljy.exceldataprocessingservice.service;

import com.ljy.exceldataprocessingservice.service.exception.InvalidExcelFormException;
import com.ljy.exceldataprocessingservice.service.metadata.*;
import com.ljy.exceldataprocessingservice.testobj.BasicExcelFormData_1;
import com.ljy.exceldataprocessingservice.testobj.BasicExcelFormData_2;
import com.ljy.exceldataprocessingservice.testobj.BasicExcelFormData_3;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExcelDataProcessingServiceTest {
    private SimpleExcelDataProcessingService excelDataProcessingService = new SimpleExcelDataProcessingService();

    @Test
    void 엑셀_내의_데이터_조회_1() throws IOException {
        // given
        ClassPathResource classPathResource = new ClassPathResource("fixture/data-type-form.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        ExcelReadMetaData<BasicExcelFormData_2> metaData = ExcelReadMetaData.basic(inputStream, BasicExcelFormData_2.class, ArrayList.class);

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
        ExcelReadMetaData<BasicExcelFormData_1> metaData = ExcelReadMetaData.basic(inputStream, BasicExcelFormData_1.class, ArrayList.class);

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
        ExcelReadMetaData<BasicExcelFormData_1> metaData = ExcelReadMetaData.basic(inputStream, BasicExcelFormData_1.class, HashSet.class);

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
        ExcelReadMetaData<BasicExcelFormData_1> metaData = ExcelReadMetaData.basic(inputStream, BasicExcelFormData_1.class, ArrayList.class);

        // when
        assertThrows(InvalidExcelFormException.class, () -> excelDataProcessingService.readExcel(metaData));
    }

    @Test
    void 데이터가_잘못된_경우_에러_5번_행_컬럼이_비워져있음() throws Exception {
        // given
        ClassPathResource classPathResource = new ClassPathResource("fixture/invalid-form_2.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        ExcelReadMetaData<BasicExcelFormData_1> metaData = ExcelReadMetaData.basic(inputStream, BasicExcelFormData_1.class, ArrayList.class);

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
        ExcelWriteMetaData<BasicExcelFormData_3> metaData = ExcelWriteMetaData.basic("엑셀 시트 이름", "엑셀 타이틀 이름", datas, BasicExcelFormData_3.class);

        // when
        byte[] bytes = excelDataProcessingService.writeExcel(metaData);
        ExcelReadMetaData<BasicExcelFormData_3> readMetaData = ExcelReadMetaData.basic(new ByteArrayInputStream(bytes), BasicExcelFormData_3.class, ArrayList.class);
        Collection<BasicExcelFormData_3> result = excelDataProcessingService.readExcel(readMetaData);

        // then
        assertEquals(100_000, result.size());
    }

}
