package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @DisplayName("핸들러 목록에서 핸들러를 찾는다.")
    @Test
    void getHandlerTest() {
        // given
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry("samples");
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isPresent();
    }

    @DisplayName("핸들러 목록에서 핸들러를 찾을 수 없는 경우 빈 Optional을 반환한다.")
    @Test
    void getHandlerNotFoundTest() {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry("samples");

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("PUT");

        // when
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isEmpty();
    }
}
