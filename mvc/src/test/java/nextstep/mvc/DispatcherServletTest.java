package nextstep.mvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    @DisplayName("DispatcherServlet은 각 Registry를 주입받고, service 메서드를 통해 로직을 실행한다.")
    void service() throws ServletException, IOException {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.addAdapterMapping(new AnnotationHandlerAdapter());
        servlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        servlet.init();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute(any())).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher(any())).thenReturn(mock(RequestDispatcher.class));

        // when
        servlet.service(request, response);

        // then
        verify(request.getRequestDispatcher(any())).forward(any(), any());
    }
}
