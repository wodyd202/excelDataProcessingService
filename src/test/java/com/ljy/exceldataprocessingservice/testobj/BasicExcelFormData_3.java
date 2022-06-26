package com.ljy.exceldataprocessingservice.testobj;

import com.ljy.exceldataprocessingservice.service.metadata.ExcelColum;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelData;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelDataValidationResult;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExcelEntity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicExcelFormData_3 implements ExcelData {

    @ExcelColum(order = 0, headerName = "test1z")
    private String test1;

    @ExcelColum(order = 1, headerName = "test2x")
    private String test2;

    @ExcelColum(order = 2, headerName = "test3c")
    private String test3;

    @ExcelColum(order = 3, headerName = "test4v")
    private String test4;

    public BasicExcelFormData_3(String test1, String test2, String test3, String test4) {
        this.test1 = test1;
        this.test2 = test2;
        this.test3 = test3;
        this.test4 = test4;
    }

    @Override
    public boolean isEmpty() {
        return test1 == null &&
                test2 == null &&
                test3 == null &&
                test4 == null;
    }

    @Override
    public ExcelDataValidationResult validate() {
        return ExcelDataValidationResult.valid();
    }
}
