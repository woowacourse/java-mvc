package com.interface21.webmvc.servlet.mvc;

import java.lang.reflect.Method;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.AnnotationTestController;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        this.dispatcherServlet = new DispatcherServlet("samples");
        dispatcherServlet.init();
    }

    @DisplayName("올바른 요청에 대해 handler를 찾을 수 있다.")
    @Test
    void findHandler() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Method method = dispatcherServlet.getClass().getDeclaredMethod("getHandler", HttpServletRequest.class);
        method.setAccessible(true);

        // when
        Object handler = method.invoke(dispatcherServlet, request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("요청을 처리할 handler를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void cannotFindHandler() throws NoSuchMethodException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/none");
        when(request.getMethod()).thenReturn("HEAD");

        Method method = dispatcherServlet.getClass().getDeclaredMethod("getHandler", HttpServletRequest.class);
        method.setAccessible(true);

        // when & then
        assertThatThrownBy(() -> method.invoke(dispatcherServlet, request))
                .rootCause()
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("요청을 처리할 수 없습니다.");
    }

    @DisplayName("올바른 handler에 대해 지원하는 adapter를 찾을 수 있다.")
    @Test
    void findHandlerAdapter() throws Exception {
        // given
        Method method = dispatcherServlet.getClass().getDeclaredMethod("getHandlerAdapter", Object.class);
        method.setAccessible(true);

        // when
        Object handlerAdapter = method.invoke(dispatcherServlet, createHandlerExecution());

        // then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    private static HandlerExecution createHandlerExecution() throws NoSuchMethodException {
        AnnotationTestController controller = new AnnotationTestController();
        Method method = controller.getClass()
                .getMethod("save", HttpServletRequest.class, HttpServletResponse.class);
        return new HandlerExecution(method, controller);
    }

    @DisplayName("handler를 지원하는 adapter를 찾지 못하면 예외를 발생시킨다.")
    @Test
    void cannotFindHandlerAdapter() throws NoSuchMethodException {
        // given
        Object invalidObject = new Object();

        Method method = dispatcherServlet.getClass().getDeclaredMethod("getHandlerAdapter", Object.class);
        method.setAccessible(true);

        // when & then
        assertThatThrownBy(() -> method.invoke(dispatcherServlet, invalidObject))
                .rootCause()
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("요청을 처리할 수 없습니다.");
    }
}

