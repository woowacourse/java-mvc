package webmvc.org.springframework.web.servlet.mvc.handler_adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.IndexController;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry registry = new HandlerAdapterRegistry(
            Set.of(new HandlerExecutionHandlerAdapter(), new IndexHandlerAdapter())
    );

    @Test
    void 적절한_어댑터를_반환한다() {
        // given
        Object handler = new IndexController("/index.jsp");

        // when
        HandlerAdapter annotatedResult = registry.getHandlerAdapter(handler);

        // then
        assertThat(annotatedResult).isInstanceOf(IndexHandlerAdapter.class);
    }
}
