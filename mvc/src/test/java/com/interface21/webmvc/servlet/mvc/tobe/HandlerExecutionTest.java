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
    void handle() throws Exception {
        // given
        TestController testController = new TestController();
        Method method = testController.getClass()
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(new TestController(), method);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("mia");

        // when
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("mia");
    }
}
