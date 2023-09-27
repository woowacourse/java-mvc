package webmvc.org.springframework.web.servlet.mvc.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerMappingException;

class HandlerMappingsTest {

    @DisplayName("일치하는 URI, Method 의 handler 를 반환한다.")
    @Test
    void getHandler() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HandlerMappings handlerMappings = new HandlerMappings("samples");
        handlerMappings.initialize();

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        final Object handler = handlerMappings.getHandler(request);

        // then
        assertThat(handler).isNotNull()
            .isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("매칭되는 Handler 가 없다면 예외가 발생한다.")
    @Test
    void getHandler_nothingMatched() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HandlerMappings handlerMappings = new HandlerMappings("samples");
        handlerMappings.initialize();

        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("GET");

        // when, then
        assertThatThrownBy(() -> handlerMappings.getHandler(request))
            .isInstanceOf(HandlerMappingException.NotFoundException.class);
    }
}
