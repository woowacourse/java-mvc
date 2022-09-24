package nextstep.mvc.controller.registry;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.ManualHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerAdapterRegistryTest {

    @DisplayName("HandlerAdapterRegistry는 각 HandlerAdapter를 등록할 수 있다.")
    @Test
    void initializedHandlerAdapters()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // given
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(manualHandlerAdapter);
        handlerAdapterRegistry.addHandlerAdapter(annotationHandlerAdapter);

        //when
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(new HandlerExecution(
            TestController.class.getConstructor().newInstance(),
            TestController.class.getDeclaredMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class
            )
        ));

        // then
        assertThat(handlerAdapter.getClass()).isEqualTo(AnnotationHandlerAdapter.class);
    }
}
