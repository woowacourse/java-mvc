package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import samples.TestController;

class HandlerExecutionTest {

    @DisplayName("객체에 정의된 메서드 실행")
    @Test
    void handle() throws Exception {
        final Object object = TestController.class.getConstructor().newInstance();
        final Method method = object.getClass().getMethods()[0];
        final HandlerExecution handlerExecution = new HandlerExecution(object, method);

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        final ModelAndView modelAndView = handlerExecution.handle(request, response);
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
