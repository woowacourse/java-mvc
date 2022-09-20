package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.tobe.adapter.AnnotationHandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class AnnotationHandlerAdapterTest {
    private final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

    @Test
    void supports() throws NoSuchMethodException {
        final Method executionMethod = TestAnnotationController.class.getDeclaredMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class
        );
        HandlerExecution handlerExecution = new HandlerExecution(executionMethod);
        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
    }

    @Test
    void handle() throws Exception {
        final HttpServletRequest request = spy(HttpServletRequest.class);
        final HttpServletResponse response = spy(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final Method executionMethod = TestAnnotationController.class.getDeclaredMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class
        );

        HandlerExecution handlerExecution = new HandlerExecution(executionMethod);
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerExecution);

        JspView jspView = (JspView) modelAndView.getView();
        assertThat(jspView.getViewName()).isEqualTo("");
        assertThat(modelAndView.getModel()).usingRecursiveComparison()
                .comparingOnlyFields("id")
                .isEqualTo(Map.of("id", "gugu"));
    }
}
