package nextstep.configuration;

import air.ApplicationContext;
import air.annotation.Bean;
import air.annotation.Configuration;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.HandlerMapping;

@Configuration
public class DispatcherServletConfiguration {

    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        HandlerMapping handlerMapping = (HandlerMapping) ApplicationContext.getBean("manualHandlerMapping");
        dispatcherServlet.addHandlerMapping(handlerMapping);
        return dispatcherServlet;
    }
}
