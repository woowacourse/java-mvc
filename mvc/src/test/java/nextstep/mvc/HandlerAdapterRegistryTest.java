package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    void manualHandlerAdapter를_찾을_수_있다() {
        HandlerAdapterRegistry adapterRegistry = new HandlerAdapterRegistry();
        ManualMappingHandlerAdapter manualMappingHandlerAdapter = new ManualMappingHandlerAdapter();
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        adapterRegistry.addHandlerAdapter(manualMappingHandlerAdapter);
        adapterRegistry.addHandlerAdapter(requestMappingHandlerAdapter);

        HandlerAdapter handlerAdapter = adapterRegistry.getHandlerAdapter(new HandlerController());

        assertThat(handlerAdapter).isExactlyInstanceOf(ManualMappingHandlerAdapter.class);
    }

    class HandlerController implements Controller {
        @Override
        public String execute(HttpServletRequest req, HttpServletResponse res) {
            return null;
        }
    }

    @Test
    void requestMappingHandlerAdapter를_찾을_수_있다() {
        HandlerAdapterRegistry adapterRegistry = new HandlerAdapterRegistry();
        ManualMappingHandlerAdapter manualMappingHandlerAdapter = new ManualMappingHandlerAdapter();
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        adapterRegistry.addHandlerAdapter(manualMappingHandlerAdapter);
        adapterRegistry.addHandlerAdapter(requestMappingHandlerAdapter);

        HandlerAdapter handlerAdapter = adapterRegistry.getHandlerAdapter(new HandlerExecution(null, null));

        assertThat(handlerAdapter).isExactlyInstanceOf(RequestMappingHandlerAdapter.class);
    }
}
