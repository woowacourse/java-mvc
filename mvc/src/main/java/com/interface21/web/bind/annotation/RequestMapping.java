package com.interface21.web.bind.annotation;

import static com.interface21.web.bind.annotation.RequestMethod.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value() default "";

    RequestMethod[] method() default {
            GET,
            POST,
            PUT,
            DELETE,
            OPTIONS,
            HEAD,
            PATCH,
            TRACE
    };
}
