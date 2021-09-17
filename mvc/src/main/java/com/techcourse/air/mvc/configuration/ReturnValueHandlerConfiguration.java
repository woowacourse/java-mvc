package com.techcourse.air.mvc.configuration;

import com.techcourse.air.core.annotation.Bean;
import com.techcourse.air.core.annotation.Configuration;
import com.techcourse.air.mvc.core.returnvalue.JsonReturnValueHandler;
import com.techcourse.air.mvc.core.returnvalue.ViewReturnValueHandler;

@Configuration
public class ReturnValueHandlerConfiguration {

    @Bean
    public ViewReturnValueHandler viewReturnValueHandler() {
        return new ViewReturnValueHandler();
    }

    @Bean
    public JsonReturnValueHandler jsonReturnValueHandler() {
        return new JsonReturnValueHandler();
    }
}
