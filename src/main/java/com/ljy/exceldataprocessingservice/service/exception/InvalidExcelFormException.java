package com.ljy.exceldataprocessingservice.service.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidExcelFormException extends RuntimeException {
    private final InvalidExcelType invalidExcelType;

    public InvalidExcelFormException(Exception e, InvalidExcelType invalidExcelType) {
        super(e);
        this.invalidExcelType = invalidExcelType;
        log.error("invalid excel form exception", e);
    }

    public InvalidExcelFormException(String message, InvalidExcelType invalidExcelType) {
        super(message);
        this.invalidExcelType = invalidExcelType;
    }
}
