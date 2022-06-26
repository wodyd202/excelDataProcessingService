package com.ljy.exceldataprocessingservice.testobj;

import com.ljy.exceldataprocessingservice.service.metadata.ExcelColum;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelData;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelEntity;
import lombok.Getter;

@Getter
@ExcelEntity
public class BasicExcelFormData_1 implements ExcelData {
    @ExcelColum(value = "test1", order = 0)
    private String test1;

    @ExcelColum(value = "test2", order = 1)
    private String test2;

    @ExcelColum(value = "test3", order = 1)
    private String test3;

    @Override
    public boolean isValid() {
        return test1 != null &&
                test2 != null &&
                test3 != null;
    }

    @Override
    public boolean isEmpty() {
        return test1 == null &&
                test2 == null &&
                test3 == null;
    }
}
