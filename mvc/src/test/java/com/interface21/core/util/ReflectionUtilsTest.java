package com.interface21.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMapping;
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.jupiter.api.Test;
import samples.ChildController;

class ReflectionUtilsTest {

    @Test
    void getAllMethods_ThenExcludeObjectMethods() {
        final Set<Method> methods = ReflectionUtils.getAllMethods(ChildController.class, method -> true);

        assertThat(methods)
                .extracting(ReflectionUtilsTest::nameOf)
                .doesNotContain("Object");
    }

    @Test
    void getAllMethods_WhenSuperclassHasMatchingMethods_ThenIncludeMethodsDeclaredInSuperclass() {
        final Set<Method> methods = ReflectionUtils.getAllMethods(
                ChildController.class,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );

        assertThat(methods)
                .extracting(ReflectionUtilsTest::nameOf)
                .containsExactlyInAnyOrder(
                        "ChildController.child()",
                        "ParentController.parent()",
                        "ParentController.parentPackagePrivate()",
                        "ParentController.parentPrivate()",
                        "ParentController.overridden()"
                );
    }

    private static String nameOf(final Method method) {
        return method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
    }

    @Test
    void getAllMethods_WhenOverriddenWithoutAnnotation_ThenIncludeOnlySuperclassMethod() {
        final Set<Method> methods = ReflectionUtils.getAllMethods(
                ChildController.class,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );

        assertThat(methods)
                .filteredOn(method -> method.getName().equals("overridden"))
                .extracting(ReflectionUtilsTest::nameOf)
                .containsExactly("ParentController.overridden()");
    }
}
