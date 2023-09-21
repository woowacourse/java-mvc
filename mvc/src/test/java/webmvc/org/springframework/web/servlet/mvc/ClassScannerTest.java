package webmvc.org.springframework.web.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import context.org.springframework.stereotype.Controller;
import java.util.Set;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ClassScannerTest {

    @Test
    void 컨트롤러_어노테이션이_있는_클래스를_찾는다() {
        final ClassScanner classScanner = new ClassScanner("samples");
        final Set<Class<?>> classes = classScanner.findClassesHasAnnotation(Controller.class);

        assertThat(classes).contains(TestController.class);
    }
}
