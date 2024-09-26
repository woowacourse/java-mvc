package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import samples.TestController;

public class ControllersTest {

    private static final Logger log = LoggerFactory.getLogger(ControllersTest.class);

    private Controllers controllers;

    @BeforeEach
    void setUp() {
        Object[] basePackage = new String[]{"samples"};
        controllers = new Controllers(basePackage);
    }

    @Test
    void 클래스_타입으로_클래스_인스턴스를_가져올_수_있다() {
        // given
        Class<?> clazz = TestController.class;

        // when
        Object controller = controllers.getController(clazz);

        // then
        assertThat(controller).isInstanceOf(TestController.class);
    }

    @Test
    void 메서드의_정보로_메서드가_속한_클래스_인스턴스를_가져올_수_있다() throws NoSuchMethodException {
        // given
        Method method = TestController.class.getMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class);
        Class<?> clazz = method.getDeclaringClass();

        // when
        Object controller = controllers.getController(clazz);

        // then
        assertThat(controller).isInstanceOf(TestController.class);
    }

    @Test
    void 특정_어노테이션이_붙은_메서드들의_정보를_가져올_수_있다() {
        // when
        Set<Method> methods = controllers.getAnnotationMethods(RequestMapping.class);
        methods.forEach(method -> log.info(method.getName()));

        // then
        assertThat(methods.size()).isEqualTo(5);
    }
}
