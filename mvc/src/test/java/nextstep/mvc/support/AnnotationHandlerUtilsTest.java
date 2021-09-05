package nextstep.mvc.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class AnnotationHandlerUtilsTest {

    @DisplayName("클래스를 Annotation 으로 검색한다.")
    @Test
    void searchClassesByAnnotation() {
        String basePath = "samples";

        Set<Class<?>> controllers = AnnotationHandlerUtils.getClassesAnnotatedWith(basePath, Controller.class);
        assertThat(controllers).contains(TestController.class);
    }

    @DisplayName("클래스 내부의 메서드를 Annotation 으로 검색한다.")
    @Test
    void getAnnotatedMethodsFormClasses() {
        String basePath = "samples";

        Set<Class<?>> controllers = AnnotationHandlerUtils.getClassesAnnotatedWith(basePath, Controller.class);

        List<Method> methods = controllers.stream()
                .flatMap(klass -> AnnotationHandlerUtils.getMethodsAnnotatedWith(klass, RequestMapping.class).stream())
                .collect(Collectors.toList());

        assertThat(methods).contains(TestController.class.getDeclaredMethods());
    }
}
