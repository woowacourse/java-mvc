package com.interface21.context.stereotype;

import com.interface21.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String value() default "";

    RequestMethod method() default RequestMethod.GET;

    String path() default "";
}
