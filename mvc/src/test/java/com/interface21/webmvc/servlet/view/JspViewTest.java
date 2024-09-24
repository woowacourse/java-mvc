package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @DisplayName("viewName이 redirect로 시작하면 sendRedirect가 호출된다.")
    @Test
    void redirectWhenViewNameStartsWithRedirect() throws Exception {
        String redirectUrl = "redirect:/home";
        JspView view = new JspView(redirectUrl);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        view.render(Map.of(), mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("/home");
    }

    @DisplayName("model에 들어있는 데이터를 HttpServletRequest에 설정한다.")
    @Test
    void setModelAttributesToRequest() throws Exception {
        String viewName = "/view.jsp";
        JspView view = new JspView(viewName);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        RequestDispatcher mockDispatcher = mock(RequestDispatcher.class);

        when(mockRequest.getRequestDispatcher(viewName)).thenReturn(mockDispatcher);

        Map<String, Object> model = Map.of("key1", "value1", "key2", "value2");

        view.render(model, mockRequest, mockResponse);

        verify(mockRequest).setAttribute("key1", "value1");
        verify(mockRequest).setAttribute("key2", "value2");
    }

    @DisplayName("viewName이 redirect로 시작하지 않으면, RequestDispatcher가 JSP로 포워딩한다.")
    @Test
    void forwardWhenViewNameDoesNotStartWithRedirect() throws Exception {
        String viewName = "/view.jsp";
        JspView view = new JspView(viewName);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        RequestDispatcher mockDispatcher = mock(RequestDispatcher.class);

        when(mockRequest.getRequestDispatcher(viewName)).thenReturn(mockDispatcher);

        view.render(Map.of(), mockRequest, mockResponse);

        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }
}
