package com.techcourse;

import com.techcourse.controllerv1.LoginControllerV1;
import com.techcourse.controllerv1.LoginViewControllerV1;
import com.techcourse.controllerv1.LogoutControllerV1;
import com.techcourse.controllerv1.RegisterControllerV1;
import com.techcourse.controllerv1.RegisterViewControllerV1;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.when;

class ManualHandlerMappingTest {

    private static final ManualHandlerMapping MANUAL_HANDLER_MAPPING = new ManualHandlerMapping();

    @Test
    void ForwardController를_반환할_수_있다() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        // when
        final Object actual = MANUAL_HANDLER_MAPPING.getHandlerExecution(request);

        // then
        assertThat(actual).isInstanceOf(ForwardController.class);
    }

    @Test
    void LoginControllerV1를_반환할_수_있다() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/v1/login");
        when(request.getMethod()).thenReturn("POST");

        // when
        final Object actual = MANUAL_HANDLER_MAPPING.getHandlerExecution(request);

        // then
        assertThat(actual).isInstanceOf(LoginControllerV1.class);
    }

    @Test
    void LoginViewControllerV1를_반환할_수_있다() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/v1/login/view");
        when(request.getMethod()).thenReturn("GET");

        // when
        final Object actual = MANUAL_HANDLER_MAPPING.getHandlerExecution(request);

        // then
        assertThat(actual).isInstanceOf(LoginViewControllerV1.class);
    }

    @Test
    void LogoutControllerV1를_반환할_수_있다() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/v1/logout");
        when(request.getMethod()).thenReturn("GET");

        // when
        final Object actual = MANUAL_HANDLER_MAPPING.getHandlerExecution(request);

        // then
        assertThat(actual).isInstanceOf(LogoutControllerV1.class);
    }

    @Test
    void RegisterViewControllerV1를_반환할_수_있다() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/v1/register/view");
        when(request.getMethod()).thenReturn("GET");

        // when
        final Object actual = MANUAL_HANDLER_MAPPING.getHandlerExecution(request);

        // then
        assertThat(actual).isInstanceOf(RegisterViewControllerV1.class);
    }

    @Test
    void RegisterControllerV1를_반환할_수_있다() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/v1/register");
        when(request.getMethod()).thenReturn("POST");

        // when
        final Object actual = MANUAL_HANDLER_MAPPING.getHandlerExecution(request);

        // then
        assertThat(actual).isInstanceOf(RegisterControllerV1.class);
    }
}
