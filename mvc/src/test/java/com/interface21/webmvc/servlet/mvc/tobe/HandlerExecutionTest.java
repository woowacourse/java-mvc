package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class HandlerExecutionTest {

    static class TestHandler {

        public static Object dummy = new Object();

        public ModelAndView handle(HttpServletRequest req, HttpServletResponse res) {
            ModelAndView modelAndView = new ModelAndView(new JspView(""));
            modelAndView.addObject("test", dummy);
            return modelAndView;
        }
    }

    @DisplayName("핸들러 메소드를 호출 할 수 있다")
    @Test
    void callHandlerMethod() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Method method = TestHandler.class.getMethod("handle", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution execution = new HandlerExecution(method);

        ModelAndView actual = execution.handle(request, response);

        assertThat(actual.getObject("test")).isEqualTo(TestHandler.dummy);
    }
}
