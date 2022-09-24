package nextstep.mvc.controller.tobe.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.tobe.handlermapping.HandlerExecution;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class HandlerAdapterRegistryTest {

    @Test
    void addHandlerAdapter() throws Exception {
        HandlerAdapterRegistry registry = new HandlerAdapterRegistry(new ArrayList<>());

        registry.addHandlerAdapter(new AnnotationHandlerAdapter());

        Class<?> clazz = Class.forName("samples.TestAnnotationController");
        Method method = clazz.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handler = new HandlerExecution(new TestAnnotationController(), method);

        HandlerAdapter handlerAdapter = registry.getHandlerAdapter(handler);
        assertThat(handlerAdapter.supports(handler)).isTrue();
    }

    @Test
    void getHandlerAdapter() throws Exception {
        HandlerAdapterRegistry registry = new HandlerAdapterRegistry(new ArrayList<>());

        registry.addHandlerAdapter(new AnnotationHandlerAdapter());

        Class<?> clazz = Class.forName("samples.TestAnnotationController");
        Method method = clazz.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handler = new HandlerExecution(new TestAnnotationController(), method);

        assertThat(registry.getHandlerAdapter(handler)).isInstanceOf(AnnotationHandlerAdapter.class);
    }
}
