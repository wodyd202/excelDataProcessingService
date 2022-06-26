package com.ljy.exceldataprocessingservice.testobj;

import com.ljy.exceldataprocessingservice.service.metadata.ExcelColum;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelData;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelDataValidationResult;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelEntity;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@ExcelEntity
public class BasicExcelFormData_2 implements ExcelData {
    public enum TestEnum {
        TEST1, TEST2
    }

    @ExcelColum(headerName = "test1", order = 0)
    private TestEnum test1;

    @ExcelColum(headerName = "test2", order = 1)
    private Date test2;

    @ExcelColum(headerName = "test3", order = 2)
    private int test3;

    @ExcelColum(headerName = "test4", order = 3)
    private long test4;

    @ExcelColum(headerName = "test5", order = 4)
    private double test5;

    @Override
    public boolean isEmpty() {
        return test1 == null &&
                test2 == null &&
                test3 == 0 &&
                test4 == 0 &&
                test5 == 0.0;
    }

    @Override
    public ExcelDataValidationResult validate() {
        if(test1 == null) {
            return ExcelDataValidationResult.invalid("test1 항목을 입력해주세요.");
        }
        if(test2 == null) {
            return ExcelDataValidationResult.invalid("test2 항목을 입력해주세요.");
        }
        return ExcelDataValidationResult.valid();
    }

}
