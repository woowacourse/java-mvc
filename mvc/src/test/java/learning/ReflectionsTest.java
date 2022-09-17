package learning;

import java.lang.reflect.Method;
import java.util.Set;
import nextstep.web.annotation.RequestMapping;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

class ReflectionsTest {

    @Test
    void readClasses() {
        ConfigurationBuilder config = new ConfigurationBuilder()
                .forPackage("samples")
                .setScanners(Scanners.values());
        Reflections reflections = new Reflections(config);

        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(RequestMapping.class);
        for (Method method : methodsAnnotatedWith) {
            System.out.println("method = " + method);
        }
    }
}
