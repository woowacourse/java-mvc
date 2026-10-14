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
    void 컨텍스트_경로를_제외하고_핸들러를_찾는다() throws Exception {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("mappingfixtures.multiple");
        handlerMapping.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/mvc/users");
        when(request.getContextPath()).thenReturn("/mvc");
        when(request.getMethod()).thenReturn("GET");

        // when
        HandlerExecution execution = handlerMapping.getHandler(request);

        // then
        assertThat(execution).isNotNull();
        assertThat(execution.handle(request, response).getObject("id")).isEqualTo("juni");
    }

    @Test
    void 컨텍스트_경로가_없어도_핸들러를_찾는다() throws Exception {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("mappingfixtures.multiple");
        handlerMapping.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/users");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // when
        HandlerExecution execution = handlerMapping.getHandler(request);

        // then
        assertThat(execution).isNotNull();
        assertThat(execution.handle(request, response).getObject("id")).isEqualTo("juni");
    }

    @Test
    void 컨텍스트_경로가_있어도_HTTP_메서드를_지정하지_않은_매핑을_찾는다() throws Exception {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("mappingfixtures.fallback");
        handlerMapping.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/mvc/shared");
        when(request.getContextPath()).thenReturn("/mvc");
        when(request.getMethod()).thenReturn("POST");

        // when
        HandlerExecution execution = handlerMapping.getHandler(request);

        // then
        assertThat(execution).isNotNull();
        assertThat(execution.handle(request, response).getObject("source")).isEqualTo("any");
    }

    @Test
    void 하나의_메서드에_등록한_GET과_POST를_모두_찾는다() throws Exception {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("mappingfixtures.multiple");
        handlerMapping.initialize();
        HttpServletRequest getRequest = mock(HttpServletRequest.class);
        HttpServletRequest postRequest = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(getRequest.getRequestURI()).thenReturn("/users");
        when(getRequest.getContextPath()).thenReturn("");
        when(getRequest.getMethod()).thenReturn("GET");
        when(postRequest.getRequestURI()).thenReturn("/users");
        when(postRequest.getContextPath()).thenReturn("");
        when(postRequest.getMethod()).thenReturn("POST");

        // when
        HandlerExecution getExecution = handlerMapping.getHandler(getRequest);
        HandlerExecution postExecution = handlerMapping.getHandler(postRequest);

        // then
        assertThat(getExecution).isNotNull().isSameAs(postExecution);
        assertThat(getExecution.handle(getRequest, response).getObject("id")).isEqualTo("juni");
        assertThat(postExecution.handle(postRequest, response).getObject("id")).isEqualTo("juni");
    }

    @Test
    void GET과_POST만_등록하면_PUT은_매핑되지_않는다() {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("mappingfixtures.multiple");
        handlerMapping.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/users");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("PUT");

        // when
        HandlerExecution execution = handlerMapping.getHandler(request);

        // then
        assertThat(execution).isNull();
    }

    @Test
    void 잘못된_컨트롤러_메서드를_등록하면_예외가_발생한다() {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("mappingfixtures.invalid");

        // when
        Throwable thrown = catchThrowable(handlerMapping::initialize);

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mappingfixtures.invalid.InvalidController")
                .hasMessageContaining("show")
                .hasMessageContaining("반환 타입은 ModelAndView");
    }

    @Test
    void 명시한_HTTP_메서드의_매핑을_우선한다() throws Exception {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("mappingfixtures.fallback");
        handlerMapping.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/shared");
        when(request.getContextPath()).thenReturn("");
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
        when(request.getContextPath()).thenReturn("");
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
        when(request.getContextPath()).thenReturn("");
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
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // when
        HandlerExecution execution = handlerMapping.getHandler(request);

        // then
        assertThat(execution).isNull();
    }
}
