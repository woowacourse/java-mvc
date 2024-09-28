package com.interface21.webmvc.servlet.mvc.tobe;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerMappingRegisterTest {

    @Test
    @DisplayName("HandlerMapping을 등록할 수 있다.")
    void addHandlerMapping() {
        HandlerMappingRegister register = new HandlerMappingRegister();

        register.addHandlerMapping(new AnnotationHandlerMapping());

        assertThat(register).extracting("handlerMappings", InstanceOfAssertFactories.list(HandlerMapping.class))
                .hasSize(1);
    }
}
