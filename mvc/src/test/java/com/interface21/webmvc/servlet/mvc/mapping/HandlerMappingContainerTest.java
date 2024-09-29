package com.interface21.webmvc.servlet.mvc.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.HandlerExecution;

class HandlerMappingContainerTest {

    @Test
    @DisplayName("Legacy MVC 핸들러 매핑 실행 : AnnotationHandlerMapping")
    void getHandlerWithAnnotationMVC() {
        HandlerMappingContainer container = new HandlerMappingContainer("com");
        container.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");

        Object handler = container.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @Test
    @DisplayName("Legacy MVC 핸들러 매핑 실행 : 해당 요청을 처리하는 HandlerMapping이 없음")
    void getHandlerFail() {
        HandlerMappingContainer container = new HandlerMappingContainer("com");
        container.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/notExists");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> container.getHandler(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 요청을 지원하는 HandlerMapping을 찾을 수 없습니다: " + request);

    }
}
