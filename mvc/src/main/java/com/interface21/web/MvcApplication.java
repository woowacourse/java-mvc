package com.interface21.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MvcApplication {

    /**
     * 스캔할 베이스 패키지들 (선택사항)
     */
    String[] scanBasePackages() default {};

    /**
     * 스캔할 베이스 패키지 클래스들 (선택사항)
     */
    Class<?>[] scanBasePackageClasses() default {};
}
