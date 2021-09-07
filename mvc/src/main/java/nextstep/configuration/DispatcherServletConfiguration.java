package nextstep.configuration;

import air.ApplicationContext;
import air.annotation.Bean;
import air.annotation.Configuration;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.mapping.ManualHandlerMapping;
import nextstep.mvc.mapping.RequestMappingHandlerMapping;

@Configuration
public class DispatcherServletConfiguration {

    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(getHandlerMapping(ManualHandlerMapping.class));
        dispatcherServlet.addHandlerMapping(getHandlerMapping(RequestMappingHandlerMapping.class));
        return dispatcherServlet;
    }

    private HandlerMapping getHandlerMapping(Class<? extends HandlerMapping> clazz) {
        return ApplicationContext.findBeanByType(clazz);
    }
}
