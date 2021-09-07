package nextstep.configuration;

import air.annotation.Bean;
import air.annotation.Configuration;
import nextstep.mvc.mapping.ManualHandlerMapping;

@Configuration
public class HandlerMappingConfiguration {

    @Bean
    public ManualHandlerMapping manualHandlerMapping() {
        return new ManualHandlerMapping();
    }

}
