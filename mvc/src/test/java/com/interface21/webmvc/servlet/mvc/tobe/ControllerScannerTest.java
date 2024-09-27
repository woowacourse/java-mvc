package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.NoConstructorController;
import samples.TestController;

class ControllerScannerTest {

    private static final String TEST_PACKAGE_NAME = "samples";

    @DisplayName("컨트롤러가 의존성과 함께 생성되어야 한다.")
    @Test
    void getControllers_ShouldReturnControllerWithDependencies_WhenControllerHasServiceDependency() throws Exception {
        // given & when
        ControllerScanner scanner = new ControllerScanner(TEST_PACKAGE_NAME);
        Map<Class<?>, Object> controllers = scanner.getControllers();

        // then
        assertAll(() -> {
            assertThat(controllers).containsKey(TestController.class);
            assertThat(controllers).containsKey(NoConstructorController.class);
            TestController controller = (TestController) controllers.get(TestController.class);
            assertThat(controller.getTestServiceA()).isNotNull();
            assertThat(controller.getTestServiceA().getIdentifier()).isEqualTo("TestService A");
            assertThat(controller.getTestServiceB()).isNotNull();
            assertThat(controller.getTestServiceB().getIdentifier()).isEqualTo("TestService B");
            assertThat(controller.sayHello()).isEqualTo("Hello from Controller");
        });
    }

    @Test
    @DisplayName("컨트롤러가 없을 경우 빈 맵이 반환되어야 한다.")
    void getControllers_ShouldReturnEmptyMap_WhenNoControllersPresent() throws Exception {
        // given
        String basePackage = "nonexistent.package";

        // when
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = scanner.getControllers();

        // then
        assertThat(controllers).isEmpty();
    }
}
