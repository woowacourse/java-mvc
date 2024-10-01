package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingsTest {

    private HandlerMappings handlerMappings;

    @BeforeEach
    void setUp() {
        handlerMappings = new HandlerMappings();
    }

    @DisplayName("맵핑되지 않은 url 요청 시 예외가 발생한다.")
    @Test
    void getHandlerWithInvalidHandlerKey() throws Exception {
        // given
        handlerMappings.addHandlerMappings(new AnnotationHandlerMapping("samples"));
        handlerMappings.init();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/invalid");
        when(request.getMethod()).thenReturn("GET");

        // when & then
        assertThatThrownBy(() -> handlerMappings.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("처리할 수 없는 요청입니다.");
    }

}
