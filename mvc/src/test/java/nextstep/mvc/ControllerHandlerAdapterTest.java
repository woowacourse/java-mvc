package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.Stream;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ControllerHandlerAdapterTest {

    @MethodSource("methodSource")
    @ParameterizedTest(name = " : {0}")
    void controllerMappingHandlerAdapter_supports_controller_type(String description, Object handler,
                                                                  boolean expected) {
        // given
        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

        // when
        final boolean actual = controllerHandlerAdapter.supports(handler);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> methodSource() {
        return Stream.of(
                Arguments.of(
                        "ControllerMappingHandlerAdapter는 Controller 인터페이스 타입 외엔 support 하지 않는다",
                        new HandlerExecution(null, null),
                        false
                ),
                Arguments.of(
                        "ControllerMappingHandlerAdapter는 Controller 인터페이스 타입만 support 한다",
                        new Controller() {
                            @Override
                            public String execute(final HttpServletRequest req, final HttpServletResponse res)
                                    throws Exception {
                                return null;
                            }
                        },
                        true
                ),
                Arguments.of(
                        "ControllerMappingHandlerAdapter는 Controller 인터페이스 타입 외엔 support 하지 않는다",
                        null,
                        false
                )
        );
    }
}
