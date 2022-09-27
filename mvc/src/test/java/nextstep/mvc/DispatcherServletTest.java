package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import nextstep.mvc.controller.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.adapter.AnnotationHandlerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setup() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
    }

    @DisplayName("디스패처 서블릿을 초기화하지 않을 시 컨트롤러를 가져올 수 없다.")
    @Test
    void service_Exception() {
        final HttpServletRequest request = spy(HttpServletRequest.class);
        final HttpServletResponse response = spy(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class);
    }

    @DisplayName("디스패처 서블릿이 요청을 처리한다.")
    @Test
    void service() throws ServletException, IOException {
        dispatcherServlet.init();

        final HttpServletRequest request = spy(HttpServletRequest.class);
        final HttpServletResponse response = spy(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("123");
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher, times(1)).forward(any(), any());
    }
}
