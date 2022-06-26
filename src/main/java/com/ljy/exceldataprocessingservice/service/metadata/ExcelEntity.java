package com.ljy.exceldataprocessingservice.service.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelEntity {
    int rowCacheSize() default 100;
    int bufferSize() default 4096;
}
