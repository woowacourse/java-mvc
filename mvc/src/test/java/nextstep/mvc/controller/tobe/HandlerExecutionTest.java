package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class HandlerExecutionTest {

    private static final String TEST_VIEW_NAME = "/test.jsp";

    @Test
    void invokeTargetMethod() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getDeclaredMethod("test", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(target, method);

        // when
        ModelAndView actual = handlerExecution.handle(request, response);

        // then
        assertThat(actual.getView()).isInstanceOf(JspView.class);
        assertThat(((JspView) actual.getView()).getViewName()).isEqualTo(TEST_VIEW_NAME);
    }

    private static class TestTarget {

        public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
            return new ModelAndView(new JspView(TEST_VIEW_NAME));
        }
    }
}
