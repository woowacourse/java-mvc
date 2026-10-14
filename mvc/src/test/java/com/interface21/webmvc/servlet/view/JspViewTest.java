package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    void 모델을_요청에_넣고_JSP로_forward한다() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/user.jsp")).thenReturn(dispatcher);
        JspView jspView = new JspView("/user.jsp");

        // when
        jspView.render(Map.of("id", "juni"), request, response);

        // then
        verify(request).setAttribute("id", "juni");
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void redirect_뷰는_새_주소로_이동시킨다() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        JspView jspView = new JspView("redirect:/users");

        // when
        jspView.render(Map.of("id", "juni"), request, response);

        // then
        verify(response).sendRedirect("/users");
        verify(request, never()).getRequestDispatcher(anyString());
        verify(request, never()).setAttribute(anyString(), any());
    }
}
