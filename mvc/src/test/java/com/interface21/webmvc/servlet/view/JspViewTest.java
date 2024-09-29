package com.interface21.webmvc.servlet.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    private static final String REQUEST_NOT_CONTAIN_REDIRECT_URL = "/get-test";
    private static final String REQUEST_CONTAIN_REDIRECT_URL = "redirect:/test";

    @Test
    @DisplayName("리다이렉트 동작 확인 성공")
    void testRenderRedirect() throws Exception {
        JspView jspView = new JspView(REQUEST_CONTAIN_REDIRECT_URL);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        jspView.render(Map.of(), request, response);

        verify(response).sendRedirect("/test");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    @DisplayName("JSP 페이지로 포워딩 성공")
    void testRenderForwardToJsp() throws Exception {
        JspView jspView = new JspView(REQUEST_NOT_CONTAIN_REDIRECT_URL);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(REQUEST_NOT_CONTAIN_REDIRECT_URL)).thenReturn(dispatcher);

        jspView.render(Map.of(), request, response);

        verify(request).getRequestDispatcher(REQUEST_NOT_CONTAIN_REDIRECT_URL);
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @DisplayName("모델 데이터를 요청으로 전달 성공")
    @Test
    void testModelAttributesSetOnRequest() throws Exception {
        JspView jspView = new JspView(REQUEST_NOT_CONTAIN_REDIRECT_URL);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(REQUEST_NOT_CONTAIN_REDIRECT_URL)).thenReturn(dispatcher);
        Map<String, Object> model = Map.of("key1", "value1", "key2", "value2");

        jspView.render(model, request, response);

        verify(request).setAttribute("key1", "value1");
        verify(request).setAttribute("key2", "value2");
    }
}
