package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ManualHandlerAdapterTest {

    @DisplayName("핸들러를 처리한다.")
    @Test
    void handle() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ManualHandlerAdapter handlerAdapter = new ManualHandlerAdapter();
        Controller controller = new ForwardController("/index.jsp");

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        assertThatCode(() -> handlerAdapter.handle(controller, request, response))
                .doesNotThrowAnyException();
    }


    @DisplayName("핸들러를 처리할 수 있는지 판별한다.")
    @ParameterizedTest
    @MethodSource("randomHandlers")
    void canHandle(Object handler, boolean expected) {
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        boolean result = manualHandlerAdapter.canHandle(handler);

        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> randomHandlers() {
        return Stream.of(
                Arguments.of(new ForwardController("/index.jsp"), true),
                Arguments.of(new LogoutController(), true),
                Arguments.of(new RegisterViewController(), true),
                Arguments.of(new RegisterController(), true),
                Arguments.of(new HandlerExecution(ManualHandlerAdapter.class.getEnclosingMethod()), false),
                Arguments.of("1234", false)
        );
    }
}
