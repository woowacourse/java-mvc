package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.controller.RegisterViewController;
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
        when(request.getRequestURI()).thenReturn("/register/view");

        final HandlerMapping handlerMapping = manualHandlerMapping;
        final Object handler = handlerMapping.getHandler(request);

        assertThat(handler).isInstanceOf(RegisterViewController.class);
        assertThat(handler).isInstanceOf(Controller.class);
    }

    @Test
    @DisplayName("로그인 요청은 수동 매핑에 등록하지 않는다")
    void doesNotMapLogin() {
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login");

        assertThat(manualHandlerMapping.getHandler(request)).isNull();
    }

    @Test
    @DisplayName("로그인 화면 요청은 수동 매핑에 등록하지 않는다")
    void doesNotMapLoginView() {
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login/view");

        assertThat(manualHandlerMapping.getHandler(request)).isNull();
    }

    @Test
    @DisplayName("로그아웃 요청은 수동 매핑에 등록하지 않는다")
    void doesNotMapLogout() {
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/logout");

        assertThat(manualHandlerMapping.getHandler(request)).isNull();
    }
}
