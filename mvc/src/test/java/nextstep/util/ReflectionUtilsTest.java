package nextstep.util;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples1.TestController1;

class ReflectionUtilsTest {

    @DisplayName("어노테이션이 붙은 클래스를 스캔한다.")
    @Test
    void scanClassByAnnotationWith() {
        //given
        Set<Class<?>> samples1 = ReflectionUtils
            .scanClassByAnnotationWith(List.of("samples1"), Controller.class);
        Set<Class<?>> samples1And2 = ReflectionUtils
            .scanClassByAnnotationWith(List.of("samples1","samples2"), Controller.class);
        //when
        //then
        assertThat(samples1).hasSize(2);
        assertThat(samples1And2).hasSize(3);
    }

    @DisplayName("어노테이션이 붙은 메소드를 찾는다.")
    @Test
    void scanAllMethodByAnnotationWith() {
        //given
        Set<Method> methods = ReflectionUtils
            .scanAllMethodByAnnotationWith(TestController1.class, RequestMapping.class);
        //when
        //then
        Set<String> expectedName = Set.of("findUserId", "save");
        Set<String> methodNames = methods.stream()
            .map(Method::getName)
            .collect(toSet());

        assertThat(methodNames).containsAnyElementsOf(expectedName);
    }
}