package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.tobe.Handler;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerMappingTest {

    private ManualHandler testHandler;
    private ConcurrentMap<String, ManualHandler> handlers;
    private ManualHandlerMapping manualHandlerMapping;

    @BeforeEach
    void setUp() {
        testHandler = new ManualHandler((req, res) -> "test-get");
        handlers = new ConcurrentHashMap<>(Map.of("/get-test", testHandler));
        manualHandlerMapping = new ManualHandlerMapping(handlers);
    }

    @Test
    @DisplayName("요청에 해당하는 Controller를 반환한다.")
    void getHandler() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Handler actual = manualHandlerMapping.getHandler(request);

        assertThat(actual).isEqualTo(testHandler);
    }

    @Test
    @DisplayName("요청에 해당하는 Controller가 없으면 null을 반환한다.")
    void getHandlerWhenNoController() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/no-controller");
        when(request.getMethod()).thenReturn("GET");

        Handler actual = manualHandlerMapping.getHandler(request);

        assertThat(actual).isNull();
    }

    @Test
    @DisplayName("요청에 해당하는 Controller가 있는지 확인한다.")
    void canHandle() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        boolean actual = manualHandlerMapping.canHandle(request);

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("요청에 해당하는 Controller가 없으면 false를 반환한다.")
    void canNotHandle() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/no-controller");
        when(request.getMethod()).thenReturn("GET");

        boolean actual = manualHandlerMapping.canHandle(request);

        assertThat(actual).isFalse();
    }
}
