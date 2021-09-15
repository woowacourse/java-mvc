package nextstep.mvc;

import nextstep.web.annotation.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.mvc.mapping.AnnotationHandlerMappingTest.BASE_PACKAGE;
import static org.assertj.core.api.Assertions.assertThat;


class BeanScannerTest {

    @DisplayName("Controller 어노테이션으로 컨트롤러들을 찾는다.")
    @Test
    void getBeansWithAnnotation() {
        List<Object> controllers = BeanScanner.getBeansWithAnnotation(BASE_PACKAGE, Controller.class);
        List<String> controllerNames = controllers.stream()
                .map(controller -> controller.getClass().getSimpleName())
                .collect(Collectors.toList());

        assertThat(controllerNames).contains("TestController", "Test2Controller");
    }
}