package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    @Test
    @DisplayName("Handler가 Controller 타입일 때 supports가 true를 반환한다.")
    void supports_returnsTrue() {
        Object handler = mock(Controller.class);
        ManualHandlerAdapter adapter = new ManualHandlerAdapter();

        boolean result = adapter.supports(handler);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Handler가 Controller 타입이 아닐 때 supports가 false를 반환한다.")
    void supports_returnsFalse() {
        Object handler = new Object();
        ManualHandlerAdapter adapter = new ManualHandlerAdapter();

        boolean result = adapter.supports(handler);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Controller의 execute를 호출하고 ModelAndView를 반환한다.")
    void handle() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Controller controller = mock(Controller.class);
        String expectedView = "/test.jsp";
        when(controller.execute(request, response)).thenReturn(expectedView);

        ManualHandlerAdapter adapter = new ManualHandlerAdapter();
        ModelAndView result = adapter.handle(request, response, controller);

        assertThat(result.getView()).isNotNull();
    }
}
