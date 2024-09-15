package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Controller
    static class DummyController {
        @RequestMapping(value = "/test", method = RequestMethod.GET)
        public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
            return null;
        }

        @RequestMapping(value = "/noRequesetMappingValueSet")
        public ModelAndView noRequestMappingValueSet(HttpServletRequest request, HttpServletResponse response) {
            return null;
        }
    }

    @Test
    @DisplayName("Controller 어노테이션을 찾아, RequestMapping이 존재하는 메서드를 핸들러로 등록한다.")
    void registerHandler() {
        String basePackage = "com.interface21.webmvc.servlet.mvc.tobe";
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        annotationHandlerMapping.initialize();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getMethod()).thenReturn("GET");

        Object handler = annotationHandlerMapping.getHandler(request);
        assertThat(handler).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    @DisplayName("RequestMapping 어노테이션의 value가 비어있는 경우, 모든 메서드를 등록한다")
    void registerHandlerWithNoRequestMappingValue(RequestMethod method) {
        String basePackage = "com.interface21.webmvc.servlet.mvc.tobe";
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        annotationHandlerMapping.initialize();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/noRequesetMappingValueSet");
        when(request.getMethod()).thenReturn(method.name());

        Object handler = annotationHandlerMapping.getHandler(request);
        assertThat(handler).isNotNull();
    }

}
