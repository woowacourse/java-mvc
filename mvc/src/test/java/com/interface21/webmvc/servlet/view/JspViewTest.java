package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class JspViewTest {

    private static final String REDIRECT = "/redirect";
    private static final String TEST_VIEW_NAME = "forward";

    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockDispatcher;

    @BeforeEach
    void setUp() {
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockDispatcher = mock(RequestDispatcher.class);
    }

    @Test
    @DisplayName("REDIRECT_PREFIX로 시작하는 이름의 뷰라면 response를 redirect로 설정한다.")
    void render_redirect() throws Exception {
        //given
        Map<String, ?> model = new HashMap<>();
        JspView jspView = new JspView(JspView.REDIRECT_PREFIX + REDIRECT);

        //when
        jspView.render(model, mockRequest, mockResponse);

        //then
        verify(mockResponse).sendRedirect(REDIRECT);
        verify(mockDispatcher, never()).forward(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("REDIRECT_PREFIX로 시작하는 이름의 뷰가 아니라면 리다이렉트하지 않고 requestDispatcher를 통해 포워딩한다.")
    void render_forward() throws Exception {
        //given
        Map<String, Object> model = new HashMap<>();
        model.put("id", "gugu");

        JspView jspView = new JspView(TEST_VIEW_NAME);

        when(mockRequest.getRequestDispatcher(TEST_VIEW_NAME)).thenReturn(mockDispatcher);

        //when
        jspView.render(model, mockRequest, mockResponse);

        //then
        verify(mockRequest).setAttribute("id", "gugu");
        verify(mockDispatcher).forward(mockRequest, mockResponse);
        verify(mockResponse, never()).sendRedirect(anyString());
    }
}
