package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.initialize();
    }

    @DisplayName("@MVC로 구현된 요청이 들어올 경우 AnnotationHandlerMapping가 해당 요청을 처리한다.")
    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST"})
    void should_mapAnnotationHandlerMapping_when_NewMvc(String requestMethod) {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn(requestMethod);

        // when & then
        assertThat(handlerMappingRegistry.getHandler(request)).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("Legacy MVC로 구현된 요청이 들어올 경우 ManualHandlerMapping가 해당 요청을 처리한다.")
    @ParameterizedTest
    @ValueSource(strings = {"/", "/login", "/login/view", "/logout"})
    void should_mapManualHandlerMapping_when_LegacyMvc(String requestUri) {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(requestUri);

        // when & then
        assertThat(handlerMappingRegistry.getHandler(request)).isInstanceOf(Controller.class);
    }
}
