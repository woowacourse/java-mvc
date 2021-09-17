package nextstep.mvc.controller.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionHandlerAdapterTest {

    private AnnotationHandlerMapping handlerMapping;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
    }

    @Test
    @DisplayName("supports")
    void supports() {
        Object handler = handlerMapping.getHandler(request);
        HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
        assertTrue(handlerExecutionHandlerAdapter.supports(handler));
    }

    @Test
    @DisplayName("handle")
    void handle() throws Exception {
        final HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = handlerMapping.getHandler(request);
        HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();

        ModelAndView modelAndView = handlerExecutionHandlerAdapter.handle(request, response, handler);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}