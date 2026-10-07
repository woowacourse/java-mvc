package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    @Test
    @DisplayName("공통 매핑 계약으로 요청 URI에 등록된 기존 컨트롤러를 찾는다")
    void findsControllerForRequest() {
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login");

        final HandlerMapping handlerMapping = manualHandlerMapping;
        final Object handler = handlerMapping.getHandler(request);

        assertThat(handler).isInstanceOf(LoginController.class);
        assertThat(handler).isInstanceOf(Controller.class);
    }
}
