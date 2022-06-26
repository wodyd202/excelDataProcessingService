package com.ljy.exceldataprocessingservice.service.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
@ToString(exclude = "datas")
public class ExcelWriteMetaData<T> {
    private final ExcelSheetMetaData sheetMetaData;
    private final ExcelTitleMetaData titleMetaData;
    private final ExcelHeaderMetaData headerMetaData;

    private final Collection<T> datas;
    private final Class<T> dataType;
}
