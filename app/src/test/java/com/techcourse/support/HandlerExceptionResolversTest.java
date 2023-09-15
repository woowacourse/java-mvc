package com.techcourse.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.HandlerExceptionResolver;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.support.HandlerNotFoundExceptionResolver;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HandlerExceptionResolversTest {

    @Test
    void 지원하지_않는_예외이면_예외를_그대로_반환() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExceptionResolvers handlerExceptionResolvers = new HandlerExceptionResolvers();
        IllegalArgumentException ex = new IllegalArgumentException();

        // when
        HandlerExceptionResolver exceptionResolver = handlerExceptionResolvers.getExceptionResolver(ex);

        // then
        assertThatThrownBy(() -> exceptionResolver.resolveException(request, response, ex))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 지원하는_예외이면_성공() {
        // given
        HandlerExceptionResolvers handlerExceptionResolvers = new HandlerExceptionResolvers();
        handlerExceptionResolvers.addHandlerExceptionResolver(new HandlerNotFoundExceptionResolver("/"));
        Exception ex = new HandlerNotFoundException("");

        // when
        HandlerExceptionResolver exceptionResolver = handlerExceptionResolvers.getExceptionResolver(ex);

        // then
        assertThat(exceptionResolver)
            .isInstanceOf(HandlerNotFoundExceptionResolver.class);
    }
}
