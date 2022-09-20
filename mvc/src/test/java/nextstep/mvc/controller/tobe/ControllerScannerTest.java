package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @DisplayName("Contoller 애노테이션을 가진 객체의 Class파일을 읽어온다.")
    @Test
    void Controller_애노테이션을_가진_객체의_Class파일을_읽어온다() {
        Object[] basePackage = {"samples"};
        Set<Class<?>> annotatedControllers = ControllerScanner.findAnnotatedController(basePackage);

        assertThat(annotatedControllers).hasSize(1);
    }

    @DisplayName("RequestMapping 애노테이션을 가진 객체의 Method를 읽어온다.")
    @Test
    void RequestMapping_애노테이션을_가진_객체의_Method를_읽어온다() {
        Class<?> clazz = TestController.class;
        List<Method> annotatedMethods = ControllerScanner.findAnnotatedMethod(clazz);

        assertThat(annotatedMethods).hasSize(2);
    }
}
