package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.Stream;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ManualHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ManualHandlerAdapterTest {

    final ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

    @ParameterizedTest
    @MethodSource("supportsData")
    @DisplayName("supports 메소드는 Controller 인터페이스 기반의 객체를 받으면 True를, 그 이외의 객체를 받으면 False를 반환한다.")
    void supports_handlerExecution(Object handler, boolean canSupport) {
        // when
        final boolean result = manualHandlerAdapter.supports(handler);

        // then
        assertThat(result).isEqualTo(canSupport);
    }

    public static Stream<Arguments> supportsData() {
        return Stream.of(
                Arguments.of(mock(Controller.class), true),
                Arguments.of(mock(HandlerExecution.class), false)
        );
    }

    @Test
    @DisplayName("handle 메소드는 입력 받은 컨트롤러의 execute 메소드를 실행하고 결과를 ModelAndView 객체로 반환한다.")
    void handle() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Controller controller = mock(Controller.class);

        when(controller.execute(request, response)).thenReturn("/index.html");

        // when
        final ModelAndView result = manualHandlerAdapter.handle(request, response, controller);

        // then
        Assertions.assertAll(() -> {
            assertThat(result.getView()).isInstanceOf(JspView.class);
            assertThat(result.getView()).extracting("viewPath")
                    .isEqualTo("/index.html");
        });
    }
}