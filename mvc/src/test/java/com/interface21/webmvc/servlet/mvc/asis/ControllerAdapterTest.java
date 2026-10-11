package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("기존 Controller 실행 어댑터")
class ControllerAdapterTest {

    private final HandlerAdapter adapter = new ControllerAdapter();

    @Nested
    @DisplayName("지원 여부 확인")
    class Supports {

        @Test
        @DisplayName("Controller 객체를 지원한다")
        void supportsController() {
            // given
            final var controller = mock(Controller.class);

            // when
            final var supported = adapter.supports(controller);

            // then
            assertThat(supported).isTrue();
        }

        @DisplayName("Controller가 아닌 실행 대상은 지원하지 않는다")
        @ParameterizedTest(name = "[{index}] 실행 대상={0}")
        @MethodSource("com.interface21.webmvc.servlet.mvc.asis.ControllerAdapterTest#unsupportedHandlers")
        void rejectsUnsupportedHandler(final String scenario, final Object handler) {
            // when
            final var supported = adapter.supports(handler);

            // then
            assertThat(supported).isFalse();
        }
    }

    @Nested
    @DisplayName("기존 Controller 실행")
    class Handling {

        private Controller controller;
        private HttpServletRequest request;
        private HttpServletResponse response;

        @BeforeEach
        void setUp() {
            controller = mock(Controller.class);
            request = mock(HttpServletRequest.class);
            response = mock(HttpServletResponse.class);
        }

        @DisplayName("반환된 화면 이름을 JSP 전달이 가능한 View로 감싼다")
        @ParameterizedTest(name = "[{index}] 반환한 JSP 경로={0}")
        @ValueSource(strings = {"/index.jsp", "/login.jsp"})
        void preservesJspForward(final String viewName) throws Exception {
            // given
            when(controller.execute(request, response)).thenReturn(viewName);
            final var dispatcher = mock(RequestDispatcher.class);
            when(request.getRequestDispatcher(viewName)).thenReturn(dispatcher);

            // when
            final var result = adapter.handle(request, response, controller);
            result.getView().render(result.getModel(), request, response);

            // then
            verify(dispatcher).forward(request, response);
        }

        @DisplayName("반환된 redirect: 화면 이름의 리다이렉트 동작을 유지한다")
        @ParameterizedTest(name = "[{index}] 리다이렉트 목적지={0}")
        @ValueSource(strings = {"/index.jsp", "/login"})
        void preservesRedirect(final String location) throws Exception {
            // given
            when(controller.execute(request, response)).thenReturn("redirect:" + location);

            // when
            final var result = adapter.handle(request, response, controller);
            result.getView().render(result.getModel(), request, response);

            // then
            verify(response).sendRedirect(location);
        }

        @Test
        @DisplayName("화면 렌더링은 하지 않고 실행 결과만 반환한다")
        void doesNotRenderDuringHandling() throws Exception {
            // given
            when(controller.execute(request, response)).thenReturn("redirect:/index.jsp");

            // when
            adapter.handle(request, response, controller);

            // then
            verifyNoInteractions(request, response);
        }

        @Test
        @DisplayName("기존 Controller의 결과에는 별도 모델을 추가하지 않는다")
        void returnsEmptyModel() throws Exception {
            // given
            when(controller.execute(request, response)).thenReturn("/index.jsp");

            // when
            final var result = adapter.handle(request, response, controller);

            // then
            assertThat(result.getModel()).isEmpty();
        }

        @Test
        @DisplayName("Controller에서 발생한 예외를 그대로 전달한다")
        void propagatesControllerFailure() throws Exception {
            // given
            final var failure = new IllegalStateException("controller failed");
            when(controller.execute(request, response)).thenThrow(failure);

            // when & then
            assertThatThrownBy(() -> adapter.handle(request, response, controller))
                    .isSameAs(failure);
        }

        @DisplayName("지원하지 않는 객체의 실행을 요청하면 명확한 오류를 던진다")
        @ParameterizedTest(name = "[{index}] 실행 대상={0}")
        @MethodSource("com.interface21.webmvc.servlet.mvc.asis.ControllerAdapterTest#unsupportedHandlers")
        void failsForUnsupportedHandler(final String scenario, final Object handler) {
            // when & then
            assertThatThrownBy(() -> adapter.handle(request, response, handler))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Controller 타입");
        }
    }

    private static Stream<Arguments> unsupportedHandlers() {
        return Stream.of(
                Arguments.of("HandlerExecution 객체", mock(HandlerExecution.class)),
                Arguments.of("일반 Object 객체", new Object()),
                Arguments.of("null", null)
        );
    }
}
