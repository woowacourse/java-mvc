package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    final HttpServletRequest request = mock(HttpServletRequest.class);
    final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    void supportsIsTrue() {
        HandlerExecution handler = mock(HandlerExecution.class);
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        boolean result = annotationHandlerAdapter.supports(handler);

        assertThat(result).isTrue();
    }

    @Test
    void supportsIsFalse() {
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        boolean result = annotationHandlerAdapter.supports(null);

        assertThat(result).isFalse();
    }

    @Test
    void handle() throws Exception {
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        ModelAndView result = annotationHandlerAdapter.handle(request, response, annotationHandlerMapping.getHandler(request));

        assertThat(result.getObject("id")).isEqualTo("gugu");
    }
}
