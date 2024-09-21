package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionTest {

    @DisplayName("필드로 가진 메서드를 invoke하여 요청을 처리한다.")
    @Test
    void handle() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        TestController testController = new TestController();
        Method findUserId = testController.getClass()
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(testController, findUserId);

        // when
        ModelAndView expected = handlerExecution.handle(request, response);
        ModelAndView actual = testController.findUserId(request, response);

        // then
        assertThat(expected.getObject("id")).isEqualTo(actual.getObject("id"));
    }
}