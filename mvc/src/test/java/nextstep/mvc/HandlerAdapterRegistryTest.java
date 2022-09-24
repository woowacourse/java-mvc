package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {


    @Test
    void HandlerAdapterRegistry를_생성하면_handler를_등록한다() throws NoSuchMethodException {
        // given
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry(
                List.of(new AnnotationHandlerAdapter()));
        Object handler = mock(HandlerExecution.class);

        // when
        Object handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @Test
    void HandlerAdapterRegistry에_handler를_추가할_수_있다() throws NoSuchMethodException {
        // given
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry(new ArrayList<>());
        Object handler = mock(HandlerExecution.class);

        // when
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        Object handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

//         then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }

}
