package webmvc.org.springframework.web.servlet.mvc.supports.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import context.org.springframework.stereotype.Controller;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ClassScannerTest {

    @Test
    void 생성자는_호출하면_ClassScanner를_초기화한다() {
        assertDoesNotThrow(() -> new ClassScanner(new String[0]));
    }

    @Test
    void findInstanceByAnnotation_메서드는_호출하면_전달한_어노테이션이_있는_클래스의_인스턴스를_반환한다() {
        final ClassScanner classScanner = new ClassScanner(new String[]{"samples"});

        final List<Object> actual = classScanner.findInstanceByAnnotation(Controller.class);

        assertThat(actual).hasSize(1);
    }
}
