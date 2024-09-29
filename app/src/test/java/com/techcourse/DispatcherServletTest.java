package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestLegacyMvcController;

class DispatcherServletTest {


    @Test
    void init() {
        // given
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        DispatcherServlet dispatcherServlet = new DispatcherServlet(manualHandlerMapping, annotationHandlerMapping);

        // when & then
        assertThatCode(() -> dispatcherServlet.init())
                .doesNotThrowAnyException();
    }

    @Test
    void serviceWithLegacyMvc() throws Exception {
        // given
        ManualHandlerMapping manualHandlerMapping = spy(ManualHandlerMapping.class);
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        DispatcherServlet dispatcherServlet = new DispatcherServlet(manualHandlerMapping, annotationHandlerMapping);

        TestLegacyMvcController testLegacyMvcController = new TestLegacyMvcController();
        when(manualHandlerMapping.getHandler("/test-legacy-mvc")).thenReturn(testLegacyMvcController);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getAttribute("id")).thenReturn("mia");
        when(request.getRequestURI()).thenReturn("/test-legacy-mvc");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("")).thenReturn(requestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(manualHandlerMapping).getHandler("/test-legacy-mvc");
    }

    @Test
    void serviceWithAnnotationMvc() throws Exception {
        // given
        ManualHandlerMapping manualHandlerMapping = spy(new ManualHandlerMapping());
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        DispatcherServlet dispatcherServlet = new DispatcherServlet(manualHandlerMapping, annotationHandlerMapping);
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
        verify(manualHandlerMapping, never()).getHandler("/test-annotation-mvc");
    }
}
