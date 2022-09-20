package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.adapter.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {
    private final DispatcherServlet dispatcherServlet = new DispatcherServlet();

    @BeforeEach
    void setup() {
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        dispatcherServlet.addHandlerMapping(handlerMapping);
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());

        dispatcherServlet.init();
    }

    @Test
    void service() throws ServletException {
        final HttpServletRequest request = spy(HttpServletRequest.class);
        final HttpServletResponse response = spy(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
    }
}
