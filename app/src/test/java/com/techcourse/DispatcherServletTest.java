package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("디스패처 서블릿")
class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        this.dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("디스패처 서블릿은 처리 가능한 어댑터를 찾지 못할 경우 예외를 반환한다.")
    @Test
    void findNotMatchHandlerAdapter() throws NoSuchMethodException {
        // given
        Object invalidObject = new Object();

        // when
        Method method = dispatcherServlet.getClass().getDeclaredMethod("findHandlerAdapter", Object.class);
        method.setAccessible(true);

        // then
        assertThatThrownBy(() -> method.invoke(dispatcherServlet, invalidObject))
                .rootCause()
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("처리할 수 없는 요청입니다.");
    }

    @DisplayName("디스패처 서블릿은 어노테이션 타입의 핸들러를 찾는다.")
    @Test
    void findAnnotationHandler() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");

        // when
        Method method = dispatcherServlet.getClass().getDeclaredMethod("findHandler", HttpServletRequest.class);
        method.setAccessible(true);
        Object actual = method.invoke(dispatcherServlet, request);

        // then
        assertThat(actual).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("디스패처 서블릿은 컨트롤러 타입의 핸들러를 찾는다.")
    @Test
    void findControllerHandler() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("GET");

        // when
        Method method = dispatcherServlet.getClass().getDeclaredMethod("findHandler", HttpServletRequest.class);
        method.setAccessible(true);
        Object actual = method.invoke(dispatcherServlet, request);

        // then
        assertThat(actual).isInstanceOf(Controller.class);
    }
}
