package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.mapping.AnnotationHandlerMapping;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void get() {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ModelAndView modelAndView = mock(ModelAndView.class);
        //when
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final String viewName = handlerExecution.handle(request, response, modelAndView);
        //then
        assertThat(handlerExecution.getHandler().getClass()).isEqualTo(TestController.class);
        assertThat(viewName).isEqualTo("test controller get method");
    }

    @Test
    void post() {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ModelAndView modelAndView = new ModelAndView();
        //when
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final String viewName = handlerExecution.handle(request, response, modelAndView);
        //then
        assertThat(handlerExecution.getHandler().getClass()).isEqualTo(TestController.class);
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        assertThat(viewName).isEqualTo("test controller post method");
    }
}
