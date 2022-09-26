package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.RequestMappingHandlerAdapter;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {


    @DisplayName("findHandlerAdapter 메서드는")
    @Nested
    class FindHandlerAdapter {

        private HandlerAdapterRegistry handlerAdapterRegistry;

        @BeforeEach
        void setUp() {
            handlerAdapterRegistry = new HandlerAdapterRegistry();
            handlerAdapterRegistry.add(new RequestMappingHandlerAdapter());
        }

        @DisplayName("매핑되는 핸들러를 찾았을 때 이를 반환한다")
        @Test
        void findHandler_returns_mapped_handler() {
            // given
            final Map<HandlerKey, HandlerExecution> samples = new ControllerScanner("samples").scan();
            final HandlerExecution handler = samples.get(new HandlerKey("/get-test", RequestMethod.GET));
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.findHandlerAdapter(handler);

            // when
            final boolean supports = handlerAdapter.supports(handler);

            // then
            assertAll(
                    () -> assertThat(supports).isTrue(),
                    () -> assertThat(handler).isNotNull()
            );
        }

        @DisplayName("매핑되는 핸들러를 찾지 못하면 예외를 던진다")
        @Test
        void findHandler_throws_exception_when_no_mapped_handler_found() {
            // given
            final Map<HandlerKey, HandlerExecution> samples = new ControllerScanner("samples").scan();
            final HandlerExecution handler = samples.get(new HandlerKey("/not-exist-url", RequestMethod.GET));

            // when & then
            assertThatThrownBy(() -> handlerAdapterRegistry.findHandlerAdapter(handler))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("No HandlerAdapter found for Handler");
        }
    }
}
