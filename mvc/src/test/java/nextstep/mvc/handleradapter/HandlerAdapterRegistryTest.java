package nextstep.mvc.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fixture.RequestFixture;
import fixture.ResponseFixture;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.handlermapping.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

class HandlerAdapterRegistryTest {

    @DisplayName("annotation 기반 ModelAndView 조회")
    @Test
    void getModelAndViewAboutAnnotation() {
        final HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        registry.add(new AnnotationHandlerAdapter());

        final HttpServletRequest request = RequestFixture.getRequest();
        final HttpServletResponse response = ResponseFixture.response();

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
}
