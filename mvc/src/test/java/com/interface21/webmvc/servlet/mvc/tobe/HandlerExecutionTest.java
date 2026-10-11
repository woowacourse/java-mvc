package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionTest {

    @Test
    void 등록된_컨트롤러_메서드를_실행한다() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("juni");
        Method handlerMethod = TestController.class.getMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution execution = new HandlerExecution(new TestController(), handlerMethod);

        // when
        ModelAndView modelAndView = execution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("juni");
    }
}
