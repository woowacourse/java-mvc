package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;
    private AnnotationHandlerAdaptor annotationHandlerAdaptor;

    @BeforeEach
    void setUp() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        ControllerScanner samples = new ControllerScanner("samples");
        handlerMapping = new AnnotationHandlerMapping(samples);
        handlerMapping.initialize();

        annotationHandlerAdaptor = new AnnotationHandlerAdaptor();
    }

    @Test
    void get() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerExecution handlerExecution = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = annotationHandlerAdaptor.handle(request, response, handlerExecution);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final HandlerExecution handlerExecution = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = annotationHandlerAdaptor.handle(request, response, handlerExecution);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
