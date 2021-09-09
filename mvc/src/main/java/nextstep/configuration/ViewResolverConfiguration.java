package nextstep.configuration;

import air.annotation.Bean;
import air.annotation.Configuration;
import nextstep.mvc.resolver.ResourceViewResolver;

@Configuration
public class ViewResolverConfiguration {

    @Bean
    public ResourceViewResolver resourceViewResolver() {
        return new ResourceViewResolver();
    }
}
