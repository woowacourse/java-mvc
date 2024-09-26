package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestManualHandlerMapping;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new HandlerMappingRegistry();
    }

    @DisplayName("@MVC 요청을 받으면 적합한 HandlerExecution을 반환한다.")
    @Test
    void getAnnotationHandler() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        registerHandlerMapping(new AnnotationHandlerMapping("samples"));

        assertThat(registry.getHandler(request).get()).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("Legacy MVC 요청을 받으면 적합한 Controller를 반환한다.")
    @Test
    void getControllerHandler() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");

        registerHandlerMapping(new TestManualHandlerMapping());

        assertThat(registry.getHandler(request).get()).isInstanceOf(Controller.class);
    }

    private void registerHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        registry.addHandlerMapping(handlerMapping);
    }
}
