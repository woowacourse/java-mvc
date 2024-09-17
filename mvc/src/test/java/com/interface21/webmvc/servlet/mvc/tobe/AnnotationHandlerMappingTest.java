package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

import samples.TestController;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("특정 패키지 하위에 @Controller 붙은 클래스 추출")
    void getTypesAnnotatedWithController() {
        //given
        final Object[] basePackage = new Object[]{"samples"};
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> expected = Set.of(TestController.class);

        //when
        final Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);

        //then
        assertThat(typesAnnotatedWith).isEqualTo(expected);
    }

    @Test
    @DisplayName("클래스에서 @RequestMapping 붙은 메서드에서 정보 추출")
    void getValueRequestMapping() {
        final Object[] basePackage = new Object[]{"samples"};
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        final Map<String, RequestMethod> expected = Map.of("/get-test", RequestMethod.GET,
                "/post-test", RequestMethod.POST);
        final Map<String, RequestMethod> findMethod = new HashMap<>();

        //when
        for (final Class<?> aClass : classes) {
            final Method[] declaredMethods = aClass.getDeclaredMethods();
            for (final Method declaredMethod : declaredMethods) {
                final RequestMapping request = declaredMethod.getAnnotation(RequestMapping.class);
                final RequestMethod[] requestMethods = request.method();
                for (final RequestMethod requestMethod : requestMethods) {
                    findMethod.put(request.value(), requestMethod);
                }
            }
        }

        //then
        assertThat(findMethod).isEqualTo(expected);
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
    @DisplayName("중복된 매핑 정보가 있는 경우 예외가 발생한다.")
    void failInit() {
        //given
        handlerMapping = new AnnotationHandlerMapping("duplicate");

        //when && then
        assertThatThrownBy(() -> handlerMapping.initialize())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("중복된 매핑 요청입니다.");

    }

    @Test
    @DisplayName("method 설정이 되어 있지 않으면 모든 HTTP method를 지원한다.")
    void provideAllMethod() {
        //given
        handlerMapping = new AnnotationHandlerMapping("all");
        handlerMapping.initialize();

        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/all");
        when(request.getMethod()).thenReturn("GET", "HEAD", "POST", "PUT", "PATCH",
                "DELETE", "OPTIONS", "TRACE");

        //when && then
        for (int i = 0; i < RequestMethod.values().length; i++) {
            assertThat(handlerMapping.getHandler(request)).isNotNull();
        }
    }
}
