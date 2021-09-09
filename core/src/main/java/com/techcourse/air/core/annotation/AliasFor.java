package com.techcourse.air.core.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AliasFor {
    Class<? extends Annotation> annotation() default Annotation.class;
}
