package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.TestHttpServletRequest;
import support.TestHttpServletResponse;

class HandlerExecutionTest {

    @DisplayName("HandlerExecution을 통해 메서드를 실행시킬 수 있다.")
    @Test
    void executeMethodThroughHandlerExecution() throws Exception {
        // given
        TestClass executionTarget = new TestClass();
        Method targetMethod = TestClass.class.getDeclaredMethod("handle", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution execution = new HandlerExecution(executionTarget, targetMethod);

        // when
        ModelAndView modelAndView = execution.handle(new TestHttpServletRequest(), new TestHttpServletResponse());
        View view = modelAndView.getView();

        // then
        assertThat(view).isInstanceOf(TestView.class);
    }

    @DisplayName("파라미터가 없는 Method를 가진 객체로 생성할 경우 오류가 발생한다.")
    @Test
    void failCreateHandlerExecutionWithNoParameter() throws Exception {
        // given
        NoParameterTestClass executionTarget = new NoParameterTestClass();
        Method[] targetMethods = NoParameterTestClass.class.getDeclaredMethods();

        // when
        assertThatThrownBy(() -> {
            Arrays.stream(targetMethods)
                    .map(method -> new HandlerExecution(executionTarget, method))
                    .toList();
        })
                // then
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("반환 타입이 다른 Method를 가진 객체로 생성할 경우 오류가 발생한다.")
    @Test
    void failCreateHandlerExecutionWithInvalidReturnType() throws Exception {
        // given
        InvalidReturnTypeTestClass executionTarget = new InvalidReturnTypeTestClass();
        Method[] targetMethods = InvalidReturnTypeTestClass.class.getDeclaredMethods();

        // when
        assertThatThrownBy(() -> {
            Arrays.stream(targetMethods)
                    .map(method -> new HandlerExecution(executionTarget, method))
                    .toList();
        })
                // then
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("파라미터 타입이 다른 Method를 가진 객체로 생성할 경우 오류가 발생한다.")
    @Test
    void failCreateHandlerExecutionWithInvalidParameter() throws Exception {
        // given
        InvalidParameterTestClass executionTarget = new InvalidParameterTestClass();
        Method[] targetMethods = InvalidParameterTestClass.class.getDeclaredMethods();

        // when
        assertThatThrownBy(() -> {
            Arrays.stream(targetMethods)
                    .map(method -> new HandlerExecution(executionTarget, method))
                    .toList();
        })
                // then
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static class TestClass {
        ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new TestView());
        }
    }

    private static class NoParameterTestClass {
        ModelAndView handle() {
            return new ModelAndView(new TestView());
        }
    }

    private static class InvalidParameterTestClass {
        ModelAndView handle(String request, String response) {
            return new ModelAndView(new TestView());
        }
    }

    private static class InvalidReturnTypeTestClass {
        String handle(final HttpServletRequest request, final HttpServletResponse response) {
            return "";
        }
    }

    private static class TestView implements View {

        @Override
        public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
                throws Exception {
            System.out.println("this is test view");
        }
    }
}