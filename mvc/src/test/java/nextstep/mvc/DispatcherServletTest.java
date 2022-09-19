package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestManualHandlerMapping;

class DispatcherServletTest {

    private DispatcherServlet sut;

    @BeforeEach
    void setUp() {
        sut = new DispatcherServlet();
        sut.addHandlerMapping(new TestManualHandlerMapping());
        sut.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        sut.init();
    }

    @Test
    void forwardIndexView() throws ServletException, IOException {
        final HttpServletRequest request = createMockedRequest("/index", "GET");
        final HttpServletResponse response = createMockedResponse();

        sut.service(request, response);

        verify(request.getRequestDispatcher("/index.jsp")).forward(request, response);
    }

    @Test
    void redirectIndexView() throws ServletException, IOException {
        final HttpServletRequest request = createMockedRequest("/redirect-index", "GET");
        final HttpServletResponse response = createMockedResponse();

        sut.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void getTestViewWithModel() throws ServletException, IOException {
        final HttpServletRequest request = createMockedRequest("/get-test", "GET");
        final HttpServletResponse response = createMockedResponse();

        sut.service(request, response);

        verify(request.getRequestDispatcher("/get-test.jsp")).forward(request, response);
    }
    
    private HttpServletRequest createMockedRequest(final String url, final String method) {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn(url);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        return request;
    }

    private HttpServletResponse createMockedResponse() {
        return mock(HttpServletResponse.class);
    }
}
