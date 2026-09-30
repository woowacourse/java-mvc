package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerExecutionTest {

    @ParameterizedTest
    @ValueSource(strings = {"modelAndView", "subtype"})
    void acceptsModelAndViewReturnType(String methodName) throws Exception {
        var method = TestController.class.getDeclaredMethod(
                methodName, HttpServletRequest.class, HttpServletResponse.class);

        assertThatCode(() -> new HandlerExecution(new TestController(), method))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"string", "object", "noReturnValue"})
    void rejectsInvalidReturnTypeBeforeInvocation(String methodName) throws Exception {
        var method = TestController.class.getDeclaredMethod(
                methodName, HttpServletRequest.class, HttpServletResponse.class);

        assertThatThrownBy(() -> new HandlerExecution(new TestController(), method))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ModelAndView")
                .hasMessageContaining(TestController.class.getName())
                .hasMessageContaining(methodName);
    }

    static class TestController {

        public ModelAndView modelAndView(HttpServletRequest request, HttpServletResponse response) {
            return new ModelAndView(new JspView("/test.jsp"));
        }

        public ExtendedModelAndView subtype(HttpServletRequest request, HttpServletResponse response) {
            return new ExtendedModelAndView();
        }

        public String string(HttpServletRequest request, HttpServletResponse response) {
            return "/test.jsp";
        }

        public Object object(HttpServletRequest request, HttpServletResponse response) {
            return new ModelAndView(new JspView("/test.jsp"));
        }

        public void noReturnValue(HttpServletRequest request, HttpServletResponse response) {
        }
    }

    static class ExtendedModelAndView extends ModelAndView {

        ExtendedModelAndView() {
            super(new JspView("/test.jsp"));
        }
    }
}
