package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;

class ManualHandlerMappingAdapterTest {

    @Test
    void login_컨트롤러를_찾는다() {
        final HandlerMapping handlerMapping = new ManualHandlerMappingAdapter(new ManualHandlerMapping());
        handlerMapping.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/login");
        final Object handler = handlerMapping.getHandler(request);

        assertThat(handler).isInstanceOf(LoginController.class);
    }

    @Test
    void login_view_컨트롤러를_찾는다() {
        final HandlerMapping handlerMapping = new ManualHandlerMappingAdapter(new ManualHandlerMapping());
        handlerMapping.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/login/view");
        final Object handler = handlerMapping.getHandler(request);

        assertThat(handler).isInstanceOf(LoginViewController.class);
    }

    @Test
    void logout_컨트롤러를_찾는다() {
        final HandlerMapping handlerMapping = new ManualHandlerMappingAdapter(new ManualHandlerMapping());
        handlerMapping.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/logout");
        final Object handler = handlerMapping.getHandler(request);

        assertThat(handler).isInstanceOf(LogoutController.class);
    }

    @Test
    void 기본_컨트롤러를_찾는다() {
        final HandlerMapping handlerMapping = new ManualHandlerMappingAdapter(new ManualHandlerMapping());
        handlerMapping.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/");
        final Object handler = handlerMapping.getHandler(request);

        assertThat(handler).isInstanceOf(ForwardController.class);
    }
}
