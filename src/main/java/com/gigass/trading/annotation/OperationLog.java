package com.gigass.trading.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    String value() default "";
    String description() default "";
}