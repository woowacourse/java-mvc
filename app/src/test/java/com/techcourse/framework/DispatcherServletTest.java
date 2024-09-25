package com.techcourse.framework;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.tobe.NoMatchedHandlerException;

class DispatcherServletTest {

    private final DispatcherServlet dispatcherServlet = new DispatcherServlet();

    @BeforeEach
    void init() {
        dispatcherServlet.init();
    }

    @Nested
    @DisplayName("요청 처리")
    class Service {

        @Test
        @DisplayName("어노테이션 기반 컨트롤러 요청을 처리할 수 있다.")
        void serviceWithAnnotation() {
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);
            final var requestDispatcher = mock(RequestDispatcher.class);

            when(request.getRequestURI()).thenReturn("/register/view");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

            assertThatCode(() -> dispatcherServlet.service(request, response))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("인터페이스 기반 컨트롤러 요청을 처리할 수 있다.")
        void serviceWithInterface() {
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);
            final var requestDispatcher = mock(RequestDispatcher.class);

            when(request.getRequestURI()).thenReturn("/");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

            assertThatCode(() -> dispatcherServlet.service(request, response))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("두 컨트롤러 모두 처리할 수 없는 요청에는 예외가 발생한다.")
        void noMatchedHandlerTest() {
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);
            final var requestDispatcher = mock(RequestDispatcher.class);

            when(request.getRequestURI()).thenReturn("/not-exist-request");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

            assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                    .isInstanceOf(ServletException.class)
                    .hasCauseExactlyInstanceOf(NoMatchedHandlerException.class);
        }
    }
}
