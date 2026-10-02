package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionTest {

    @Test
    @DisplayName("지정한 객체와 메서드에 요청·응답을 전달하고 반환된 ModelAndView를 그대로 돌려준다")
    void returnsControllerResult() throws Exception {
        // given
        final var controller = mock(TestController.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var expected = new ModelAndView(new JspView("/get-test.jsp"));
        when(controller.findUserId(request, response)).thenReturn(expected);

        final var method = TestController.class.getMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final var handlerExecution = new HandlerExecution(controller, method);

        // when
        final var actual = handlerExecution.handle(request, response);

        // then
        assertThat(actual).isSameAs(expected);
    }
}
