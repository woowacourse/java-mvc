package nextstep.configuration;

import air.context.ApplicationContext;
import air.context.ApplicationContextProvider;
import air.annotation.Bean;
import air.annotation.Configuration;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.mapping.AnnotationHandlerMapping;
import nextstep.mvc.mapping.HandlerMapping;
import nextstep.mvc.mapping.ManualHandlerMapping;

@Configuration
public class DispatcherServletConfiguration {

    private final ApplicationContext context = ApplicationContextProvider.getApplicationContext();

    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(getHandlerMapping(ManualHandlerMapping.class));
        dispatcherServlet.addHandlerMapping(getHandlerMapping(AnnotationHandlerMapping.class));
        return dispatcherServlet;
    }

    private HandlerMapping getHandlerMapping(Class<? extends HandlerMapping> clazz) {
        return context.findBeanByType(clazz);
    }
}
