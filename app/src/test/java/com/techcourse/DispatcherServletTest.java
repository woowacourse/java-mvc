package com.techcourse;

import java.lang.reflect.Method;
import java.util.stream.Stream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.RegisterController;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    private static Stream<Arguments> handlerProvider() {
        return Stream.of(
                Arguments.of("/login", "POST", Controller.class),
                Arguments.of("/register", "POST", HandlerExecution.class)
        );
    }

    private static Stream<Arguments> handlerAdapterProvider() throws NoSuchMethodException {
        return Stream.of(
                Arguments.of(new LoginController(), ControllerHandlerAdapter.class),
                Arguments.of(createHandlerExecution(), AnnotationHandlerAdapter.class)
        );
    }

    private static HandlerExecution createHandlerExecution() throws NoSuchMethodException {
        RegisterController controller = new RegisterController();
        Method method = controller.getClass()
                .getMethod("save", HttpServletRequest.class, HttpServletResponse.class);
        return new HandlerExecution(method, controller);
    }

    @BeforeEach
    void setUp() {
        this.dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("올바른 요청에 대해 handler를 찾을 수 있다.")
    @ParameterizedTest
    @MethodSource("handlerProvider")
    void findHandler(String requestUri, String httpMethod, Class<?> expectedHandlerClass) throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(requestUri);
        when(request.getMethod()).thenReturn(httpMethod);

        Method method = dispatcherServlet.getClass().getDeclaredMethod("findHandler", HttpServletRequest.class);
        method.setAccessible(true);

        // when
        Object handler = method.invoke(dispatcherServlet, request);

        // then
        assertThat(handler).isInstanceOf(expectedHandlerClass);
    }

    @DisplayName("요청을 처리할 handler를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void cannotFindHandler() throws NoSuchMethodException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/none");
        when(request.getMethod()).thenReturn("HEAD");

        Method method = dispatcherServlet.getClass().getDeclaredMethod("findHandler", HttpServletRequest.class);
        method.setAccessible(true);

        // when & then
        assertThatThrownBy(() -> method.invoke(dispatcherServlet, request))
                .rootCause()
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("요청을 처리할 수 없습니다.");
    }

    @DisplayName("올바른 handler에 대해 지원하는 adapter를 찾을 수 있다.")
    @ParameterizedTest
    @MethodSource("handlerAdapterProvider")
    void findHandlerAdapter(Object handler, Class<?> expectedAdapterClass) throws Exception {
        // given
        Method method = dispatcherServlet.getClass().getDeclaredMethod("findHandlerAdapter", Object.class);
        method.setAccessible(true);

        // when
        Object handlerAdapter = method.invoke(dispatcherServlet, handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(expectedAdapterClass);
    }

    @DisplayName("handler를 지원하는 adapter를 찾지 못하면 예외를 발생시킨다.")
    @Test
    void cannotFindHandlerAdapter() throws NoSuchMethodException {
        // given
        Object invalidObject = new Object();

        Method method = dispatcherServlet.getClass().getDeclaredMethod("findHandlerAdapter", Object.class);
        method.setAccessible(true);

        // when & then
        assertThatThrownBy(() -> method.invoke(dispatcherServlet, invalidObject))
                .rootCause()
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("요청을 처리할 수 없습니다.");
    }
}

