package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        handlerAdapter = new RequestMappingHandlerAdapter();
    }

    @Test
    void get() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/prefix/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var handler = handlerExecution.getHandler();
        final var modelAndView = handlerAdapter.handle(request, response, handler);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/prefix/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var handler = handlerExecution.getHandler();
        final var modelAndView = handlerAdapter.handle(request, response, handler);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void put() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/prefix/put-test");
        when(request.getMethod()).thenReturn("PUT");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var handler = handlerExecution.getHandler();
        final var modelAndView = handlerAdapter.handle(request, response, handler);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void delete() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/prefix/delete-test");
        when(request.getMethod()).thenReturn("DELETE");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var handler = handlerExecution.getHandler();
        final var modelAndView = handlerAdapter.handle(request, response, handler);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void patch() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/prefix/patch-test");
        when(request.getMethod()).thenReturn("PATCH");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var handler = handlerExecution.getHandler();
        final var modelAndView = handlerAdapter.handle(request, response, handler);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void options() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/prefix/options-test");
        when(request.getMethod()).thenReturn("OPTIONS");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var handler = handlerExecution.getHandler();
        final var modelAndView = handlerAdapter.handle(request, response, handler);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
