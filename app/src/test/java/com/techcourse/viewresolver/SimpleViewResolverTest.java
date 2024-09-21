package com.techcourse.viewresolver;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleViewResolverTest {

    private SimpleViewResolver viewResolver;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        viewResolver = new SimpleViewResolver();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testResolveViewWithModelAndView() throws Exception {
        JspView view = new JspView(JspView.REDIRECT_PREFIX + "/index.jsp");
        ModelAndView modelAndView = new ModelAndView(view);

        viewResolver.resolveView(modelAndView, request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    public void testResolveViewWithString() throws Exception {
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        String viewName = "/testView";

        viewResolver.resolveView(viewName, request, response);

        verify(request.getRequestDispatcher(viewName)).forward(request, response);
    }

    @Test
    public void testResolveViewWithRedirect() throws Exception {
        String viewName = JspView.REDIRECT_PREFIX + "/index.jsp";

        viewResolver.resolveView(viewName, request, response);

        verify(response).sendRedirect("/index.jsp");
    }
}
