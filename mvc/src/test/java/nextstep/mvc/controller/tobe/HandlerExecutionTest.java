package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import samples.TestReflections;

class HandlerExecutionTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    void handle() throws Exception {
        TestReflections executionObject = new TestReflections();
        Method method = TestReflections.class.getMethod("handler", HttpServletRequest.class, HttpServletResponse.class);
        HandlerMethod handlerMethod = new HandlerMethod(executionObject, method);

        HandlerExecution handlerExecution = new HandlerExecution(handlerMethod);
        ModelAndView modelAndView = handlerExecution.handle(httpServletRequest, httpServletResponse);

        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
