package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry("samples");
        mockRequest = mock(HttpServletRequest.class);
    }

    @Test
    @DisplayName("적합한 핸들러를 찾지 못한경우 빈 핸들러를 반환한다.")
    void getHandlerReturnEmpty() {
        setUpRequest(mockRequest, "/empty-test", "GET", "gugu");
        Optional<Object> handler = handlerMappingRegistry.getHandler(mockRequest);

        assertThat(handler).isEmpty();
    }

    @Test
    @DisplayName("등록된 핸들러 매핑이 요청을 처리할 수 있는 핸드러를 반환한다.")
    void getHandler() {
        setUpRequest(mockRequest, "/get-test", "GET", "gugu");
        Optional<Object> annotationHandler = handlerMappingRegistry.getHandler(mockRequest);

        assertThat(annotationHandler).isPresent();
    }

    private void setUpRequest(HttpServletRequest request, String uri, String method, String id) {
        when(request.getAttribute("id")).thenReturn(id);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);
    }
}
