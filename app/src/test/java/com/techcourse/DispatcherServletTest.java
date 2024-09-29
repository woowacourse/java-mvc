package com.techcourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.interface21.webmvc.servlet.view.JspView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() throws Exception {
        dispatcherServlet = new DispatcherServlet();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);

        dispatcherServlet.init();
    }

    @DisplayName("정상적인 요청을 처리하고, 올바른 View로 forward 한다.")
    @Test
    void handleValidRequest() throws Exception {
        // Given
        String jspPath = "/index.jsp";
        setUpRequest(mockRequest, "/", "GET");

        // When
        when(mockRequest.getRequestDispatcher(jspPath)).thenReturn(requestDispatcher);
        dispatcherServlet.service(mockRequest, mockResponse);

        // Then
        verify(requestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("핸들러를 찾지 못한 경우, 404 Not Found 페이지를 렌더링한다.")
    void handleNotFound() throws Exception {
        // Given
        String notFoundView = JspView.NOT_FOUND_VIEW;
        setUpRequest(mockRequest, "/notFound", "GET");

        // When
        when(mockRequest.getRequestDispatcher(notFoundView)).thenReturn(requestDispatcher);
        dispatcherServlet.service(mockRequest, mockResponse);

        // Then
        verify(requestDispatcher).forward(mockRequest, mockResponse);
    }

    private void setUpRequest(HttpServletRequest request, String uri, String method) {
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);
    }
}
