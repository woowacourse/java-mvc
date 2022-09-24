package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.Stream;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerExecutionHandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

    @ParameterizedTest
    @MethodSource("supportsData")
    @DisplayName("supports 메소드는 HandlerExecution 객체를 받으면 True를, 그 이외의 객체를 받으면 False를 반환한다.")
    void supports_handlerExecution(Object handler, boolean canSupport) {
        // when
        final boolean result = handlerAdapter.supports(handler);

        // then
        assertThat(result).isEqualTo(canSupport);
    }

    public static Stream<Arguments> supportsData() {
        return Stream.of(
                Arguments.of(mock(HandlerExecution.class), true),
                Arguments.of(mock(Controller.class), false)
        );
    }

    @Test
    @DisplayName("handle 메소드는 입력 받은 HandlerExecution의 handle 메소드를 실행하고 결과를 ModelAndView 객체로 반환한다.")
    void handle() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        when(handlerExecution.handle(request, response)).thenReturn(new ModelAndView(new JspView("/index.html")));

        // when
        final ModelAndView result = handlerAdapter.handle(request, response, handlerExecution);

        // then
        Assertions.assertAll(() -> {
            assertThat(result.getView()).isInstanceOf(JspView.class);
            assertThat(result.getView()).extracting("viewPath")
                    .isEqualTo("/index.html");
        });
    }
}