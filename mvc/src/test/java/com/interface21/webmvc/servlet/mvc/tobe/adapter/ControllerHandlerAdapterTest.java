package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;

@DisplayName("ControllerHandlerAdapter Test")
class ControllerHandlerAdapterTest {

    @DisplayName("입력된 handler 객체가 Controller 인스턴스면 true를 반환한다.")
    @Test
    void supportsTrue() {
        // Given
        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        final Controller handler = (final HttpServletRequest req, final HttpServletResponse res) -> "";

        // When
        final boolean supported = controllerHandlerAdapter.supports(handler);

        // Then
        assertThat(supported).isTrue();
    }

    @DisplayName("입력된 handler 객체가 Controller 인스턴스가 아니면 false를 반환한다.")
    @Test
    void supportsFalse() {
        // Given
        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        final HandlerExecution handler = new HandlerExecution(null, null);

        // When
        final boolean supported = controllerHandlerAdapter.supports(handler);

        // Then
        assertThat(supported).isFalse();
    }
    
    @DisplayName("입력된 handler에 작업 처리 요청을 하고 그 결과를 ModelAndView 형태로 반환한다.")
    @Test        
    void handle() throws Exception {
        // Given
        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        final Controller handler = (final HttpServletRequest req, final HttpServletResponse res) -> "/success";
                
        // When
        final ModelAndView result = controllerHandlerAdapter.handle(null, null, handler);

        // Then
        assertThat(result).isNotNull();
    }
}
