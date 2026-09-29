package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class HandlerExecutionTest {

    @ParameterizedTest
    @ValueSource(strings = {"privateMethod", "wrongParameter", "wrongReturnType"})
    void 핸들러_메서드_시그니처가_잘못되면_생성에_실패한다(final String methodName) {
        final var method = findMethod(methodName);

        assertThatThrownBy(() -> new HandlerExecution(new SampleController(), method))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 컨트롤러에서_던진_예외를_그대로_전달한다() throws Exception {
        final var method = SampleController.class.getMethod("throwException", HttpServletRequest.class, HttpServletResponse.class);
        final var handlerExecution = new HandlerExecution(new SampleController(), method);

        assertThatThrownBy(() -> handlerExecution.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("controller exception");
    }

    private static Method findMethod(final String name) {
        for (final var method : SampleController.class.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        throw new IllegalArgumentException(name);
    }

    static class SampleController {

        private ModelAndView privateMethod(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView(""));
        }

        public ModelAndView wrongParameter(final HttpServletRequest request) {
            return new ModelAndView(new JspView(""));
        }

        public String wrongReturnType(final HttpServletRequest request, final HttpServletResponse response) {
            return "/index.jsp";
        }

        public ModelAndView throwException(final HttpServletRequest request, final HttpServletResponse response) {
            throw new IllegalArgumentException("controller exception");
        }
    }
}
