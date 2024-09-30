package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ManualHandlerAdapter 테스트")
class ManualHandlerAdapterTest {

    private ManualHandlerAdapter manualHandlerAdapter;
    private Controller mockController;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        manualHandlerAdapter = new ManualHandlerAdapter();
        mockController = mock(Controller.class);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("Handler 지원 여부 확인")
    void supports() {
        Assertions.assertAll(
                () -> assertThat(manualHandlerAdapter.supports(mockController)).isTrue(),
                () -> assertThat(manualHandlerAdapter.supports(new Object())).isFalse()
        );
    }

    @Test
    @DisplayName("Handler 처리 및 ModelAndView 반환")
    void handle() throws Exception {
        when(mockController.execute(mockRequest, mockResponse)).thenReturn("/test.jsp");

        ModelAndView modelAndView = manualHandlerAdapter.handle(mockRequest, mockResponse, mockController);
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
