package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @DisplayName("요청을 처리할 수 있는 HandlerMapping을 반환한다.")
    @Test
    void getHandler() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/login");

        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry(
                List.of(new AnnotationHandlerMapping())
        );

        Object handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isInstanceOf(LoginController.class);
    }
}
