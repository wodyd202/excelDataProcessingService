package com.ljy.exceldataprocessingservice.service.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class ExcelReadMetaData<T> {
    private final InputStream excelInputStream;
    private final int standardRow;
    private final int standardCol;
    private final Class<T> mapClassType;
    private final Class<? extends Collection> collectionType;
}
