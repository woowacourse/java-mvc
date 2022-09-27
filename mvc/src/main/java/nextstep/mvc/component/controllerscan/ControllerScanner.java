package nextstep.mvc.component.controllerscan;

import java.util.HashSet;
import java.util.Set;
import nextstep.web.annotation.Controller;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(final Object[] basePackage) {
        this.basePackage = basePackage;
    }

    public Set<Class<?>> scanAllController() {
        final Set<Class<?>> result = new HashSet<>();
        for (Object packageName : basePackage) {
            result.addAll(scanControllerInPackage((String) packageName));
        }

        return result;
    }

    private Set<Class<?>> scanControllerInPackage(final String packageName) {
        final ReflectionLoader reflectionLoader = new ReflectionsReflectionLoader();
        return reflectionLoader.getClassesAnnotatedWith(packageName, Controller.class);
    }
}
