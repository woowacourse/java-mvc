package com.techcourse.air.mvc.configuration;

import com.techcourse.air.core.annotation.Bean;
import com.techcourse.air.core.annotation.Configuration;
import com.techcourse.air.mvc.core.mapping.AnnotationHandlerMapping;
import com.techcourse.air.mvc.core.mapping.ManualHandlerMapping;

@Configuration
public class HandlerMappingConfiguration {

    @Bean
    public ManualHandlerMapping manualHandlerMapping() {
        return new ManualHandlerMapping();
    }

    @Bean
    public AnnotationHandlerMapping annotationHandlerMapping() {
        return new AnnotationHandlerMapping();
    }
}
