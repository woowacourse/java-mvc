package com.interface21.webmvc.servlet.mvc;

import java.lang.reflect.Method;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.interface21.webmvc.servlet.ModelAndView;
import samples.AnnotationTestController;
import samples.TestController;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new AnnotationHandlerAdapter();
    }

    @DisplayName("AnnotationHandler를 지원한다.")
    @Test
    void supports() throws NoSuchMethodException {
        // given
        AnnotationTestController controller = new AnnotationTestController();
        Method method = controller.getClass()
                .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        Object handler = new HandlerExecution(method, controller);

        // when
        boolean result = handlerAdapter.supports(handler);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("AnnotationHandler외의 handler는 지원하지 않는다.")
    @Test
    void cannotSupport() {
        // given
        TestController controller = new TestController();
        Object handler = controller.getClass().getDeclaredMethods()[0];

        // when
        boolean result = handlerAdapter.supports(handler);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("인자로 받은 handler를 수행하여 ModelAndView를 반환한다.")
    @Test
    void handle() throws Exception {
        // given
        AnnotationTestController controller = new AnnotationTestController();
        Method method = controller.getClass()
                .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        Object handler = new HandlerExecution(method, controller);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        // then
        assertThat(modelAndView).isNotNull();
    }
}
