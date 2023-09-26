package webmvc.org.springframework.web.servlet.mvc.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.ForwardController;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry registry = new HandlerAdapterRegistry(
            Set.of(new HandlerExecutionHandlerAdapter(), new DefaultHandlerAdapter())
    );

    @Test
    void 적절한_어댑터를_반환한다() {
        // given
        Object handler = new ForwardController("/index.jsp");

        // when
        HandlerAdapter annotatedResult = registry.getHandlerAdapter(handler);

        // then
        assertThat(annotatedResult).isInstanceOf(DefaultHandlerAdapter.class);
    }
}
