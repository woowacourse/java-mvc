package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class HandlerMappingRegistryTest {

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

    @DisplayName("handlerMappingRegistry에 HandlerMapping을 등록하면 HandlerMapping을 초기화한다.")
    @Test
    void addHandlerMapping() {
        // given
        AnnotationHandlerMapping annotationHandlerMapping = mock(AnnotationHandlerMapping.class);

        // when
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        // then
        verify(annotationHandlerMapping, times(1)).initialize();
    }

    @DisplayName("request에 맞는 Handler를 찾는다.")
    @Test
    void getHandler() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        AnnotationHandlerMapping annotationHandlerMapping = mock(AnnotationHandlerMapping.class);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        given(annotationHandlerMapping.getHandler(request)).willReturn(new Object());

        // when
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isNotEmpty();
    }

    @DisplayName("request에 맞는 Handler를 찾지 못하면 빈 Optional을 반환한다.")
    @Test
    void getHandler_IsEmpty() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        AnnotationHandlerMapping annotationHandlerMapping = mock(AnnotationHandlerMapping.class);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        given(annotationHandlerMapping.getHandler(request)).willReturn(null);

        // when
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isEmpty();
    }
}
