package com.interface21.webmvc.servlet.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class ControllerHandlerAdapterTest {

    private ControllerHandlerAdapter adapter;
    private Controller controller;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        adapter = new ControllerHandlerAdapter();
        controller = new TestController();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void canHandleTest_whenHandlerIsController_returnTrue() {
        Object handler = controller;

        boolean result = adapter.canHandle(handler);

        assertThat(result).isTrue();
    }

    @Test
    void canHandleTest_whenHandlerIsNotController_returnFalse() {
        // given
        Object handler = new Object();

        // when
        boolean result = adapter.canHandle(handler);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void handleTest() throws Exception {
        // given
        String viewName = "testViewName";

        // when
        ModelAndView modelAndView = adapter.handle(request, response, controller);

        // then
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
        assertThat(modelAndView.getView()).isEqualTo(new JspView(viewName));
    }

    private static class TestController implements Controller {

        @Override
        public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            return "testViewName";
        }
    }
}
