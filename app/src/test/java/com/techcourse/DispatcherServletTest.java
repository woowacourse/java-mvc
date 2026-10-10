package com.techcourse;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    @Test
    @DisplayName("동일한 요청이 들어오면 AnnotationHandler가 먼저 적용된다")
    void annotationHandlerFirst() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/");
        when(request.getServletPath()).thenReturn("/");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        final var dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/annotation-handler");
    }

    @Controller
    public static class AnnotationController {

        @RequestMapping(value = "/", method = RequestMethod.GET)
        public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView("redirect:/annotation-handler"));
        }
    }
}
