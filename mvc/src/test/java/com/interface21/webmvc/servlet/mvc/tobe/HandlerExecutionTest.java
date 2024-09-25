package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.context.stereotype.Controller;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionTest {

    private HandlerExecution handlerExecution;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        Method method = TestController.class.getDeclaredMethod(
                "nonMethod",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        handlerExecution = new HandlerExecution(method);
    }

    @DisplayName("Reflection을 이용해 Method를 실행한다.")
    @Test
    void handle() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("none")).thenReturn("yes");

        // when
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView).isNotNull();
        assertThat(modelAndView.getObject("none")).isEqualTo("yes");
    }

    @Test
    @DisplayName("잘못된 핸들러 메서드 호출시 에러 발생")
    void testInvocationTargetException() throws Exception {
        // given
        Method exceptionMethod = ExceptionThrowingController.class.getMethod("testHandler", HttpServletRequest.class,
                HttpServletResponse.class);
        handlerExecution = new HandlerExecution(exceptionMethod);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // when && then
        assertThatThrownBy(() -> handlerExecution.handle(request, response))
                .isInstanceOf(HandlerExecutionException.class)
                .hasMessageContaining("메서드 호출 중 오류 발생: ");
    }

    @Test
    @DisplayName("존재하지 않는 생성자로 인해 에러 발생")
    void testNoSuchMethodException() throws Exception {
        Method abstractMethod = NoneConstructorController.class.getMethod("abstractHandler", HttpServletRequest.class,
                HttpServletResponse.class);
        handlerExecution = new HandlerExecution(abstractMethod);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        assertThatThrownBy(() -> handlerExecution.handle(request, response))
                .isInstanceOf(HandlerExecutionException.class)
                .hasMessageContaining("메서드를 찾을 수 없습니다: ");
    }

    @Controller
    static class ExceptionThrowingController {

        public ExceptionThrowingController() {
        }

        public ModelAndView testHandler(HttpServletRequest request, HttpServletResponse response) {
            throw new RuntimeException("Forced exception");
        }
    }

    abstract static class NoneConstructorController {

        public abstract ModelAndView abstractHandler(HttpServletRequest request, HttpServletResponse response);
    }
}

