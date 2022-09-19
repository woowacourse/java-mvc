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

    @Test
    void invokeHandler() throws Exception {
        // given
        TestController controller = new TestController();
        Method method = controller.getClass()
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        // when
        HandlerExecution execution = new HandlerExecution(controller, method);
        ModelAndView modelAndView = execution.handle(request, response);

        // then
        assertThat(modelAndView).isNotNull();
    }
}
