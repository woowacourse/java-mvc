package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import samples.TestController;

public class HandlerExecutionTest {

    @Test
    @DisplayName("메소드 실행 후 ModelAndView를 반환한다.")
    void handle() throws Exception {

        // given
        TestController testController = new TestController();
        Method method = testController.getClass().getDeclaredMethods()[0];

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute(any())).thenReturn("panda");

        // when
        HandlerExecution handlerExecution = new HandlerExecution(testController, method);
        ModelAndView modelAndView = handlerExecution.handle(request, response);
        String id = String.valueOf(modelAndView.getObject("id"));

        // when
        assertThat(id).isEqualTo("panda");
    }
}
