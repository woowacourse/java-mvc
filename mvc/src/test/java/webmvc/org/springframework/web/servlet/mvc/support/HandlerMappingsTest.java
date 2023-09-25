package webmvc.org.springframework.web.servlet.mvc.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HandlerMappingsTest {

    @Test
    void HandlerMapping이_활성화되지_않으면_예외() {
        // given
        HandlerMappings handlerMappings = new HandlerMappings();
        HttpServletRequest request = mock(HttpServletRequest.class);

        // when & then
        assertThatThrownBy(() -> handlerMappings.getHandler(request))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void HandlerMapping이_활성화되었는데_다시_활성화_하면_예외() {
        // given
        HandlerMappings handlerMappings = new HandlerMappings();

        // when
        handlerMappings.initialize();

        // then
        assertThatThrownBy(handlerMappings::initialize)
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void HandlerMapping이_활성화되지_않을때_Handler를_찾으면_예외() {
        // given
        HandlerMappings handlerMappings = new HandlerMappings();
        HttpServletRequest request = mock(HttpServletRequest.class);

        // when & then
        assertThatThrownBy(() -> handlerMappings.getHandler(request))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void Handler를_찾을때_HttpServletRequest가_null이면_예외() {
        // given
        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.initialize();

        // when & then
        assertThatThrownBy(() -> handlerMappings.getHandler(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Handler를_찾을때_찾을수_없는_Handler이면_예외() {
        // given
        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        given(handlerMapping.getHandler(any(HttpServletRequest.class)))
            .willReturn(null);

        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI())
            .willReturn("/foo");
        given(request.getMethod())
            .willReturn("GET");

        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(handlerMapping);
        handlerMappings.initialize();

        // when & then
        assertThatThrownBy(() -> handlerMappings.getHandler(request))
            .isInstanceOf(HandlerNotFoundException.class);
    }

    @Test
    void Handler_찾기_성공() {
        // given
        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        given(handlerMapping.getHandler(any(HttpServletRequest.class)))
            .willReturn(new ForwardController("/foo"));

        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI())
            .willReturn("/foo");
        given(request.getMethod())
            .willReturn("GET");

        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(handlerMapping);
        handlerMappings.initialize();

        // when
        Object handler = handlerMappings.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(ForwardController.class);
    }
}
