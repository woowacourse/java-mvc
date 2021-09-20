package nextstep.mvc.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ControllerScannerTest {

    @DisplayName("ControllerScanner에 패키지를 지정해서 @Controller 어노테이션이 붙은 클래스와 인스턴스를 가져올 수 있다.")
    @Test
    void getControllers() {
        // given
        String basePackage = "nextstep.mvc.controller";
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        // when
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        Object testController = controllers.get(TestController.class);
        Object test2Controller = controllers.get(Test2Controller.class);

        assertThat(controllers).hasSize(2);
        assertThat(testController).isInstanceOf(TestController.class);
        assertThat(test2Controller).isInstanceOf(Test2Controller.class);
    }

    @DisplayName("ControllerScanner의 대상이 존재하지 않는 패키지를 대상으로 하면 아무것도 가져오지 않는다. ")
    @Test
    void getControllersWithNoExistControllerBasePackage() {
        // given
        String basePackage = "nextstep.mvc.controller.tobe";
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        // when
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        assertThat(controllers).isEmpty();
    }

    @DisplayName("ControllerScanner에 존재하지 않는 패키지를 지정하면 아무것도 가져오지 않는다.")
    @Test
    void getControllersInvalidBasePackage() {
        // given
        String basePackage = "nextstep.charlie.choco";
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        // when
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        assertThat(controllers).isEmpty();
    }
}