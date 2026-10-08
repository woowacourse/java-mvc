package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionTest {
    @DisplayName("올바른 시그니처의 메서드로 HandlerExecution을 생성할 수 있다.")
    @Test
    void create() throws NoSuchMethodException {
        final Method method = Fixture.class.getMethod("valid", HttpServletRequest.class, HttpServletResponse.class);

        assertThatCode(() -> new HandlerExecution(new Fixture(), method)).doesNotThrowAnyException();
    }

    @DisplayName("public이 아닌 메서드는 예외가 발생한다.")
    @Test
    void notPublic() throws Exception {
        final Method method = Fixture.class.getDeclaredMethod("notPublic", HttpServletRequest.class,
                HttpServletResponse.class);

        assertThatThrownBy(() -> new HandlerExecution(new Fixture(), method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("public");
    }

    @DisplayName("파라미터가 (HttpServletRequest, HttpServletResponse)가 아니면 예외가 발생한다.")
    @Test
    void invalidParameter() throws Exception {
        final Method method = Fixture.class.getMethod("invalidParameter", String.class);

        assertThatThrownBy(() -> new HandlerExecution(new Fixture(), method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("파라미터");
    }

    @DisplayName("반환 타입이 ModelAndView가 아니면 예외가 발생한다.")
    @Test
    void invalidReturnType() throws Exception {
        final Method method = Fixture.class.getMethod("invalidReturnType", HttpServletRequest.class,
                HttpServletResponse.class);

        assertThatThrownBy(() -> new HandlerExecution(new Fixture(), method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ModelAndView");
    }

    static class Fixture {
        public ModelAndView valid(HttpServletRequest request, HttpServletResponse response) {
            return null;
        }

        ModelAndView notPublic(HttpServletRequest request, HttpServletResponse response) {
            return null;
        }

        public ModelAndView invalidParameter(String value) {
            return null;
        }

        public String invalidReturnType(HttpServletRequest request, HttpServletResponse response) {
            return null;
        }
    }
}
