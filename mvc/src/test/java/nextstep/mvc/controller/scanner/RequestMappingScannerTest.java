package nextstep.mvc.controller.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMappingScannerTest {

    @Test
    @DisplayName("RequestMapping Scanner는 BasePackage를 기준으로 @RequestMapping이 사용된 모든 메서드를 찾는다.")
    void findAllMethodsOfAnnotationRequestMapping() {
        // given & when
        Set<?> methods = RequestMappingScanner
            .getInstance()
            .getAllAnnotations(AnnotationController.class);

        // then
        assertThat(methods).hasSize(1);
    }

    @Controller
    private class AnnotationController {

        @RequestMapping
        public void requestMappingMethod() {

        }

        public void notRequestMappingMethod() {

        }
    }
}
