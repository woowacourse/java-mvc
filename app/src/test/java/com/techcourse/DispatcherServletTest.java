package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new SampleHandlerMapping());
        dispatcherServlet.init();
    }

    static class SampleHandlerMapping implements HandlerMapping {

        private static final Map<String, String> controllers = new HashMap<>();

        @Override
        public void initialize() {
            controllers.put("/jazz", "jazz");
        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            String requestURI = request.getRequestURI();
            return controllers.get(requestURI);
        }
    }

    @DisplayName("요청을 처리할 수 있는 핸들러를 찾지 못하면 예외를 발생시킨다.")
    @Test
    void test() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/kargo");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessage("요청을 처리할 수 있는 핸들러가 존재하지 않습니다.");
    }

    @DisplayName("핸들러를 처리할 수 있는 어댑터를 찾지 못하면 예외를 발생시킨다.")
    @Test
    void test1() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/jazz");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessage("핸들러를 처리할 수 있는 어댑터가 존재하지 않습니다.");
    }
}
