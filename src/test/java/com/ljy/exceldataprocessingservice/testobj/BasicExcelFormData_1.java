package com.ljy.exceldataprocessingservice.testobj;

import com.ljy.exceldataprocessingservice.service.metadata.ExcelColum;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelData;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelDataValidationResult;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelEntity;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@ExcelEntity
public class BasicExcelFormData_1 implements ExcelData {
    @ExcelColum(value = "test1", order = 0)
    private String test1;

    @ExcelColum(value = "test2", order = 1)
    private String test2;

    @ExcelColum(value = "test3", order = 1)
    private String test3;

    @Override
    public boolean isEmpty() {
        return test1 == null &&
                test2 == null &&
                test3 == null;
    }

    @Override
    public ExcelDataValidationResult validate() {
        System.out.println(this);
        if(test1 == null || test1.isEmpty()) {
            return ExcelDataValidationResult.invalid("test1 항목을 입력해주세요.");
        }
        if(test2 == null || test2.isEmpty()) {
            return ExcelDataValidationResult.invalid("test2 항목을 입력해주세요.");
        }
        if(test3 == null || test3.isEmpty()) {
            return ExcelDataValidationResult.invalid("test3 항목을 입력해주세요.");
        }
        return ExcelDataValidationResult.valid();
    }

}
