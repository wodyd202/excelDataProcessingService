package com.ljy.exceldataprocessingservice.service.metadata;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExcelDataValidationResult {
    private final String message;
    private final boolean valid;

    public static ExcelDataValidationResult invalid(String message) {
        return new ExcelDataValidationResult(message, false);
    }

    public static ExcelDataValidationResult valid() {
        return new ExcelDataValidationResult(null, true);
    }

    public boolean hasError() {
        return !this.valid;
    }

    public String getMessage() {
        return this.message;
    }
}
