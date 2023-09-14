package com.techcourse.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.HandlerExceptionResolver;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerExceptionResolverNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.support.HandlerNotFoundExceptionResolver;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HandlerExceptionResolversTest {

    @Test
    void 지원하지_않는_예외이면_예외() {
        // given
        HandlerExceptionResolvers handlerExceptionResolvers = new HandlerExceptionResolvers();
        Exception ex = new Exception();

        // when & then
        assertThatThrownBy(() -> handlerExceptionResolvers.getExceptionResolver(ex))
            .isInstanceOf(HandlerExceptionResolverNotFoundException.class);
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
