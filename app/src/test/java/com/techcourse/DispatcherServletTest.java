package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class DispatcherServletTest {

    HttpServletRequest request;
    HttpServletResponse response;
    DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcherServlet = new DispatcherServlet();

        dispatcherServlet.init();
    }

    @Test
    @DisplayName("요청과 응답을 파라미터로 받아 적절한 뷰를 렌더링한다.")
    void service() throws Exception {
        //given
        ModelAndView modelAndView = mock(ModelAndView.class);
        View view = mock(JspView.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(modelAndView.getView()).thenReturn(view);

        //when
        dispatcherServlet.service(request, response);

        //then
        verify(dispatcher, times(1)).forward(any(), any());
    }

    @Test
    @DisplayName("처리 불가능한 요청이 들어올 경우 ServletException을 반환한다.")
    void service_fail() {
        //given
        when(request.getRequestURI()).thenReturn("/ERROR");
        when(request.getMethod()).thenReturn("GET");

        //when, then
        Assertions.assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class);
    }
}
