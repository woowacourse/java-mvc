package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

    public static Stream<Arguments> provideObjWithBool() {
        return Stream.of(
                Arguments.of(mock(Controller.class), true),
                Arguments.of("String", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideObjWithBool")
    void 타입에_따라서_boolean_값을_반환한다(final Object object, final boolean expected) {
        // given, when, then
        assertThat(controllerHandlerAdapter.supports(object)).isEqualTo(expected);
    }

    @Test
    void 컨트롤러를_실행시키고_ModleAndView를_반환한다() throws Exception {
        // given
        final var controller = mock(Controller.class);
        final var request = mock(HttpServletRequest.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        final var testViewName = "test";

        given(controller.execute(any(), any()))
                .willReturn(testViewName);
        given(request.getRequestDispatcher(any()))
                .willReturn(requestDispatcher);
        willDoNothing().given(requestDispatcher)
                .forward(any(), any());

        // when
        final var modelAndView = controllerHandlerAdapter.handle(request, null, controller);

        // then
        modelAndView.getView().render(Map.of(), request, null);
        verify(request).getRequestDispatcher(testViewName);
    }
}
