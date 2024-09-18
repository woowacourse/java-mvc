package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.handleradaptor.ControllerHandlerAdaptor;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class ControllerHandlerAdaptorTest {

    @DisplayName("Controller의 반환값 String을 이름으로 하는  JspView로 변환시킨다.")
    @Test
    void test() throws Exception {
        HandlerAdaptor adaptor = new ControllerHandlerAdaptor();
        Controller controller = (request, response) -> "aaa";

        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        ModelAndView modelAndView = adaptor.handle(request, response, controller);

        View view = modelAndView.getView();
        assertAll(
                () -> assertThat(view).isInstanceOf(JspView.class),
                () -> assertThat(((JspView) view)).extracting("viewName").isEqualTo("aaa")
        );

    }

}
