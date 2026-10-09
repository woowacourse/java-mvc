package com.techcourse.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.DispatcherServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HomeControllerTest {

    @Test
    @DisplayName("루트 요청을 어노테이션 매핑으로 찾아 홈 JSP로 포워드한다")
    void forwardsRootRequestToHome() throws Exception {
        TestDispatcherServlet servlet = initializedServlet();
        HttpServletRequest request = getRequest("/");
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.handle(request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private TestDispatcherServlet initializedServlet() {
        TestDispatcherServlet servlet = new TestDispatcherServlet();
        servlet.init();
        return servlet;
    }

    private HttpServletRequest getRequest(final String uri) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn(uri);
        return request;
    }

    private static class TestDispatcherServlet extends DispatcherServlet {

        private TestDispatcherServlet() {
            super("com.techcourse.controller");
        }

        private void handle(final HttpServletRequest request, final HttpServletResponse response)
                throws ServletException {
            super.service(request, response);
        }
    }
}
