package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HandlerMappingRegistryTest {

    HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

    @BeforeEach
    void setUp() {
        handlerMappingRegistry.addHandlerMapping(new ManualHandlerMapping());
        handlerMappingRegistry.init();
    }

    @DisplayName("URI에 해당하는 컨트롤러 인터페이스 기반 핸들러를 반환한다.")
    @Test
    void getHandlerWithController() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login");

        Object handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isInstanceOf(LoginController.class);
    }
}
