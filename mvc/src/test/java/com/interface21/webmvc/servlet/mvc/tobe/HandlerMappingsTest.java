package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingsTest {

    private HandlerMappings handlerMappings;

    @BeforeEach
    void setUp() {
        handlerMappings = new HandlerMappings(List.of(new AnnotationHandlerMapping("samples.valid")));
    }

    @DisplayName("요청을 처리할 Handler가 존재하지 않으면 예외가 발생한다.")
    @Test
    void getHandler() {
        // given
        final var request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/invalid");
        when(request.getMethod()).thenReturn("GET");

        // when & then
        assertThatThrownBy(() -> handlerMappings.getHandler(request)).isInstanceOf(HandlingException.class)
                .hasMessage("요청을 처리할 핸들러가 존재하지 않습니다.");
    }
}