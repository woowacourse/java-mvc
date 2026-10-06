package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("컨트롤러 메서드 규약 검증")
class HandlerExecutionValidationTest {

    @Nested
    @DisplayName("인자 검증")
    class Parameters {

        @DisplayName("요청·응답 인자의 개수와 타입과 순서가 정확하지 않으면 거부한다")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionValidationTest#invalidParameterMethods")
        void rejectsInvalidParameters(final String scenario, final Method method) {
            // given
            final var controller = new SignatureController();

            // when & then
            assertThatThrownBy(() -> new HandlerExecution(controller, method))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("인자", method.getName());
        }
    }

    @Nested
    @DisplayName("반환 타입 검증")
    class ReturnType {

        @DisplayName("ModelAndView를 반환하지 않는 메서드는 거부한다")
        @ParameterizedTest(name = "[{index}] 메서드={0}")
        @ValueSource(strings = {"returnsString", "returnsObject", "returnsVoid"})
        void rejectsInvalidReturnType(final String methodName) throws NoSuchMethodException {
            // given
            final var controller = new SignatureController();
            final var method = SignatureController.class.getMethod(
                    methodName, HttpServletRequest.class, HttpServletResponse.class);

            // when & then
            assertThatThrownBy(() -> new HandlerExecution(controller, method))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("반환 타입", methodName);
        }

        @Test
        @DisplayName("ModelAndView의 하위 타입도 반환 타입으로 허용한다")
        void acceptsModelAndViewSubtype() throws NoSuchMethodException {
            // given
            final var controller = new SignatureController();
            final var method = SignatureController.class.getMethod(
                    "returnsSubtype", HttpServletRequest.class, HttpServletResponse.class);

            // when & then
            assertThatCode(() -> new HandlerExecution(controller, method))
                    .doesNotThrowAnyException();
        }
    }

    private static Stream<Arguments> invalidParameterMethods() throws NoSuchMethodException {
        final var type = SignatureController.class;
        return Stream.of(
                Arguments.of("인자 없음", type.getMethod("withoutParameters")),
                Arguments.of("응답 인자 누락", type.getMethod("onlyRequest", HttpServletRequest.class)),
                Arguments.of("요청·응답 순서 뒤바뀜", type.getMethod(
                        "reversed", HttpServletResponse.class, HttpServletRequest.class)),
                Arguments.of("요청 인자의 타입이 String", type.getMethod(
                        "wrongType", String.class, HttpServletResponse.class)),
                Arguments.of("불필요한 세 번째 인자", type.getMethod(
                        "extraArgument", HttpServletRequest.class, HttpServletResponse.class, String.class))
        );
    }

    public static class SignatureController {

        public ModelAndView withoutParameters() {
            return null;
        }

        public ModelAndView onlyRequest(final HttpServletRequest request) {
            return null;
        }

        public ModelAndView reversed(final HttpServletResponse response, final HttpServletRequest request) {
            return null;
        }

        public ModelAndView wrongType(final String request, final HttpServletResponse response) {
            return null;
        }

        public ModelAndView extraArgument(
                final HttpServletRequest request, final HttpServletResponse response, final String extra) {
            return null;
        }

        public String returnsString(final HttpServletRequest request, final HttpServletResponse response) {
            return "view";
        }

        public Object returnsObject(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView("/user.jsp"));
        }

        public void returnsVoid(final HttpServletRequest request, final HttpServletResponse response) {
        }

        public ExtendedModelAndView returnsSubtype(
                final HttpServletRequest request, final HttpServletResponse response) {
            return new ExtendedModelAndView();
        }
    }

    public static class ExtendedModelAndView extends ModelAndView {

        public ExtendedModelAndView() {
            super(new JspView("/user.jsp"));
        }
    }
}
