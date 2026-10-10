package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerExecutionTest {

    private final SignatureController controller = new SignatureController();

    @Test
    void 올바른_시그니처의_메서드로_생성할_수_있다() throws Exception {
        final var method = SignatureController.class.getDeclaredMethod(
                "valid", HttpServletRequest.class, HttpServletResponse.class);

        assertThatCode(() -> new HandlerExecution(controller, method))
                .doesNotThrowAnyException();
    }

    @Test
    void 상위_타입으로_요청을_받는_메서드로_생성할_수_있다() throws Exception {
        final var method = SignatureController.class.getDeclaredMethod(
                "superTypeParameter", ServletRequest.class, HttpServletResponse.class);

        assertThatCode(() -> new HandlerExecution(controller, method))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"missingParameter", "reversedParameters", "stringReturn", "voidReturn", "privateMethod"})
    void 시그니처가_잘못된_메서드로_생성하면_예외가_발생한다(final String methodName) {
        final var method = findMethod(methodName);

        assertThatThrownBy(() -> new HandlerExecution(controller, method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(methodName);
    }

    private Method findMethod(final String methodName) {
        for (final var method : SignatureController.class.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new IllegalArgumentException(methodName);
    }

    @SuppressWarnings("unused")
    static class SignatureController {

        public ModelAndView valid(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView(""));
        }

        public ModelAndView superTypeParameter(final ServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView(""));
        }

        public ModelAndView missingParameter(final HttpServletRequest request) {
            return new ModelAndView(new JspView(""));
        }

        public ModelAndView reversedParameters(final HttpServletResponse response, final HttpServletRequest request) {
            return new ModelAndView(new JspView(""));
        }

        public String stringReturn(final HttpServletRequest request, final HttpServletResponse response) {
            return "";
        }

        public void voidReturn(final HttpServletRequest request, final HttpServletResponse response) {
        }

        private ModelAndView privateMethod(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView(""));
        }
    }
}
