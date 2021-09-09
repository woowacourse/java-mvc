package com.techcourse.air.mvc.configuration;

import com.techcourse.air.core.annotation.Bean;
import com.techcourse.air.core.annotation.Configuration;
import com.techcourse.air.mvc.core.adapter.AnnotationHandlerAdapter;
import com.techcourse.air.mvc.core.adapter.SimpleControllerHandlerAdapter;

@Configuration
public class HandlerAdapterConfiguration {

    @Bean
    public SimpleControllerHandlerAdapter simpleControllerHandlerAdapter() {
        return new SimpleControllerHandlerAdapter();
    }

    @Bean
    public AnnotationHandlerAdapter annotationHandlerAdapter() {
        return new AnnotationHandlerAdapter();
    }
}
