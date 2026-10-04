package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    void 뷰_이름이_redirect로_시작하면_접두사를_뗀_경로로_리다이렉트한다() throws Exception {
        final JspView jspView = new JspView("redirect:/index.jsp");

        jspView.render(Map.of(), request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 리다이렉트할_때는_포워드하지_않는다() throws Exception {
        final JspView jspView = new JspView("redirect:/index.jsp");

        jspView.render(Map.of(), request, response);

        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void 뷰_이름이_redirect로_시작하지_않으면_해당_경로로_포워드한다() throws Exception {
        final JspView jspView = new JspView("/login.jsp");

        jspView.render(Map.of(), request, response);

        verify(request).getRequestDispatcher("/login.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 포워드할_때는_리다이렉트하지_않는다() throws Exception {
        final JspView jspView = new JspView("/login.jsp");

        jspView.render(Map.of(), request, response);

        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void 포워드하기_전에_모델을_요청_속성으로_담는다() throws Exception {
        final JspView jspView = new JspView("/register.jsp");

        jspView.render(Map.of("account", "gugu"), request, response);

        final InOrder inOrder = inOrder(request, requestDispatcher);
        inOrder.verify(request).setAttribute("account", "gugu");
        inOrder.verify(requestDispatcher).forward(request, response);
    }
}
