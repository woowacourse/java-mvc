package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class ClassScannerTest {

    private ClassScanner classScanner;

    @BeforeEach
    void setUp() {
        classScanner = new ClassScanner("samples");
    }

    @DisplayName("경로에 있는 클래스 중 해당 어노테이션을 가지고 있는 클래스를 전부 반환")
    @Test
    void scanAllObjectsWithAnnotation() {
        final Set<Object> objects = classScanner.scanAllObjectsWithAnnotation(Controller.class);

        assertThat(objects).hasSize(1);
        assertThat(objects.toArray()[0]).isExactlyInstanceOf(TestAnnotationController.class);
    }

    @DisplayName("객체가 가지고 있는 메서드 중 해당 어노테이션을 가지고 있는 것을 전부 반환")
    @Test
    void scanAllMethodsWithAnnotation() {
        final TestAnnotationController controller = new TestAnnotationController();
        final Set<Method> methods = classScanner.scanAllMethodsWithAnnotation(controller, RequestMapping.class);

        assertThat(methods).hasSize(2);
        assertThat(methods.stream().map(Method::getName).collect(Collectors.toList())).contains("findUserId", "save");
    }
}