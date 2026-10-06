package com.techcourse;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("수동 요청 매핑")
class ManualHandlerMappingTest {

    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new ManualHandlerMapping();
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("HandlerMapping 인터페이스로 요청을 받아 기존 컨트롤러를 찾는다")
    void findsLegacyControllerThroughInterface() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login");

        // when
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(LoginController.class);
    }

    @Test
    @DisplayName("어노테이션 방식으로 옮긴 회원가입 경로는 수동 매핑에서 찾지 않는다")
    void doesNotMapMigratedRegistrationPath() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register");

        // when
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isNull();
    }

    @Test
    @DisplayName("기존 회원가입 화면 경로는 호환을 위해 유지한다")
    void preservesLegacyRegistrationViewPath() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register/view");

        // when
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(RegisterViewController.class);
    }

    @Test
    @DisplayName("등록되지 않은 경로는 실행 대상이 없다")
    void returnsNullForUnknownPath() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/unknown");

        // when
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isNull();
    }
}
