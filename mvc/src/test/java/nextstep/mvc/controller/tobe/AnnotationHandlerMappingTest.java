package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.core.AnnotationConfigApplicationContext;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.adapter.RequestMappingHandlerAdapter;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

class AnnotationHandlerMappingTest {

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
            TestController.class);

        handlerMapping = new AnnotationHandlerMapping(applicationContext);
        handlerMapping.initialize();
        this.handlerAdapter = new RequestMappingHandlerAdapter();
    }

    @Test
    void get() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final MethodHandler methodHandler = (MethodHandler) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerAdapter.handle(request, response, methodHandler);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final MethodHandler methodHandler = (MethodHandler) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerAdapter.handle(request, response, methodHandler);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void notFound() {

    }
}
