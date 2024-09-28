package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    @DisplayName("핸들러 반환 성공 : 핸들러매핑이 1개 등록되어 있는 경우")
    @Test
    void getHandler() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login");

        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);

        // when
        Object handler = handlerMappingRegistry.getHandler(request);

        // then
        Assertions.assertThat(handler).isInstanceOf(LoginController.class);
    }

    @DisplayName("핸들러 반환 성공 : 핸들러매핑이 여러 개 등록되어 있는 경우")
    @Test
    void getHandler_whenOtherHandlerMappingExist() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        HandlerMapping annotationHandlerMapping = mock(AnnotationHandlerMapping.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        when(annotationHandlerMapping.handlerExist(request)).thenReturn(true);
        when(annotationHandlerMapping.getHandler(request)).thenReturn(handlerExecution);

        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        // when
        Object handler = handlerMappingRegistry.getHandler(request);

        // then
        Assertions.assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
