package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionTest {

    private HandlerExecution handlerExecution;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        final Method executionMethod = TestController.class.getDeclaredMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class
        );
        handlerExecution = new HandlerExecution(executionMethod);
    }

    @Test
    @DisplayName("handler가 주어지면 올바르게 handle한 결과를 반환한다.")
    void handle() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("kth990303");

        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView)
                .extracting("model")
                .isEqualTo(Map.of("id", "kth990303"));
    }
}