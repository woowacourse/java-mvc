package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.ViewResolver;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.viewresolver.SimpleViewResolver;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @DisplayName("올바른 uri 요청을 보내면 JSP를 랜더링하고 forward 한다.")
    @Test
    void serviceSuccessWithValidRequest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        given(request.getRequestURI()).willReturn("/register");
        given(request.getMethod()).willReturn("GET");
        given(request.getRequestDispatcher("register.jsp")).willReturn(requestDispatcher);

        HandlerRegistry handlerRegistry = mock(HandlerRegistry.class);
        given(handlerRegistry.handle(any(), any())).willReturn(new ModelAndView(new JspView("register.jsp")));

        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerRegistry, new SimpleViewResolver());

        dispatcherServlet.service(request, response);

        then(requestDispatcher).should().forward(request, response);
    }

    @DisplayName("올바르지 않은 uri 요청을 보내면 에러를 던진다.")
    @Test
    void serviceFailWithInvalidRequest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HandlerRegistry handlerRegistry = mock(HandlerRegistry.class);
        ViewResolver viewResolver = mock(ViewResolver.class);
        given(handlerRegistry.handle(request, response)).willThrow(new RuntimeException());

        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerRegistry, viewResolver);

        assertThatCode(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class);
    }
}
