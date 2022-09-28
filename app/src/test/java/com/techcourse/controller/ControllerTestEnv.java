package com.techcourse.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.AnnotationHandlerMapping;
import nextstep.mvc.controller.HandlerExecutionHandlerAdapter;

public class ControllerTestEnv {

    private final DispatcherServlet dispatcherServlet;

    public ControllerTestEnv() {
        this.dispatcherServlet = new DispatcherServlet();
    }

    public void init() {
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
    }

    public HttpServletRequest getRequestOf(final String requestUri, final String httpMethod) {
        return getRequestOf(requestUri, httpMethod, new HashMap<>());
    }

    public HttpServletRequest getRequestOf(final String requestUri, final String httpMethod,
                                           final Map<String, String> parameter) {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn(requestUri);
        when(request.getMethod()).thenReturn(httpMethod);
        when(request.getRequestDispatcher(any())).thenReturn(mock(RequestDispatcher.class));
        setRequestParameter(request, parameter);

        return request;
    }

    private void setRequestParameter(final HttpServletRequest request, final Map<String, String> parameter) {
        for (String key : parameter.keySet()) {
            when(request.getParameter(key)).thenReturn(parameter.get(key));
        }
    }

    public HttpServletResponse getResponse() {
        return mock(HttpServletResponse.class);
    }

    public void setRequestSessionForLogin(final HttpServletRequest request) {
        final HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("user")).thenReturn(null);
    }

    public void setRequestSessionForLogout(final HttpServletRequest request) {
        final HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(httpSession);
        doNothing().when(httpSession).removeAttribute("user");
    }

    public void setWriterForJsonResponse(final HttpServletResponse response) throws IOException {
        when(response.getWriter()).thenReturn(mock(PrintWriter.class));
    }

    public void sendRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        dispatcherServlet.service(request, response);
    }

    public void verifyRequestForwardTo(final HttpServletRequest request, final String viewPath) {
        verify(request, times(1)).getRequestDispatcher(viewPath);
    }

    public void verifyResponseSendRedirectTo(final HttpServletResponse response, final String location)
            throws IOException {
        verify(response, times(1)).sendRedirect(location);
    }

    public void verifyResponseWrite(final HttpServletResponse response, final String body) throws IOException {
        verify(response.getWriter(), times(1)).write(body);
    }
}
