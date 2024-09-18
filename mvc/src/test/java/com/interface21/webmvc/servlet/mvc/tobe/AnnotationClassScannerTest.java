package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.context.stereotype.Controller;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.AnnotatedExampleController;
import samples.TestController;

class AnnotationClassScannerTest {

    @DisplayName("특정 Annotation이 달린 클래스를 반환한다.")
    @Test
    void scan() {
        AnnotationClassScanner classScanner = new AnnotationClassScanner("samples");
        Set<Class<?>> controllers = classScanner.scan(Controller.class);

        assertThat(controllers).containsExactlyInAnyOrder(AnnotatedExampleController.class, TestController.class);
    }
}
