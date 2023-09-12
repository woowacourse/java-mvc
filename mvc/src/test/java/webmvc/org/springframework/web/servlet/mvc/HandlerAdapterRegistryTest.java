package webmvc.org.springframework.web.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerAdapterRegistryTest {

    @Test
    void 핸들러를_처리할_수_있는_핸들러_어댑터를_반환한다() {
        // given
        final HandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
        final HandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry(List.of(
                handlerExecutionHandlerAdapter, controllerHandlerAdapter
        ));
        final HandlerExecution handler = new HandlerExecution(null, null);

        // when
        final HandlerAdapter result = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(result).isEqualTo(handlerExecutionHandlerAdapter);
    }
}
