package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import samples.TestController;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
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

    @DisplayName("요청 url에 대한 핸들러가 존재하지 않으면 null 핸들러를 반환한다.")
    @Test
    void handlerNotFound() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/non-existing-url");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = handlerMapping.getHandler(request);

        assertThat(handlerExecution).isNull();
    }

    @DisplayName("요청 url은 같지만, 메서드가 다르면 null 핸들러를 반환한다.")
    @Test
    void methodNotAllowed() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = handlerMapping.getHandler(request);

        assertThat(handlerExecution).isNull();
    }

    @DisplayName("RequestMapping에 메서드를 지정하지 않아도 처리할 수 있다.")
    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void noRequestMethod(RequestMethod requestMethod) {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/no-request-method");
        when(request.getMethod()).thenReturn(requestMethod.name());

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        assertThat(handlerExecution).isNotNull();
    }

    @DisplayName("핸들러의 정상 등록을 확인한다.")
    @Test
    void initialize() throws Exception {
        Class<?> clazz = AnnotationHandlerMapping.class;
        AnnotationHandlerMapping instance = (AnnotationHandlerMapping) clazz.getDeclaredConstructor(Object[].class)
                .newInstance((Object) new Object[]{"samples"});

        instance.initialize();

        Field handlerExecutions = clazz.getDeclaredField("handlerExecutions");
        handlerExecutions.setAccessible(true);

        Map<?, ?> executions = (Map<?, ?>) handlerExecutions.get(handlerMapping);

        List<String> requestUrl = Stream.of(TestController.class.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .map(method -> method.getAnnotation(RequestMapping.class).value())
                .toList();

        int expectedHandlerExecutionsSize = Stream.of(TestController.class.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .map(method -> {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    RequestMethod[] methods = annotation.method();
                    int methodLength = methods.length;
                    if (methodLength != 0) {
                        return methodLength;
                    }
                    return RequestMethod.values().length;
                })
                .reduce(0, Integer::sum);

        assertThat(executions.size()).isEqualTo(expectedHandlerExecutionsSize);
        assertThat(executions.keySet())
                .extracting("url")
                .containsAll(requestUrl);
    }
}
