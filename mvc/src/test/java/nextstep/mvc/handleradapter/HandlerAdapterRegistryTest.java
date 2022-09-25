package nextstep.mvc.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.handlermapping.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.support.RequestMethod;
import samples.TestInterfaceController;

class HandlerAdapterRegistryTest {

    @DisplayName("annotation 기반 handler adapter 초기화")
    @Test
    void annotationInit() {
        final HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        registry.add(new AnnotationHandlerAdapter());

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn(String.valueOf(RequestMethod.GET));
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final ModelAndView modelAndView = registry.getModelAndView(getHandlerExecution(), request, response);

        assertThat(modelAndView.getView())
            .usingRecursiveComparison()
            .isEqualTo(new JspView("/get-test.jsp"));
    }

    private HandlerExecution getHandlerExecution() {
        try {
            final Class<?> clazz = Class.forName("samples.TestAnnotationController");
            final Object controller = clazz.getConstructors()[0].newInstance();
            final Method method = clazz.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

            return new HandlerExecution(controller, method);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    @DisplayName("controller interface 기반 handler adapter 초기화")
    @Test
    void controllerInterfaceInit() {
        final HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        registry.add(new ManualHandlerAdapter());

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final ModelAndView modelAndView = registry.getModelAndView(new TestInterfaceController(), request, response);

        assertThat(modelAndView.getView())
            .usingRecursiveComparison()
            .isEqualTo(new JspView("/test-path.jsp"));
    }
}
