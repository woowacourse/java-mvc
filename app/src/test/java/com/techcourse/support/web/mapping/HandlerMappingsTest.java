package com.techcourse.support.web.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerMappingsTest {

    @Test
    void 생성자는_호출하면_HandlerMappings를_초기화한다() {
        assertDoesNotThrow(HandlerMappings::new);
    }

    @Test
    void initialize_메서드는_호출하면_필요한_HandlerMapping을_등록한다() {
        final HandlerMappings handlerMappings = new HandlerMappings();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/");

        assertThatCode(handlerMappings::initialize).doesNotThrowAnyException();
    }

    @Test
    void getHander_메서드는_해당_요청을_처리할_수_있는_핸들러가_있다면_해당_핸들러를_반환한다() {
        final HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(new ManualHandlerMappingWrapper());
        handlerMappings.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/");

        final Object actual = handlerMappings.getHandler(request);

        assertThat(actual).isNotNull();
    }

    @Test
    void getHander_메서드는_해당_요청을_처리할_수_있는_핸들러가_없다면_null을_반환한다() {
        final HandlerMappings handlerMappings = new HandlerMappings();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/");

        final Object actual = handlerMappings.getHandler(request);

        assertThat(actual).isNull();
    }
}
