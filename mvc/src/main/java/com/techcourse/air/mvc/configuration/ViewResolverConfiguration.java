package com.techcourse.air.mvc.configuration;

import com.techcourse.air.core.annotation.Bean;
import com.techcourse.air.core.annotation.Configuration;
import com.techcourse.air.mvc.core.resolver.ResourceViewResolver;

@Configuration
public class ViewResolverConfiguration {

    @Bean
    public ResourceViewResolver resourceViewResolver() {
        return new ResourceViewResolver();
    }
}
