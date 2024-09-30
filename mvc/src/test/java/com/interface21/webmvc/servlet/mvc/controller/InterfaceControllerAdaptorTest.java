package com.interface21.webmvc.servlet.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InterfaceControllerAdaptorTest {

    private InterfaceControllerAdaptor adaptor;
    private Controller mockInterfaceController;
    private HandlerExecution mockAnnotationController;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        adaptor = new InterfaceControllerAdaptor();
        mockInterfaceController = mock(Controller.class);
        mockAnnotationController = mock(HandlerExecution.class);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @DisplayName("실행 가능 여부를 알 수 있다.")
    @Test
    void canExecute() {
        assertAll(
                () -> assertThat(adaptor.canExecute(mockInterfaceController)).isTrue(),
                () -> assertThat(adaptor.canExecute(mockAnnotationController)).isFalse()
        );
    }

    @DisplayName("실행 시 Controller의 execute 메서드를 호출하고 ModelAndView를 반환한다.")
    @Test
    void execute() throws Exception {
        when(mockInterfaceController.execute(mockRequest, mockResponse)).thenReturn("test");

        ModelAndView modelAndView = adaptor.execute(mockInterfaceController, mockRequest, mockResponse);

        verify(mockInterfaceController, times(1)).execute(mockRequest, mockResponse);
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
