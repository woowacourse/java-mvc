package com.techcourse.air.mvc.configuration;

import com.techcourse.air.core.annotation.Bean;
import com.techcourse.air.core.annotation.Configuration;
import com.techcourse.air.core.context.ApplicationContext;
import com.techcourse.air.core.context.ApplicationContextProvider;
import com.techcourse.air.mvc.core.DispatcherServlet;
import com.techcourse.air.mvc.core.adapter.AnnotationHandlerAdapter;
import com.techcourse.air.mvc.core.adapter.HandlerAdapter;
import com.techcourse.air.mvc.core.adapter.SimpleControllerHandlerAdapter;
import com.techcourse.air.mvc.core.mapping.AnnotationHandlerMapping;
import com.techcourse.air.mvc.core.mapping.HandlerMapping;
import com.techcourse.air.mvc.core.mapping.ManualHandlerMapping;
import com.techcourse.air.mvc.core.resolver.ResourceViewResolver;
import com.techcourse.air.mvc.core.resolver.ViewResolver;

@Configuration
public class DispatcherServletConfiguration {

    private final ApplicationContext context = ApplicationContextProvider.getApplicationContext();

    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        dispatcherServlet.addHandlerMapping(getHandlerMapping(ManualHandlerMapping.class));
        dispatcherServlet.addHandlerMapping(getHandlerMapping(AnnotationHandlerMapping.class));

        dispatcherServlet.addHandlerAdapter(getHandlerAdapter(SimpleControllerHandlerAdapter.class));
        dispatcherServlet.addHandlerAdapter(getHandlerAdapter(AnnotationHandlerAdapter.class));

        dispatcherServlet.addViewResolver(getViewResolver(ResourceViewResolver.class));
        return dispatcherServlet;
    }

    private HandlerMapping getHandlerMapping(Class<? extends HandlerMapping> clazz) {
        return context.findBeanByType(clazz);
    }

    private HandlerAdapter getHandlerAdapter(Class<? extends HandlerAdapter> clazz) {
        return context.findBeanByType(clazz);
    }

    private ViewResolver getViewResolver(Class<? extends ViewResolver> clazz) {
        return context.findBeanByType(clazz);
    }
}
