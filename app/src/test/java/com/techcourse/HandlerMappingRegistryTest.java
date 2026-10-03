package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.initialize();
        request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
    }

    @Test
    @DisplayName("어노테이션으로 등록한 요청을 HandlerExecution으로 핸들링한다")
    void handlesAnnotationRequest() {
        when(request.getRequestURI()).thenReturn("/registry-test");

        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).hasValueSatisfying(value -> assertThat(value).isInstanceOf(HandlerExecution.class));
    }

    @Test
    @DisplayName("수동으로 등록한 요청을 Controller로 핸들링한다")
    void handlesManualRequest() {
        when(request.getRequestURI()).thenReturn("/");

        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).hasValueSatisfying(value -> assertThat(value).isInstanceOf(Controller.class));
    }

    @Test
    @DisplayName("여러 핸들러 매핑이 처리할 수 있는 요청은 어노테이션 핸들러를 우선한다")
    void prioritizesAnnotationHandler() {
        when(request.getRequestURI()).thenReturn("/login/view");

        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).hasValueSatisfying(value -> assertThat(value).isInstanceOf(HandlerExecution.class));
    }

    @Test
    @DisplayName("어노테이션 매핑이 지원하지 않는 HTTP 메서드는 다음 핸들러 매핑에서 처리한다")
    void delegatesUnsupportedMethodToNextHandlerMapping() {
        when(request.getMethod()).thenReturn("CUSTOM");
        when(request.getRequestURI()).thenReturn("/login/view");

        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).hasValueSatisfying(value -> assertThat(value).isInstanceOf(Controller.class));
    }

    @Test
    @DisplayName("아무 핸들러 매핑도 처리할 수 없는 요청이면 빈 Optional을 반환한다")
    void returnsEmptyWhenNoHandlerCanHandle() {
        when(request.getRequestURI()).thenReturn("/unknown-get-uri");

        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isEmpty();
    }
}
