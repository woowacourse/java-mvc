package web.org.springframework.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static web.org.springframework.web.bind.annotation.HttpMappings.isAnyMatch;

import context.org.springframework.stereotype.Controller;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestController;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HttpMappingsTest {

    @Test
    void 입력받은_애너테이션이_매핑_애너테이션이라면_true를_반환한다() {
        // given
        final RequestMapping annotation = TestController.class.getDeclaredAnnotation(RequestMapping.class);

        // expect
        assertThat(isAnyMatch(annotation)).isEqualTo(true);
    }

    @Test
    void 입력받은_애너테이션이_매핑_애너테이션이_아니라면_false를_반환한다() {
        // given
        final Controller annotation = TestController.class.getDeclaredAnnotation(Controller.class);

        // expect
        assertThat(isAnyMatch(annotation)).isEqualTo(false);
    }
}
