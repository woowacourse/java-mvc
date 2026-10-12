package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    @Test
    @DisplayName("홈 요청은 수동 매핑에 등록하지 않는다")
    void doesNotMapHome() {
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");

        assertThat(manualHandlerMapping.getHandler(request)).isNull();
    }

    @Test
    @DisplayName("회원가입 화면 요청은 수동 매핑에 등록하지 않는다")
    void doesNotMapRegisterView() {
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register/view");

        assertThat(manualHandlerMapping.getHandler(request)).isNull();
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
