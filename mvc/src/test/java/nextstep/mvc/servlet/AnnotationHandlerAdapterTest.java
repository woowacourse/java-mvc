package nextstep.mvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerAdapterTest {
    private AnnotationHandlerAdapter annotationHandlerAdapter;

    @BeforeEach
    void setUp() {
        annotationHandlerAdapter = new AnnotationHandlerAdapter();
    }

    @Test
    void support() {
        final TestController testController = new TestController();
        Method[] methods = TestController.class.getDeclaredMethods();
        boolean supports = annotationHandlerAdapter.supports(new HandlerExecution(methods[0], testController));

        assertThat(supports).isTrue();
    }

    @Test
    void handle() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("sally");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final TestController testController = new TestController();
        Method[] methods = TestController.class.getDeclaredMethods();
        HandlerExecution handler = new HandlerExecution(methods[0], testController);

        ModelAndView modelAndView = handler.handle(request, response);
        assertThat(modelAndView.getObject("id")).isEqualTo("sally");
        assertThat(modelAndView.getView()).usingRecursiveComparison()
                .isEqualTo(new JspView(""));
    }
}
