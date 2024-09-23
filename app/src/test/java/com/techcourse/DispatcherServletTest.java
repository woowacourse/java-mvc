package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("요청에 해당하는 핸들러를 찾을 수 없으면 예외가 발생한다.")
    @Test
    void throwsWhenHandlerNotFound() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/not-found");

        // when & then
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("요청에 해당하는 핸들러를 찾을 수 없습니다.");
    }

    @DisplayName("요청에 해당하는 핸들러 어댑터를 찾을 수 없으면 예외가 발생한다.")
    @Test
    void throwsWhenHandlerAdapterNotFound() {
        // given
        registerFakeHandlerMapping();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/test");

        // when & then
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("요청에 해당하는 핸들러 어댑터를 찾을 수 없습니다.");
    }

    private void registerFakeHandlerMapping() {
        dispatcherServlet.addHandlerMapping(new HandlerMapping() {
            @Override
            public void initialize() {
            }

            @Override
            public Object getHandler(HttpServletRequest request) {
                return "test";
            }
        });
    }
}
