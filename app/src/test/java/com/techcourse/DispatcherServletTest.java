package com.techcourse;

import java.lang.reflect.Method;
import java.util.stream.Stream;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import com.interface21.webmvc.servlet.mvc.asis.Controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    private static Stream<Arguments> handlerProvider() {
        return Stream.of(
                Arguments.of("/login", "GET", Controller.class)
        );
    }

    @BeforeEach
    void setUp() {
        this.dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
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

