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
    void handle() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        TestController testController = new TestController();
        Method method = testController.getClass()
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(testController, method);

        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
