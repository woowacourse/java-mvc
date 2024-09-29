package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.controller.LoginViewController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerMappingTest {

    @DisplayName("존재하는 method와 uri에 대해 올바르게 컨트롤러를 반환")
    @Test
    void getHandler() {
        HandlerMapping handlerMapping = new ManualHandlerMapping();
        HttpServletRequest request = mock(HttpServletRequest.class);

        doReturn("/login/view")
                .when(request).getRequestURI();
        doReturn("GET")
                .when(request).getMethod();

        assertThat(handlerMapping.getHandler(request))
                .isExactlyInstanceOf(LoginViewController.class);
    }

    @DisplayName("존재하지 않는 method와 uri에 대해 null 반환")
    @Test
    void getHandler_Fail() {
        HandlerMapping handlerMapping = new ManualHandlerMapping();
        HttpServletRequest request = mock(HttpServletRequest.class);

        doReturn("/asdasd")
                .when(request).getRequestURI();
        doReturn("GET")
                .when(request).getMethod();

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
