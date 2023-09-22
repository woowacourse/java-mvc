package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

import static org.mockito.Mockito.*;

class DispatcherServletTest {

    @Test
    void service_TestController() throws Exception {
        // given
        final var handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        final var handlerAdapter = new AnnotationHandlerAdapter();
        final var dispatcherServlet = new DispatcherServlet(handlerMapping, handlerAdapter);

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestDispatcher("")).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(any(), any());

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(request, times(1)).getRequestDispatcher("");
        verify(requestDispatcher, times(1)).forward(request, response);
    }
}
