package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("어노테이션 요청 매핑 등록과 조회")
class AnnotationHandlerRegistrationTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples", "mappingfixtures");
        handlerMapping.initialize();
    }

    @DisplayName("경로와 HTTP 메서드가 일치하면 실행 대상을 찾는다")
    @ParameterizedTest(name = "[{index}] {0} {1}")
    @CsvSource({"GET, /get-test", "POST, /post-test"})
    void findsRegisteredHandler(final String method, final String path) {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(path);

        // when
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("요청에 대응하는 매핑이 없으면 실행 대상을 찾지 못한다")
    @ParameterizedTest(name = "[{index}] {0}: {1} {2}")
    @CsvSource({
            "등록되지 않은 경로, GET, /unknown",
            "일치하지 않는 HTTP 메서드, POST, /get-test",
            "지원하지 않는 HTTP 메서드, CONNECT, /get-test"
    })
    void returnsNullForUnmappedRequest(final String scenario, final String method, final String path) {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(path);

        // when
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isNull();
    }

    @DisplayName("method 설정이 없으면 모든 RequestMethod 값에 매핑한다")
    @ParameterizedTest(name = "[{index}] {0} /all-methods")
    @EnumSource(RequestMethod.class)
    void registersAllMethodsWhenUnspecified(final RequestMethod method) {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method.name());
        when(request.getRequestURI()).thenReturn("/all-methods");

        // when
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
