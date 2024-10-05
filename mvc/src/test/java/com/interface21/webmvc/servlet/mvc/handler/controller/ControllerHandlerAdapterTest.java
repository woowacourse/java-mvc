package com.interface21.webmvc.servlet.mvc.handler.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.handler.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.annotation.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new ControllerHandlerAdapter();
    }

    @DisplayName("Controller 타입 객체를 지원한다.")
    @Test
    void supports_WhenHandlerInstanceOfController() {
        Object handler = (Controller) (req, res) -> "";

        assertThat(handlerAdapter.supports(handler)).isTrue();
    }

    @DisplayName("HandlerExecution 타입 객체는 지원하지 않는다.")
    @Test
    void supports_WhenHandlerInstanceOfHandlerAdapter() {
        Object handler = new HandlerExecution(null, null);

        assertThat(handlerAdapter.supports(handler)).isFalse();
    }

    @DisplayName("Controller를 실행하고 ModelAndView 객체를 반환한다.")
    @Test
    void handle() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Object handler = (Controller) (req, res) -> "viewName";

        ModelAndView result = handlerAdapter.handle(request, response, handler);
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(ModelAndView.class);
        assertThat(result.getView()).isInstanceOf(JspView.class);
    }
}
