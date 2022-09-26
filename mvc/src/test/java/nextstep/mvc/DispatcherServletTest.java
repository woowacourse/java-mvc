package nextstep.mvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.AnnotationHandlerMapping;
import nextstep.mvc.controller.HandlerExecutionHandlerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private final DispatcherServlet dispatcherServlet = new DispatcherServlet();

    @BeforeEach
    void init() {
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.init();
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
    }

    @Test
    void get_annotationHandlerMapping() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        assertDoesNotThrow(() -> dispatcherServlet.service(request, response));
    }

    @Test
    void post_annotationHandlerMapping() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        assertDoesNotThrow(() -> dispatcherServlet.service(request, response));
    }
}
