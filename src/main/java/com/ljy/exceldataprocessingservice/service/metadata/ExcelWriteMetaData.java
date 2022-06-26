package com.ljy.exceldataprocessingservice.service.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
@ToString(exclude = "datas")
public class ExcelWriteMetaData<T> {
    private final String sheetTitle;
    private final String sheetName;
    private final Collection<T> datas;
    private final Class<T> dataType;
}