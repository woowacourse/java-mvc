package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("nextstep.mvc.controller");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void findControllers() {
        List<Class<?>> controllers = handlerMapping.findControllers();
        System.out.println("controllers = " + controllers);
        assertThat(controllers).hasSize(1);
    }

    @Test
    void findMethods() {
        List<Method> methods = handlerMapping.findMappingMethods(TestController.class);
        System.out.println("methods = " + methods);
        assertThat(methods).hasSize(2);
    }

    @Test
    void sameRequestURL() {
        //given
        List<Method> methods = handlerMapping.findMappingMethods(TestController.class);
        Method findUserId = methods.get(0);
        Method save = methods.get(1);
        assertThat(findUserId.getName()).isEqualTo("findUserId");

        //when
        boolean sameRequestURL = handlerMapping.sameRequestURL("/get-test", findUserId);
        boolean sameRequestURL2 = handlerMapping.sameRequestURL("/get-test", save);

        //then
        assertThat(sameRequestURL).isTrue();
        assertThat(sameRequestURL2).isFalse();
    }

    @Test
    void anyMatchRequestMethod() {
        //given
        List<Method> methods = handlerMapping.findMappingMethods(TestController.class);
        Method findUserId = methods.get(0);
        assertThat(findUserId.getName()).isEqualTo("findUserId");

        //when
        boolean anyMatchRequestMethod = handlerMapping.anyMatchRequestMethod(RequestMethod.GET, findUserId);
        boolean anyMatchRequestMethod2 = handlerMapping.anyMatchRequestMethod(RequestMethod.POST, findUserId);

        //then
        assertThat(anyMatchRequestMethod).isTrue();
        assertThat(anyMatchRequestMethod2).isFalse();
    }
}
