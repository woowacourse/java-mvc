package com.techcourse;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HandlerMappingRegistryTest {

    HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

    @DisplayName("URI에 해당하는 어노테이션 기반 핸들러를 반환한다.")
    @Test
    void getHandlerWithAnnotation() {
        handlerMappingRegistry.addHandlerMapping(0, new AnnotationHandlerMapping("com"));
        handlerMappingRegistry.init();

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getMethod()).thenReturn(RequestMethod.GET.name());
        when(request.getRequestURI()).thenReturn("/");

        Object handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
