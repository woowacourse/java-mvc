package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private final com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry handlerMappingRegistry = new com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry();

    @BeforeEach
    void setUp() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    @Test
    void 매핑된_어노테이션_컨트롤러가_있으면_HandlerExecution을_반환한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");

        Optional<Object> actual = handlerMappingRegistry.getHandler(request);

        assertThat(actual).isPresent()
                .get()
                .isInstanceOf(HandlerExecution.class);
    }

    @Test
    void 매핑된_레거시_컨트롤러가_있으면_Controller를_반환한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/register/view");

        Optional<Object> actual = handlerMappingRegistry.getHandler(request);

        assertThat(actual).isPresent()
                .get()
                .isInstanceOf(Controller.class);
    }

    @Test
    void 매핑된_컨트롤러가_없으면_빈_Optional을_반환한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/invalid-uri");
        when(request.getMethod()).thenReturn("GET");

        Optional<Object> actual = handlerMappingRegistry.getHandler(request);

        assertThat(actual).isEmpty();
    }
}
