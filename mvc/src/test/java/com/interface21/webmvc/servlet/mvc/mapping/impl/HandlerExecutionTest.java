package com.interface21.webmvc.servlet.mvc.mapping.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.impl.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionTest {

    @Test
    @DisplayName("주어진 메서드에 대해 HandlerExecution 객체를 생성한다.")
    void create() throws NoSuchMethodException {
        Method declaredMethod = TestClass.class.getDeclaredMethod(
                "testMethod", HttpServletRequest.class, HttpServletResponse.class);

        assertThatCode(() -> new HandlerExecution(declaredMethod, new TestClass()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("저장해둔 메서드를 실행한다.")
    void handle() throws Exception {
        Method declaredMethod = TestClass.class.getDeclaredMethod(
                "testMethod", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(declaredMethod, new TestClass());

        ModelAndView modelAndView = handlerExecution.handle(null, null);

        assertThat(modelAndView.getView()).isInstanceOf(JsonView.class);
    }

    static class TestClass {
        public ModelAndView testMethod(HttpServletRequest request, HttpServletResponse response) {
            return new ModelAndView(new JsonView());
        }
    }
}
