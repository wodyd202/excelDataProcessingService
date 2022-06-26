package com.ljy.exceldataprocessingservice.service.metadata;

public interface ExcelData {
    boolean isEmpty();

    ExcelDataValidationResult validate();
}
