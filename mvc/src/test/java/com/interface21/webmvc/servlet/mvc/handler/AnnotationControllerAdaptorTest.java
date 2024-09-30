package com.interface21.webmvc.servlet.mvc.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.controller.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationControllerAdaptorTest {

    private AnnotationControllerAdaptor adaptor;
    private HandlerExecution mockAnnotationController;
    private Controller mockInterfaceController;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        adaptor = new AnnotationControllerAdaptor();
        mockAnnotationController = mock(HandlerExecution.class);
        mockInterfaceController = mock(Controller.class);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @DisplayName("실행 가능 여부를 알 수 있다.")
    @Test
    void canExecute() {
        assertAll(
                () -> assertThat(adaptor.canExecute(mockAnnotationController)).isTrue(),
                () -> assertThat(adaptor.canExecute(mockInterfaceController)).isFalse()
        );
    }

    @DisplayName("실행 시 HandlerExecution의 handle 메서드를 호출하고 ModelAndView를 반환한다.")
    @Test
    void execute() throws Exception {
        when(mockAnnotationController.handle(mockRequest, mockResponse))
                .thenReturn(new ModelAndView(new JspView("test")));

        ModelAndView modelAndView = adaptor.execute(mockAnnotationController, mockRequest, mockResponse);

        verify(mockAnnotationController, times(1)).handle(mockRequest, mockResponse);
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
