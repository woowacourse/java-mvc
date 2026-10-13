package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void 같은_URL과_HTTP_메서드에_핸들러가_중복_매핑되면_예외가_발생한다() {
        final var duplicateHandlerMapping = new AnnotationHandlerMapping("fixture.duplicate");

        assertThatThrownBy(duplicateHandlerMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복된 요청 매핑입니다");
    }

    @Test
    void public이_아닌_메서드에_RequestMapping이_붙어있으면_예외가_발생한다() {
        final var nonPublicHandlerMapping = new AnnotationHandlerMapping("fixture.nonpublic");

        assertThatThrownBy(nonPublicHandlerMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("public이어야 합니다");
    }
}
