package nextstep.mvc.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.assembler.annotation.Component;
import nextstep.mvc.support.annotation.AnnotationHandleUtils;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class AnnotationHandleUtilsTest {

    @DisplayName("클래스를 Annotation 으로 검색한다.")
    @Test
    void searchClassesByAnnotation() {
        String basePath = "samples";

        Set<Class<?>> controllers = AnnotationHandleUtils.getClassesAnnotated(basePath, Controller.class);
        assertThat(controllers).isEqualTo(TestController.class);
    }

    @DisplayName("클래스 내부의 메서드를 Annotation 으로 검색한다.")
    @Test
    void getAnnotatedMethodsFormClasses() {
        String basePath = "samples";

        Set<Class<?>> controllers = AnnotationHandleUtils.getClassesAnnotated(basePath, Controller.class);

        List<Method> methods = controllers.stream()
                .flatMap(klass -> AnnotationHandleUtils.getMethodsAnnotatedWith(klass, RequestMapping.class).stream())
                .collect(Collectors.toList());

        for(Method method : TestController.class.getMethods()){
            boolean isSearched = methods.contains(method);
            boolean isPresentInReal = method.isAnnotationPresent(RequestMapping.class);

            assertThat(isSearched).isEqualTo(isPresentInReal);
        }
    }
}
