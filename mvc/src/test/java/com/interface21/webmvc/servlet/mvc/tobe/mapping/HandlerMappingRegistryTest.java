package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HandlerMappingRegistry 테스트")
class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerMapping mockHandlerMapping1;
    private HandlerMapping mockHandlerMapping2;
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        mockHandlerMapping1 = mock(HandlerMapping.class);
        mockHandlerMapping2 = mock(HandlerMapping.class);
        mockRequest = mock(HttpServletRequest.class);
    }

    @Test
    @DisplayName("지원하는 핸들러 반환 테스트")
    void returnCorrectHandler() {
        Object expectedHandler = new Object();
        when(mockHandlerMapping1.getHandler(mockRequest)).thenReturn(null);
        when(mockHandlerMapping2.getHandler(mockRequest)).thenReturn(expectedHandler);

        handlerMappingRegistry.addHandlerMapping(mockHandlerMapping1);
        handlerMappingRegistry.addHandlerMapping(mockHandlerMapping2);

        Object result = handlerMappingRegistry.getHandler(mockRequest);
        assertThat(result).isEqualTo(expectedHandler);
    }

    @Test
    @DisplayName("지원하는 핸들러가 없을 때 예외 발생 테스트")
    void throwExceptionIfNoHandler() {
        when(mockHandlerMapping1.getHandler(mockRequest)).thenReturn(null);
        when(mockHandlerMapping2.getHandler(mockRequest)).thenReturn(null);

        handlerMappingRegistry.addHandlerMapping(mockHandlerMapping1);
        handlerMappingRegistry.addHandlerMapping(mockHandlerMapping2);

        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(mockRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("요청을 처리할 수 있는 핸들러 매핑 정보가 없습니다");
    }
}
