package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionAdapterTest {

    private final HandlerAdapter handlerAdapter = new HandlerExecutionAdapter();
    private HandlerExecution handlerExecution;

    @BeforeEach
    void setUp() throws Exception {
        final Method executionMethod = TestController.class.getDeclaredMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class
        );
        final Object controller = executionMethod.getDeclaringClass().getConstructor().newInstance();
        handlerExecution = new HandlerExecution(controller, executionMethod);
    }

    @Test
    @DisplayName("HandlerExecution 여부를 반환한다.")
    void supports() {
        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
    }

    @Test
    @DisplayName("HandlerMapping으로 찾아온 컨트롤러를 handle하여 올바른 ModelAndView를 반환해준다.")
    void handle() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("kth990303");

        final ModelAndView result = handlerAdapter.handle(request, response, handlerExecution);

        assertThat(result)
                .extracting("model")
                .isEqualTo(Map.of("id", "kth990303"));
    }
}
