package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.bean.container.BeanContainer;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class HandlerExecutionTest {

    @BeforeEach
    void setUp() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.clear();
        beanContainer.registerBeans(List.of(new TestHandlerExecution()));
    }

    @DisplayName("Method를 handle로 실행시킨다.")
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

    @DisplayName("Method의 파라미터 순서에 상관없이 handle로 실행시킨다.")
    @Test
    void handleSwitch() throws Exception {
        Method method = TestHandlerExecution.class.getMethod(
                "testSwitch", HttpServletResponse.class, HttpServletRequest.class);
        HandlerExecution handlerExecution = new HandlerExecution(method);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("gugu")).isEqualTo("haha");
    }

    @DisplayName("Method의 파라미터에 Request만 있어도 handle로 실행시킨다.")
    @Test
    void handleOnlyRequest() throws Exception {
        Method method = TestHandlerExecution.class.getMethod(
                "onlyRequest", HttpServletRequest.class);
        HandlerExecution handlerExecution = new HandlerExecution(method);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("gugu")).isEqualTo("haha");
    }

    @DisplayName("Method의 파라미터에 Request만 있어도 handle로 실행시킨다.")
    @Test
    void handleOnlyResponse() throws Exception {
        Method method = TestHandlerExecution.class.getMethod(
                "onlyResponse", HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(method);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("gugu")).isEqualTo("haha");
    }

    @DisplayName("Method의 파라미터가 없어도 handle로 실행시킨다.")
    @Test
    void none() throws Exception {
        Method method = TestHandlerExecution.class.getMethod("none");
        HandlerExecution handlerExecution = new HandlerExecution(method);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("gugu")).isEqualTo("haha");
    }

    @DisplayName("Method에 HttpServletRequest, HttpServletResponse 이외의 파라미터는 지원하지 않는다.")
    @Test
    void notSupport() throws Exception {
        Method method = TestHandlerExecution.class.getMethod(
                "notSupport", Long.class);
        HandlerExecution handlerExecution = new HandlerExecution(method);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThatThrownBy(() -> handlerExecution.handle(request, response))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지원하지 않는 파라미터 입니다.");
    }

    public static class TestHandlerExecution {

        private static ModelAndView createModelAndView() {
            ModelAndView modelAndView = new ModelAndView(new JspView("abc"));
            modelAndView.addObject("gugu", "haha");
            return modelAndView;
        }

        public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
            return createModelAndView();
        }

        public ModelAndView testSwitch(HttpServletResponse response, HttpServletRequest request) {
            return createModelAndView();
        }

        public ModelAndView onlyRequest(HttpServletRequest request) {
            return createModelAndView();
        }

        public ModelAndView onlyResponse(HttpServletResponse response) {
            return createModelAndView();
        }

        public ModelAndView none() {
            return createModelAndView();
        }

        public ModelAndView notSupport(Long id) {
            return createModelAndView();
        }
    }
}
