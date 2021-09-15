package nextstep.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.mapping.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static nextstep.mvc.mapping.AnnotationHandlerMappingTest.BASE_PACKAGE;
import static org.mockito.Mockito.*;

class DispatcherServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private DispatcherServlet dispatcherServlet;
    private MockRequestDispatcher mockRequestDispatcher;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mockRequestDispatcher = mock(MockRequestDispatcher.class);

        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(BASE_PACKAGE));
        dispatcherServlet.init();
    }

    @Test
    void service() throws ServletException, IOException {
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test2");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("index.jsp")).thenReturn(mockRequestDispatcher);

        dispatcherServlet.service(request, response);

        verify(mockRequestDispatcher).forward(request, response);
    }

    @Test
    void notFound() throws ServletException {
        when(request.getRequestURI()).thenReturn("/notfound");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("index.jsp")).thenReturn(mockRequestDispatcher);

        dispatcherServlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    class MockRequestDispatcher implements RequestDispatcher {
        @Override
        public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        }

        @Override
        public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        }
    }
}