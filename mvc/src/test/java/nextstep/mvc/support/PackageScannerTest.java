package nextstep.mvc.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.web.annotation.Controller;
import samples.TestAnnotationController;

class PackageScannerTest {

    @DisplayName("패키지 경로에 해당 annotation 이 존재하는 클래스 목록을 조회한다")
    @Test
    void readTypesAnnotatedWith() {
        final Set<Class<?>> classes = PackageScanner.readTypesAnnotatedWith(Controller.class, "samples");

        assertThat(classes).containsOnly(TestAnnotationController.class);
    }
}
