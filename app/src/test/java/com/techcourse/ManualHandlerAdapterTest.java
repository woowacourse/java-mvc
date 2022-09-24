package com.techcourse;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import java.util.stream.Stream;
import nextstep.mvc.controller.asis.Controller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ManualHandlerAdapterTest {

    private ControllerHandlerAdapter manualHandlerAdapter = new ControllerHandlerAdapter();

    @DisplayName("ControllerHandlerAdapter 에 인스턴스 값을 확인한다.")
    @ParameterizedTest(name = "{index} {1} ")
    @MethodSource("controllerArguments")
    void supports(Controller controller, String message) {

        final boolean expected = manualHandlerAdapter.supports(controller);
        // then
        Assertions.assertTrue(expected);
    }

    static Stream<Arguments> controllerArguments() {
        return Stream.of(
                arguments(new LoginController(), " LoginController 타입 확인 "),
                arguments(new LoginViewController(), " LoginViewController 타입 확인 "),
                arguments(new RegisterController(), " RegisterController 타입 확인 "),
                arguments(new RegisterViewController(), " RegisterViewController 타입 확인 "),
                arguments(new LogoutController(), " LogoutController 타입 확인 "));
    }
}
