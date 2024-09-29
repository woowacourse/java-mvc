package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class HandlerMappingRegistryTest {

    HandlerMappingRegistry handlerMappingRegistry;
    HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMapping = mock(AnnotationHandlerMapping.class);

        handlerMappingRegistry.addHandlerMappings(handlerMapping);
    }

    @Test
    @DisplayName("HandlerMappingRegistry에 해당 handlerMapping을 등록한다.")
    void addHandlerMapping() throws IllegalAccessException, NoSuchFieldException {
        //given
        HandlerMapping handlerMapping = mock(AnnotationHandlerMapping.class);

        //when
        handlerMappingRegistry.addHandlerMappings(handlerMapping);
        Field field = handlerMappingRegistry.getClass().getDeclaredField("handlerMappings");
        field.setAccessible(true);

        List<HandlerMapping> handlerMappings = (List<HandlerMapping>) field.get(handlerMappingRegistry);

        //then
        assertAll(
                () -> assertThat(handlerMappings).hasSize(2),
                () -> assertThat(handlerMappings.getLast()).isEqualTo(handlerMapping)
        );
    }

    @Test
    @DisplayName("HandlerMappingRegistry에 해당 handlerMapping을 등록하면 초기화를 진행한다.")
    void addHandlerMapping_initialize() {
        //given
        HandlerMapping handlerMapping2 = mock(AnnotationHandlerMapping.class);

        //when
        handlerMappingRegistry.addHandlerMappings(handlerMapping2);

        //then
        assertAll(
                () -> verify(handlerMapping, times(1)).initialize(),
                () -> verify(handlerMapping2, times(1)).initialize()
        );
    }

    @Test
    @DisplayName("HandlerMappingRegistry에서 해당 요청을 처리할 수 있는 handler를 찾아온다.")
    void getHandlerMapping() {
        //given
        HttpServletRequest request = new MockHttpServletRequest();
        when(handlerMapping.getHandler(request)).thenReturn(Optional.ofNullable("test"));

        //when
        Object handler = handlerMappingRegistry.getHandler(request);

        //then
        assertThat(handler).isNotNull().isEqualTo("test");
    }

    @Test
    @DisplayName("HandlerMappingRegistry에서 해당 handler를 처리할 수 있는 handlerMapping가 없다면 NoSuchElementException을 반환한다.")
    void getHandlerMapping_failWithUnsupportedHandler() {
        //given
        HttpServletRequest request = new MockHttpServletRequest();
        when(handlerMapping.getHandler(request)).thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Handler");
    }
}
