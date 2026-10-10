package com.techcourse;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        final var mapping = new ManualHandlerMapping();
        mapping.initialize();
        handlerMapping = mapping;
    }

    @Test
    @DisplayName("요청 URI에 등록된 레거시 핸들러를 조회한다")
    void findsHandlerByRequestUri() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");

        assertThat(handlerMapping.getHandler(request)).isInstanceOf(ForwardController.class);
    }

    @Test
    @DisplayName("등록되지 않은 요청 URI는 null을 반환한다")
    void returnsNullForUnmappedRequest() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/unmapped");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
