package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.controller.HandlerExecutionHandlerAdapter;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import samples.ManualTestController;
import samples.TestController;

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

    public static Stream<Arguments> supportsData() throws NoSuchMethodException {
        return Stream.of(
                Arguments.of(getHandlerExecution(), true),
                Arguments.of(new ManualTestController(), false)
        );
    }

    private static HandlerExecution getHandlerExecution() throws NoSuchMethodException {
        final TestController testController = new TestController();
        final Method method = testController.getClass()
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        return new HandlerExecution(testController, method);
    }

    @Test
    @DisplayName("handle 메소드는 입력 받은 HandlerExecution의 handle 메소드를 실행하고 결과를 ModelAndView 객체로 반환한다.")
    void handle() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        HandlerExecution handlerExecution = getHandlerExecution();

        // when
        final ModelAndView result = handlerAdapter.handle(request, response, handlerExecution);

        // then
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertAll(() -> {
            assertThat(result.getView()).isEqualTo(modelAndView.getView());
            assertThat(result.getModel()).containsEntry("id", "gugu");
        });
    }
}