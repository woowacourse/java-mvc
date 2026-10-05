package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerExecutionTest {

    private final HandlerMethodFixture fixture = new HandlerMethodFixture();

    @Test
    @DisplayName("ModelAndView가 아닌 타입을 반환하는 핸들러를 거부한다")
    void 잘못된_반환_타입을_거부한다() throws NoSuchMethodException {
        final Method method = HandlerMethodFixture.class.getDeclaredMethod(
                "invalidReturnType",
                HttpServletRequest.class,
                HttpServletResponse.class
        );

        assertThatThrownBy(() -> new HandlerExecution(fixture, method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("invalidReturnType")
                .hasMessageContaining("ModelAndView");
    }

    @Test
    @DisplayName("요청과 응답을 인자로 받지 않는 핸들러를 거부한다")
    void 잘못된_인자를_거부한다() throws NoSuchMethodException {
        final Method method = HandlerMethodFixture.class.getDeclaredMethod(
                "invalidParameters",
                String.class
        );

        assertThatThrownBy(() -> new HandlerExecution(fixture, method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("invalidParameters")
                .hasMessageContaining("HttpServletRequest")
                .hasMessageContaining("HttpServletResponse");
    }

    @Test
    @DisplayName("public이 아닌 핸들러를 거부한다")
    void public이_아닌_핸들러를_거부한다() throws NoSuchMethodException {
        final Method method = HandlerMethodFixture.class.getDeclaredMethod(
                "invalidAccess",
                HttpServletRequest.class,
                HttpServletResponse.class
        );

        assertThatThrownBy(() -> new HandlerExecution(fixture, method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("invalidAccess")
                .hasMessageContaining("public");
    }

    private static class HandlerMethodFixture {

        public String invalidReturnType(
                final HttpServletRequest request,
                final HttpServletResponse response
        ) {
            return "invalid";
        }

        public ModelAndView invalidParameters(final String value) {
            return null;
        }

        private ModelAndView invalidAccess(
                final HttpServletRequest request,
                final HttpServletResponse response
        ) {
            return null;
        }
    }
}
