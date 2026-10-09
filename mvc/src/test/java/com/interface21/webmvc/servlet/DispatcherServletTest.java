package com.interface21.webmvc.servlet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    @DisplayName("어노테이션 핸들러가 반환한 모델을 요청 속성에 담아 뷰로 포워드한다")
    void handlesAnnotationRequest() throws Exception {
        HttpServletRequest request = getRequest("/registry-test");
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/registry-test.jsp")).thenReturn(requestDispatcher);
        DispatcherServlet dispatcherServlet = initializedDispatcherServlet();

        dispatcherServlet.service(request, response);

        verify(request).setAttribute("name", "gugu");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("같은 URL의 GET과 POST 요청을 서로 다른 어노테이션 핸들러로 처리한다")
    void handlesRequestsByHttpMethod() throws Exception {
        HttpServletRequest getRequest = getRequest("/registry-test");
        HttpServletRequest postRequest = mock(HttpServletRequest.class);
        when(postRequest.getMethod()).thenReturn("POST");
        when(postRequest.getRequestURI()).thenReturn("/registry-test");
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher getRequestDispatcher = mock(RequestDispatcher.class);
        RequestDispatcher postRequestDispatcher = mock(RequestDispatcher.class);
        when(getRequest.getRequestDispatcher("/registry-test.jsp")).thenReturn(getRequestDispatcher);
        when(postRequest.getRequestDispatcher("/registry-post-test.jsp")).thenReturn(postRequestDispatcher);
        DispatcherServlet dispatcherServlet = initializedDispatcherServlet();

        dispatcherServlet.service(getRequest, response);
        dispatcherServlet.service(postRequest, response);

        verify(getRequest).setAttribute("name", "gugu");
        verify(getRequestDispatcher).forward(getRequest, response);
        verify(postRequest).setAttribute("name", "post-gugu");
        verify(postRequestDispatcher).forward(postRequest, response);
    }

    @Test
    @DisplayName("아무 핸들러도 처리할 수 없는 요청이면 404 오류를 응답한다")
    void respondsNotFoundWhenNoHandlerCanHandle() throws Exception {
        HttpServletRequest request = getRequest("/unknown-get-uri");
        HttpServletResponse response = mock(HttpServletResponse.class);
        DispatcherServlet dispatcherServlet = initializedDispatcherServlet();

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private DispatcherServlet initializedDispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet("dispatcherfixtures");
        dispatcherServlet.init();
        return dispatcherServlet;
    }

    private HttpServletRequest getRequest(final String requestUri) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn(requestUri);
        return request;
    }
}
