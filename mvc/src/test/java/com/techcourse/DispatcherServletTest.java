package com.techcourse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.handleradapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handlermapping.AnnotationHandlerMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        this.dispatcherServlet = new DispatcherServlet();

        AnnotationHandlerMapping annotationHandlerMapping= new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();

        dispatcherServlet.addHandlerMapping(annotationHandlerMapping);
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());

        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
    }

    @Test
    void service_get_request() throws Exception {
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("test/get")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(request).getRequestDispatcher("test/get");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void service_post_request() throws Exception {
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("test/post")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(request).getRequestDispatcher("test/post");
        verify(requestDispatcher).forward(request, response);
    }
}
