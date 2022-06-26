package com.ljy.exceldataprocessingservice.service.exception;

import java.io.IOException;

public class InvalidExcelFormException extends RuntimeException {
    public InvalidExcelFormException(Exception e) {
        super(e);
    }

    public InvalidExcelFormException(String message) {
        super(message);
    }
}
