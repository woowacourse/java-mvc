package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutorTest {

    private HandlerExecutor handlerExecutor;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        handlerExecutor = new HandlerExecutor();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("핸들러를 처리할 수 있다.")
    void execute() throws Exception {
        when(request.getAttribute("id")).thenReturn("pororo");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        HandlerMappingRegister register = new HandlerMappingRegister();

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        register.addHandlerMapping(annotationHandlerMapping);

        Object handler = register.getHandler(request);
        ModelAndView modelAndView = handlerExecutor.execute(handler, request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("pororo");
    }
}
