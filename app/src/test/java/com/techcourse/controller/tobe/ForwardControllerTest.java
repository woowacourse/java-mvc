package com.techcourse.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.ViewResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ForwardControllerTest {

    @DisplayName("기본 URL 요청시 index.jsp 를 보여준다.")
    @Test
    void renderIndexView() throws Exception {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        ForwardController indexController = new ForwardController();

        when(request.getRequestDispatcher("/index.jsp"))
            .thenReturn(requestDispatcher);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        ModelAndView modelAndView = indexController.showIndex(request, response);
        View view = modelAndView.getView();

        ViewResolver.resolve(request, response, modelAndView);

        assertThat(view).isInstanceOf(JspView.class);
        verify(requestDispatcher).forward(request, response);
    }
}
