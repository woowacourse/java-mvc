package com.interface21.webmvc.servlet;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        this.dispatcherServlet = new DispatcherServlet();

        final var annotationHandlerMapping = new AnnotationHandlerMapping(TestController.class.getPackage().getName());
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

    @Controller
    public static class TestController {

        @RequestMapping(value = "/get-test", method = RequestMethod.GET)
        public ModelAndView getTest(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView("test/get"));
        }

        @RequestMapping(value = "/post-test", method = RequestMethod.POST)
        public ModelAndView postTest(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView("test/post"));
        }
    }
}
