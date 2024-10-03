package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

class AnnotationExecutionsTest {

    private AnnotationExecutions annotationExecutions;

    @BeforeEach
    void setUp() throws Exception {
        annotationExecutions = new AnnotationExecutions();
        TestController instance = TestController.class.getConstructor().newInstance();
        for (Method method : TestController.class.getDeclaredMethods()) {
            annotationExecutions.addExecutor(instance, method);
        }
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) annotationExecutions.getHandler(request.getRequestURI(), RequestMethod.GET);
        final var modelAndView = (ModelAndView) handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = (HandlerExecution) annotationExecutions.getHandler(request.getRequestURI(), RequestMethod.POST);
        final var modelAndView = (ModelAndView) handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void 정의되지_않은_method_get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/none-method-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) annotationExecutions.getHandler(request.getRequestURI(), RequestMethod.GET);
        final var modelAndView = (ModelAndView) handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void 정의되지_않은_method_post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/none-method-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = (HandlerExecution) annotationExecutions.getHandler(request.getRequestURI(), RequestMethod.POST);
        final var modelAndView = (ModelAndView) handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void 정의되지_않은_method_delete() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/none-method-test");
        when(request.getMethod()).thenReturn("DELETE");

        final var handlerExecution = (HandlerExecution) annotationExecutions.getHandler(request.getRequestURI(), RequestMethod.DELETE);
        final var modelAndView = (ModelAndView) handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void executor를_추가한다()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        AnnotationExecutions annotationExecutions = new AnnotationExecutions();
        TestController controller = TestController.class.newInstance();
        Method method = TestController.class.getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        // when
        annotationExecutions.addExecutor(controller, method);

        // then
        assertThat(annotationExecutions.hasHandler("/get-test", RequestMethod.GET)).isTrue();
    }
}
