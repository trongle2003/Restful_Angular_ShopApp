package com.project.shopapp.ultil.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // can thiệp vào quá trình chạy
@Target(ElementType.METHOD)
public @interface ApiMessage {
    String value();
}
