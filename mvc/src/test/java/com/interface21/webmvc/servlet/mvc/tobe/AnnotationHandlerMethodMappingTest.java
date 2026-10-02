package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("HTTP 메서드별 컨트롤러 실행")
class AnnotationHandlerMethodMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("mappingfixtures");
        handlerMapping.initialize();
    }

    @Nested
    @DisplayName("같은 경로를 서로 다른 메서드가 처리할 때")
    class SamePath {

        @DisplayName("HTTP 메서드에 따라 서로 다른 컨트롤러 메서드를 실행한다")
        @ParameterizedTest(name = "[{index}] {0} /users → {1}")
        @CsvSource({"GET, show", "POST, save"})
        void executesMatchingMethod(final String method, final String expectedHandler) throws Exception {
            // given
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);
            when(request.getRequestURI()).thenReturn("/users");
            when(request.getMethod()).thenReturn(method);

            // when
            final var execution = (HandlerExecution) handlerMapping.getHandler(request);
            final var result = execution.handle(request, response);

            // then
            assertThat(result.getObject("handler")).isEqualTo(expectedHandler);
        }
    }

    @Nested
    @DisplayName("한 메서드에 여러 HTTP 메서드를 지정했을 때")
    class MultipleMethods {

        @DisplayName("지정한 GET과 POST는 동일한 컨트롤러 메서드를 실행한다")
        @ParameterizedTest(name = "[{index}] {0} /multi → shared")
        @EnumSource(value = RequestMethod.class, names = {"GET", "POST"})
        void executesSharedMethod(final RequestMethod method) throws Exception {
            // given
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);
            when(request.getRequestURI()).thenReturn("/multi");
            when(request.getMethod()).thenReturn(method.name());

            // when
            final var execution = (HandlerExecution) handlerMapping.getHandler(request);
            final var result = execution.handle(request, response);

            // then
            assertThat(result.getObject("handler")).isEqualTo("shared");
        }

        @DisplayName("지정하지 않은 HTTP 메서드는 실행 대상이 없다")
        @ParameterizedTest(name = "[{index}] {0} /multi → 매핑 없음")
        @EnumSource(value = RequestMethod.class, names = {"PUT", "DELETE"})
        void doesNotMapUnspecifiedMethod(final RequestMethod method) {
            // given
            final var request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/multi");
            when(request.getMethod()).thenReturn(method.name());

            // when
            final var handler = handlerMapping.getHandler(request);

            // then
            assertThat(handler).isNull();
        }
    }
}
