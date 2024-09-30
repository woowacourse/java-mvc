package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RequestMappingHandlerAdapter 테스트")
class RequestMappingHandlerAdapterTest {

    private RequestMappingHandlerAdapter handlerAdapter;
    private HandlerExecution mockHandlerExecution;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        handlerAdapter = new RequestMappingHandlerAdapter();
        mockHandlerExecution = mock(HandlerExecution.class);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("HandlerExecution 지원 여부 테스트")
    void supportsHandlerExecution() {
        Assertions.assertAll(
                () -> assertThat(handlerAdapter.supports(mockHandlerExecution)).isTrue(),
                () -> assertThat(handlerAdapter.supports(new Object())).isFalse()
        );
    }

    @Test
    @DisplayName("잘못된 핸들러 타입 처리 시 예외 발생 테스트")
    void throwExceptionForInvalidHandler() {
        assertThatThrownBy(() -> handlerAdapter.handle(mockRequest, mockResponse, new Object()))
                .isInstanceOf(ClassCastException.class);
    }
}
