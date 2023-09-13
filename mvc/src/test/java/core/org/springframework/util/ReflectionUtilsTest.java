package core.org.springframework.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import context.org.springframework.stereotype.Controller;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReflectionUtilsTest {

    @Test
    void 주어진_패키지에서_특정_어노테이션이_있는_클래스가_담긴_Set을_반환한다() {
        // given
        final Object[] basePackage = {"core.org.springframework.util"};
        final Class<? extends Annotation> annotation = Controller.class;

        // when
        final Set<Class<?>> found = ReflectionUtils.getClassHasAnnotationWith(annotation, basePackage);

        // then
        assertThat(found).contains(SampleController.class);
    }

    @Test
    void 클래스에_특정_어노테이션이_붙은_메서드를_반환한다() {
        // given
        final Object[] basePackage = {"core.org.springframework.util"};
        final Class<? extends Annotation> annotation = Controller.class;
        final Set<Class<?>> annotatedClass = ReflectionUtils.getClassHasAnnotationWith(annotation, basePackage);

        // when
        final List<Method> methods = ReflectionUtils.getMethodHasAnnotationWith(RequestMapping.class, annotatedClass);

        // then
        assertSoftly(softly -> {
            softly.assertThat(methods.size()).isEqualTo(1);
            softly.assertThat(methods.get(0).getName()).isEqualTo("sample");
        });
    }

    @Test
    void 매서드에_있는_어노테이션을_가져온다() {
        // given
        final List<Method> methods = ReflectionUtils.
                getMethodHasAnnotationWith(RequestMapping.class, Set.of(SampleController.class));

        // when
        RequestMapping found = ReflectionUtils.getMethodAnnotation(methods.get(0), RequestMapping.class);

        // then
        assertSoftly(softly -> {
            softly.assertThat(found.value()).isEqualTo("/sample");
            softly.assertThat(found.method()).contains(RequestMethod.GET);
        });
    }
}
