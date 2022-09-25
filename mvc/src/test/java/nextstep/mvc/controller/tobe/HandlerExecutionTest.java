package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import samples.TestController;

class HandlerExecutionTest {

    @Test
    @DisplayName("Request, Response를 받아 method를 실행하고 ModelAndView를 반환한다.")
    void handle() {
        // given
        final Object controller = new TestController();
        final Method findUserIdMethod = controller.getClass().getDeclaredMethods()[0];
        final HandlerExecution execution = new HandlerExecution(controller, findUserIdMethod);

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        final ModelAndView modelAndView = execution.handle(request, response);
        final String id = (String)modelAndView.getAttribute("id");
        final Object expectedNull = modelAndView.getAttribute("invalidAttribute");

        // then
        assertAll(
            () -> assertThat(id).isEqualTo("gugu"),
            () -> assertThat(expectedNull).isNull()
        );
    }

}
