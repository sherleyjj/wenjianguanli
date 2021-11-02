package com.example.common.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface JBasicLog {
    String msg() default "JLog:";
}
