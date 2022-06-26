package com.ljy.exceldataprocessingservice.service.exception;

public class InvalidExcelFormException extends RuntimeException {
    private final InvalidExcelType invalidExcelType;

    public InvalidExcelFormException(Exception e, InvalidExcelType invalidExcelType) {
        super(e);
        this.invalidExcelType = invalidExcelType;
    }

    public InvalidExcelFormException(String message, InvalidExcelType invalidExcelType) {
        super(message);
        this.invalidExcelType = invalidExcelType;
    }
}
