package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingBehaviorTest {

    @Test
    void 명시한_HTTP_메서드의_매핑을_우선한다() throws Exception {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("mappingfixtures.fallback");
        handlerMapping.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/shared");
        when(request.getMethod()).thenReturn("GET");

        // when
        HandlerExecution execution = handlerMapping.getHandler(request);

        // then
        assertThat(execution.handle(request, response).getObject("source")).isEqualTo("get");
    }

    @Test
    void 메서드가_일치하지_않으면_HTTP_메서드를_지정하지_않은_매핑을_선택한다() throws Exception {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("mappingfixtures.fallback");
        handlerMapping.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/shared");
        when(request.getMethod()).thenReturn("POST");

        // when
        HandlerExecution execution = handlerMapping.getHandler(request);

        // then
        assertThat(execution.handle(request, response).getObject("source")).isEqualTo("any");
    }

    @Test
    void RequestMethod에_없는_HTTP_메서드도_처리한다() throws Exception {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("mappingfixtures.fallback");
        handlerMapping.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/shared");
        when(request.getMethod()).thenReturn("PROPFIND");

        // when
        HandlerExecution execution = handlerMapping.getHandler(request);

        // then
        assertThat(execution.handle(request, response).getObject("source")).isEqualTo("any");
    }

    @Test
    void 같은_매핑을_두_번_등록하면_예외가_발생한다() {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("mappingfixtures.duplicate");

        // when
        Throwable thrown = catchThrowable(handlerMapping::initialize);

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복된 매핑");
    }

    @Test
    void 일치하는_매핑이_없으면_null을_반환한다() {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/missing");
        when(request.getMethod()).thenReturn("GET");

        // when
        HandlerExecution execution = handlerMapping.getHandler(request);

        // then
        assertThat(execution).isNull();
    }
}
