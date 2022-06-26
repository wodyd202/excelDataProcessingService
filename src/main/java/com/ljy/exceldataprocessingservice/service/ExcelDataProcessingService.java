package com.ljy.exceldataprocessingservice.service;

import com.ljy.exceldataprocessingservice.service.metadata.ExcelReadMetaData;
import com.ljy.exceldataprocessingservice.service.metadata.ExcelWriteMetaData;

import java.util.Collection;

public interface ExcelDataProcessingService {
    <T> Collection<T> readExcel(ExcelReadMetaData<T> metaData);
    <T> byte[] writeExcel(ExcelWriteMetaData<T> metaData);
}
