package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionTest {

    private HandlerExecution execution;

    @Test
    void 해당_method를_실행한다() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        final TestController testController = new TestController();
        final Class<? extends TestController> clazz = testController.getClass();
        final Method method = clazz.getDeclaredMethod("findUserId", HttpServletRequest.class,
                HttpServletResponse.class);
        execution = new HandlerExecution(testController, method);

        final ModelAndView modelAndView = execution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
