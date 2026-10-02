package com.interface21.webmvc.servlet.mvc.tobe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionTest {

    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("컨트롤러의 핸들러 메서드를 호출하고 그 결과를 반환한다")
    void handle_invokesControllerMethod() throws Exception {
        // given
        when(request.getAttribute("id")).thenReturn("gugu");
        Method method = TestController.class.getMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(new TestController(), method);

        // when
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertEquals("gugu", modelAndView.getObject("id"));
    }

    @Test
    @DisplayName("핸들러 메서드 시그니처가 맞지 않으면 예외가 발생한다")
    void handle_withInvalidSignature() throws Exception {
        // given
        Method method = Object.class.getMethod("toString");
        HandlerExecution handlerExecution = new HandlerExecution(new Object(), method);

        // when, then
        Assertions.assertThatThrownBy(
                () -> handlerExecution.handle(request, response)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("핸들러 메서드가 던진 예외는 InvocationTargetException으로 전달된다")
    void handle_whenControllerThrows() throws Exception {
        // given
        Method method = ThrowingController.class.getMethod(
                "fail",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        HandlerExecution handlerExecution = new HandlerExecution(new ThrowingController(), method);

        // when, then
        Assertions.assertThatThrownBy(
                () -> handlerExecution.handle(request, response)
        ).isInstanceOf(InvocationTargetException.class);
    }

    public static class ThrowingController {

        public ModelAndView fail(final HttpServletRequest request, final HttpServletResponse response) {
            throw new IllegalStateException("fail");
        }
    }
}
