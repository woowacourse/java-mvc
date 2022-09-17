package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionTest {

    @Test
    void HandlerExecution은_handler를_handle할_수_있다() throws Exception {
        // given
        final Method executionMethod = TestController.class.getDeclaredMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class
        );

        final HandlerExecution handlerExecution = new HandlerExecution(TestController.class, executionMethod);

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");

        // when
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView).extracting("view", "model")
                .containsExactly(new JspView(""), Map.of("id", "gugu"));
    }
}
