package com.ljy.exceldataprocessingservice.service.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
@ToString(exclude = "datas")
public class ExcelWriteMetaData<T> {
    private final ExcelSheetMetaData sheetMetaData;
    private final ExcelTitleMetaData titleMetaData;
    private final ExcelHeaderMetaData headerMetaData;

    private final Collection<T> datas;
    private final Class<T> dataType;

    public static <T> ExcelWriteMetaData basic(String sheetName, String titleName, Collection<T> datas, Class<T> dataType) {
        ExcelSheetMetaData sheetMetaData = new ExcelSheetMetaData(sheetName);
        ExcelTitleMetaData titleMetaData = new ExcelTitleMetaData(titleName, 0);
        ExcelHeaderMetaData headerMetaData = new ExcelHeaderMetaData(1);
        return new ExcelWriteMetaData(sheetMetaData, titleMetaData, headerMetaData, datas, dataType);
    }
}
