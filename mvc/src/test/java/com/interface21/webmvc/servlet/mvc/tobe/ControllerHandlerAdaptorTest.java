package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerHandlerAdaptorTest {

    @Test
    @DisplayName("Object 객체 handler를 받아서 결과에 해당하는 ModelAndView를 반환한다.")
    void handle() throws Exception {
        //given
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Controller handler = mock(Controller.class);

        when(handler.execute(request, response)).thenReturn("test_view");

        ControllerHandlerAdaptor controllerHandlerAdaptor = new ControllerHandlerAdaptor();

        //when
        ModelAndView modelAndView = controllerHandlerAdaptor.handle(request, response, handler);

        //then
        assertThat(modelAndView.getView()).isExactlyInstanceOf(JspView.class)
                .hasFieldOrPropertyWithValue("viewName", "test_view");
    }

    @Test
    @DisplayName("Object 객체 handler가 Controller 클래스에 속하는지 여부를 반환한다.")
    void supports() {
        //given
        Object handler = mock(Controller.class);

        ControllerHandlerAdaptor controllerHandlerAdaptor = new ControllerHandlerAdaptor();

        //when
        boolean isControllerInstance = controllerHandlerAdaptor.supports(handler);

        //then
        assertThat(isControllerInstance).isTrue();
    }
}
