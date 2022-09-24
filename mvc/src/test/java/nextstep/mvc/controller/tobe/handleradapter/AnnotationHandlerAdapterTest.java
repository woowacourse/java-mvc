package nextstep.mvc.controller.tobe.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.tobe.handlermapping.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;
import samples.TestManualController;

class AnnotationHandlerAdapterTest {

    @Test
    void supports() throws Exception {
        Class<?> clazz = Class.forName("samples.TestAnnotationController");
        Method method = clazz.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handler = new HandlerExecution(new TestAnnotationController(), method);
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        boolean supports = annotationHandlerAdapter.supports(handler);

        assertThat(supports).isTrue();
    }

    @Test
    void notSupports() {
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        boolean supports = annotationHandlerAdapter.supports(new TestManualController());

        assertThat(supports).isFalse();
    }

    @Test
    void handle() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Class<?> clazz = Class.forName("samples.TestAnnotationController");
        Method method = clazz.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handler = new HandlerExecution(new TestAnnotationController(), method);
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        ModelAndView modelAndView = annotationHandlerAdapter.handle(request, response, handler);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
