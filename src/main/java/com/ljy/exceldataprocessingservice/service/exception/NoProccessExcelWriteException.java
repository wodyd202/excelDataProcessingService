package com.ljy.exceldataprocessingservice.service.exception;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class NoProccessExcelWriteException extends RuntimeException {
    public NoProccessExcelWriteException(IOException e) {
        super("엑셀 파일을 쓰는 도중 문제가 발생했습니다.");
        log.error("excel write exception", e);
    }

    public NoProccessExcelWriteException(IllegalAccessException e) {
        super("데이터 타입이 올바르지 않습니다. 타입을 다시 확인해주세요.");
        log.error("excel write exception", e);
    }
}
