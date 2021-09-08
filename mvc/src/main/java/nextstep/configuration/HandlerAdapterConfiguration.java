package nextstep.configuration;

import air.annotation.Bean;
import air.annotation.Configuration;
import nextstep.mvc.adapter.AnnotationHandlerAdapter;
import nextstep.mvc.adapter.SimpleControllerHandlerAdapter;

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
