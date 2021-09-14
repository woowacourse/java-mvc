package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class Junit4TestRunner extends JunitOutput {

    @Test
    void runWithReflections() throws Exception {
        // given
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getConstructor().newInstance();
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forClass(Junit4Test.class))
                        .setScanners(new MethodAnnotationsScanner())
        );

        // when
        for (Method method : reflections.getMethodsAnnotatedWith(MyTest.class)) {
            method.invoke(junit4Test);
        }

        // then
        String output = captor.toString().trim();
        assertThat(output)
                .contains(
                        "Running Test1",
                        "Running Test2")
                .doesNotContain("Running Test3");
    }

    @Test
    void runWithoutReflections() throws Exception {
        // given
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getConstructor().newInstance();

        // when
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(junit4Test);
            }
        }

        // then
        String output = captor.toString().trim();
        assertThat(output)
                .contains(
                        "Running Test1",
                        "Running Test2")
                .doesNotContain("Running Test3");
    }
}
