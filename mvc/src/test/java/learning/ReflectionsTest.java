package learning;

import java.util.Set;
import nextstep.web.annotation.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class ReflectionsTest {

    @Test
    void readClasses() {
        Reflections reflections = new Reflections("samples");

        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        int size = typesAnnotatedWith.size();
        System.out.println("size = " + size);
        for (Class<?> aClass : typesAnnotatedWith) {
            System.out.println("aClass = " + aClass);
        }
    }
}
