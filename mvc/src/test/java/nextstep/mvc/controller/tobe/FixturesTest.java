package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class FixturesTest {

    @Test
    void getMethod() {
        Fixtures fixtures = new Fixtures();
        TestAnnotationController testAnnotationController = new TestAnnotationController();
        assertThat(fixtures.getHandlerMethod(testAnnotationController, "findUserId"))
                .isNotNull();
    }

    @Test
    void getInstance() {
        Fixtures fixtures = new Fixtures();
        TestAnnotationController testAnnotationController = new TestAnnotationController();
        Method findUserId = fixtures.getHandlerMethod(testAnnotationController, "findUserId");
        assertThat(fixtures.getInstance(findUserId)).isInstanceOf(TestAnnotationController.class);
    }
}
