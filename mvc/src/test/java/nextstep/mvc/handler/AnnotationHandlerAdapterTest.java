package nextstep.mvc.handler;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    @Disabled("AnnotationHandlerAdapterTest에서 samples가 아닌 test 패키지 전체를 읽는 문제 해결해야함")
    void supportRequestMappingAnnotatedClass() {
//        class AnnotatedClass {
//            @RequestMapping(value = "/test", method = RequestMethod.GET)
//            public void test() {
//
//            }
//        }
//        AnnotatedClass annotatedClass = new AnnotatedClass();
//
//        assertThat(sut.supports(annotatedClass)).isTrue();
    }

    @Test
    @DisplayName("RequestMapping 애너테이션이 하나라도 붙어있지 않으면 거짓을 반환한다")
    void notSupportedIfNonAnnotated() {
        class NotAnnotatedClass {

        }
        NotAnnotatedClass notAnnotatedClass = new NotAnnotatedClass();

        assertThat(sut.supports(notAnnotatedClass)).isFalse();
    }
}
