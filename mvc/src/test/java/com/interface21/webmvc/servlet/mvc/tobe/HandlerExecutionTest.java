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

    @DisplayName("주입받은 Method를 handle로 실행시킨다.")
    @Test
    void handle() throws Exception {
        Method method = TestHandlerExecution.class.getMethod(
                "test", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(method);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("gugu")).isEqualTo("haha");
    }

    public static class TestHandlerExecution {

        public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
            ModelAndView modelAndView = new ModelAndView(new JspView("abc"));
            modelAndView.addObject("gugu", "haha");
            return modelAndView;
        }
    }
}
