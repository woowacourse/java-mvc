package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegisterTest {

    private HandlerMappingRegister register;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        register = new HandlerMappingRegister();
        request = mock(HttpServletRequest.class);
    }

    @Test
    @DisplayName("HandlerMapping을 등록할 수 있다.")
    void addHandlerMapping() {
        register.addHandlerMapping(new AnnotationHandlerMapping());

        assertThat(register).extracting("handlerMappings", InstanceOfAssertFactories.list(HandlerMapping.class))
                .hasSize(1);
    }

    @Test
    @DisplayName("등록된 HandlerMapping을 통해 handler를 찾을 수 있다.")
    void getHandler() {
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        register.addHandlerMapping(handlerMapping);

        assertThat(register.getHandler(request)).isInstanceOf(HandlerExecution.class);
    }

    @Test
    @DisplayName("등록된 HandlerMapping을 통해 handler를 찾을 수 없다면 예외가 발생한다.")
    void invalidRequest() {
        when(request.getRequestURI()).thenReturn("/invalid-test");
        when(request.getMethod()).thenReturn("GET");

        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        register.addHandlerMapping(handlerMapping);

        assertThatThrownBy(() -> register.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
