package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionTest {

    @Test
    void 컨트롤러의_메서드를_실행() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("ted");

        TestController controller = new TestController();
        Method method = controller.getClass()
                .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        // when
        ModelAndView actual = handlerExecution.handle(request, response);

        // then
        assertThat(actual.getObject("id")).isEqualTo("ted");
    }
}
