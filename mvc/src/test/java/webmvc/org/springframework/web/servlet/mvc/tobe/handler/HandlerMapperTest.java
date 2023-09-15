package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerNotFoundException;

class HandlerMapperTest {

    @Test
    @DisplayName("Request에 매핑되는 Handler를 반환한다.")
    void getHandler() throws Exception {
        // given
        HandlerMapper handlerMapper = new HandlerMapper();
        handlerMapper.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMapper.init();

        final var request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        Object handler = handlerMapper.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @Test
    @DisplayName("Request에 매핑되는 Handler가 존재하지 않으면 예외가 발생한다.")
    void throws_HandlerNotFound() throws Exception {
        // given
        HandlerMapper handlerMapper = new HandlerMapper();
        handlerMapper.init();

        final var request = mock(HttpServletRequest.class);

        // when
        assertThatThrownBy(() -> handlerMapper.getHandler(request))
                .isInstanceOf(HandlerNotFoundException.class)
                .hasMessage("매핑될 핸들러가 존재하지 않습니다.");
    }
}
