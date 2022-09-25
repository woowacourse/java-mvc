package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import nextstep.mvc.controller.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerMethod;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HandlerAdapterRegistryTest {

    @Test
    void addHandlerAdapter() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry(new ArrayList<>());
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());

        HandlerMethod handlerMethod = Mockito.mock(HandlerMethod.class);
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(new HandlerExecution(handlerMethod));

        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }
}
