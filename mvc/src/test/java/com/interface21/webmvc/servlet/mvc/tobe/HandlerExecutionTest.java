package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;

import samples.TestController;

public class HandlerExecutionTest {

    @DisplayName("컨트롤러 메서드를 실행한다.")
    @Test
    void handleTest() throws Exception {
        // given
        final var controller = mock(TestController.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final Method method = TestController.class.getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        final ModelAndView expected = new ModelAndView(null);
        when(controller.findUserId(request, response)).thenReturn(expected);

        // when
        final ModelAndView actual = handlerExecution.handle(request, response);

        // then
        assertThat(actual).isEqualTo(expected);
        verify(controller).findUserId(request, response);
    }
}
