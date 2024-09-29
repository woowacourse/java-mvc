package com.interface21.webmvc.servlet.mvc.tobe;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestManualHandlerMapping;

public class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Test
    @DisplayName("수동으로 매핑된 요청을 받으면 Controller를 반환한다.")
    void should_return_Controller_when_get_RequestMapping_request() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");

        TestManualHandlerMapping testManualHandlerMapping = new TestManualHandlerMapping();
        testManualHandlerMapping.initialize();

        handlerMappingRegistry.addHandlerMapping(testManualHandlerMapping);

        // when
        Object handlerMapping = handlerMappingRegistry.getHandlerMapping(request).get();

        //then
        assertThat(handlerMapping).isInstanceOf(Controller.class);
    }

    @Test
    @DisplayName("@RequestMapping으로 매핑된 요청을 받으면 HandlerExecution을 반환한다.")
    void should_return_handlerExecution_when_get_RequestMapping_request() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        // when
        Object handlerMapping = handlerMappingRegistry.getHandlerMapping(request).get();

        //then
        assertThat(handlerMapping).isInstanceOf(HandlerExecution.class);
    }

    @Test
    @DisplayName("존재하지 않는 정보로 매핑된 요청을 받으면 빈 Optional 객체를 반환한다.")
    void should_return_empty_Optional_when_get_invalid_RequestMapping_request() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/invalid");
        when(request.getMethod()).thenReturn("GET");

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        // when
        Optional<Object> handlerMapping = handlerMappingRegistry.getHandlerMapping(request);

        //then
        assertTrue(handlerMapping.isEmpty());
    }
}
