package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import samples.TestController;

class ControllerScannerTest {

    @DisplayName("@Controller 어노테이션이 붙은 클래스를 인스턴스화하여 반환한다.")
    @Test
    void scanAndInstantiateController() {
        // given
        ConfigurationBuilder configBuilder = new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("samples"))
                .filterInputsBy(new FilterBuilder().includePackage("samples"));
        Reflections reflections = new Reflections(configBuilder);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);

        // when
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        assertThat(controllers).containsKey(TestController.class);
    }

    @DisplayName("@Controller 어노테이션이 붙은 클래스를 인스턴스화 할 수 없는 경우 예외가 발생한다.")
    @Test
    void throwsWhenCannotInstantiateController() {
        // given
        Reflections reflections = new Reflections("errorsamples");
        ControllerScanner controllerScanner = new ControllerScanner(reflections);

        // when & then
        assertThatThrownBy(controllerScanner::getControllers)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("컨트롤러를 인스턴스화 할 수 없습니다.");
    }
}
