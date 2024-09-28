package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.NotFoundViewController;

public class HandlerManagerTest {

    private HandlerManager handlerManager;

    @BeforeEach
    void setUp() {
        handlerManager = new HandlerManager(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping("samples")
        );
        handlerManager.initialize();
    }

    @DisplayName("요청에 맞는 핸들러를 가지고 있는 핸들러매핑이 없다면 NotFoundController를 반환한다.")
    @Test
    void notFoundRender() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/none");
        when(request.getMethod()).thenReturn("GET");

        final Object handler = handlerManager.findHandler(request);
        assertThat(handler).isInstanceOf(NotFoundViewController.class);
    }

    @DisplayName("요청에 맞는 HandlerExecution 을 반환한다.")
    @Test
    void findHandlerExecution() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final Object handler = handlerManager.findHandler(request);
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("요청에 맞는 Controller 를 반환한다.")
    @Test
    void findController() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");

        final Object handler = handlerManager.findHandler(request);
        assertThat(handler).isInstanceOf(LoginViewController.class);
    }
}
