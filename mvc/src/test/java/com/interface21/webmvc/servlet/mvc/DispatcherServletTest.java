package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    void init() {
        // given
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        DispatcherServlet dispatcherServlet = new DispatcherServlet(annotationHandlerMapping);

        // when & then
        assertThatCode(() -> dispatcherServlet.init())
                .doesNotThrowAnyException();
    }

    @Test
    void serviceWithAnnotationMvc() throws Exception {
        // given
        AnnotationHandlerMapping annotationHandlerMapping = spy(new AnnotationHandlerMapping("samples"));
        DispatcherServlet dispatcherServlet = new DispatcherServlet(annotationHandlerMapping);
        dispatcherServlet.init();

        HttpServletRequest request = spy(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getAttribute("id")).thenReturn("mia");
        when(request.getRequestURI()).thenReturn("/test-annotation-mvc");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("")).thenReturn(requestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(annotationHandlerMapping).getHandler(request);
    }
}
