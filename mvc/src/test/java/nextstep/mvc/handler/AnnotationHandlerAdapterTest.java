package nextstep.mvc.handler;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.web.annotation.RequestMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    AnnotationHandlerAdapter sut;

    @BeforeEach
    void setUp() {
        sut = new AnnotationHandlerAdapter();
    }

    @Test
    @DisplayName("RequestMapping 애너테이션이 하나라도 붙어있으면 참을 반환한다")
    void supportRequestMappingAnnotatedClass() {
        AnnotatedClass annotatedClass = new AnnotatedClass();

        assertThat(sut.supports(annotatedClass)).isTrue();
    }

    @Test
    @DisplayName("RequestMapping 애너테이션이 하나라도 붙어있지 않으면 거짓을 반환한다")
    void notSupportedIfNonAnnotated() {
        NotAnnotatedClass notAnnotatedClass = new NotAnnotatedClass();

        assertThat(sut.supports(notAnnotatedClass)).isFalse();
    }

    public static class AnnotatedClass {

        @RequestMapping
        public void test() {

        }
    }

    private static class NotAnnotatedClass {

    }
}
