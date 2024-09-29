package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.mapping.HandlerMappingRegistry;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerMapping handlerMapping1;
    private HandlerMapping handlerMapping2;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMapping1 = mock(HandlerMapping.class);
        handlerMapping2 = mock(HandlerMapping.class);
    }

    @DisplayName("핸들러 반환 성공 : 핸들러매핑이 1개 등록되어 있는 경우")
    @Test
    void getHandler() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        Object object = mock(Object.class);
        when(request.getRequestURI()).thenReturn(any());
        when(handlerMapping1.handlerExist(request)).thenReturn(true);
        when(handlerMapping1.getHandler(request)).thenReturn(object);

        handlerMappingRegistry.addHandlerMapping(handlerMapping1);

        // when
        Object handler = handlerMappingRegistry.getHandler(request);

        // then
        Assertions.assertThat(handler).isEqualTo(object);
    }

    @DisplayName("핸들러 반환 성공 : 핸들러매핑이 여러 개 등록되어 있는 경우")
    @Test
    void getHandler_whenOtherHandlerMappingExist() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        Object object = mock(Object.class);

        when(request.getRequestURI()).thenReturn(any());
        when(handlerMapping1.handlerExist(request)).thenReturn(true);
        when(handlerMapping2.handlerExist(request)).thenReturn(false);
        when(handlerMapping1.getHandler(request)).thenReturn(object);

        handlerMappingRegistry.addHandlerMapping(handlerMapping1);
        handlerMappingRegistry.addHandlerMapping(handlerMapping2);

        // when
        Object handler = handlerMappingRegistry.getHandler(request);

        // then
        Assertions.assertThat(handler).isEqualTo(object);
    }

    @DisplayName("핸들러 반환 실패 : 일치하는 핸들러가 존재하지 않을 때")
    @Test
    void noHandlerExist() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(any());
        when(handlerMapping1.handlerExist(request)).thenReturn(false);

        handlerMappingRegistry.addHandlerMapping(handlerMapping1);

        // when & then
        Assertions.assertThatCode(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(NoSuchElementException.class);
    }
}
