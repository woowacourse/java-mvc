package com.interface21.web.bind.annotation;

import static com.interface21.web.bind.annotation.RequestMethod.DELETE;
import static com.interface21.web.bind.annotation.RequestMethod.GET;
import static com.interface21.web.bind.annotation.RequestMethod.HEAD;
import static com.interface21.web.bind.annotation.RequestMethod.OPTIONS;
import static com.interface21.web.bind.annotation.RequestMethod.PATCH;
import static com.interface21.web.bind.annotation.RequestMethod.POST;
import static com.interface21.web.bind.annotation.RequestMethod.PUT;
import static com.interface21.web.bind.annotation.RequestMethod.TRACE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    String value() default "";

    RequestMethod[] method() default {GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE};
}
