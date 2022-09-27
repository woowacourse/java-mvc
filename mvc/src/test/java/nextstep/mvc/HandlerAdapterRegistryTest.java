package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.adapter.HandlerAdapterRegistry;
import nextstep.mvc.adapter.RequestMappingHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    void requestMappingHandlerAdapter를_찾을_수_있다() {
        HandlerAdapterRegistry adapterRegistry = new HandlerAdapterRegistry();
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        adapterRegistry.addHandlerAdapter(requestMappingHandlerAdapter);

        HandlerAdapter handlerAdapter = adapterRegistry.getHandlerAdapter(new HandlerExecution(null, null));

        assertThat(handlerAdapter).isExactlyInstanceOf(RequestMappingHandlerAdapter.class);
    }
}
