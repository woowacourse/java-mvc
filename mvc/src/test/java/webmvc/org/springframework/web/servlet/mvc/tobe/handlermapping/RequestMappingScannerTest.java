package webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping;

import context.org.springframework.stereotype.Controller;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

class RequestMappingScannerTest {

    private static final String BASE_PACKAGE = "samples";

    private final RequestMappingScanner requestMappingScanner = new RequestMappingScanner();

    @Test
    void scanRequestMappingMethods() {
        //given
        Reflections reflections = new Reflections(BASE_PACKAGE);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        //when
        List<Method> methods = requestMappingScanner.scanRequestMappingMethods(controllers);

        //then
        Assertions.assertThat(methods.size()).isEqualTo(2);
    }
}
