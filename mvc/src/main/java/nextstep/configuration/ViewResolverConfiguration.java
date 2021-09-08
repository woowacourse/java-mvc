package nextstep.configuration;

import air.annotation.Bean;
import air.annotation.Configuration;
import nextstep.mvc.resolver.JspViewResolver;

@Configuration
public class ViewResolverConfiguration {

    @Bean
    public JspViewResolver jspViewResolver() {
        return new JspViewResolver();
    }
}
