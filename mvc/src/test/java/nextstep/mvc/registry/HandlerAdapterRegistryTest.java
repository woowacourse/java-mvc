package nextstep.mvc.registry;

import static org.assertj.core.api.Assertions.assertThatNoException;

import nextstep.mvc.controller.tobe.adapters.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.adapters.ManualHandlerAdapter;
import nextstep.mvc.controller.tobe.mappings.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.ManualController;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("핸들러 어댑터를 추가한다")
    void addHandlerAdapter() {
        // given
        HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        // when & then
        assertThatNoException()
                .isThrownBy(() -> registry.addHandlerAdapter(new AnnotationHandlerAdapter()));
    }

    @Test
    @DisplayName("핸들러에 따라 적절한 어댑터를 찾는다(어노테이션)")
    void findHandlerAdapterAnnotation() {
        // given
        HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new AnnotationHandlerAdapter());
        registry.addHandlerAdapter(new ManualHandlerAdapter());
        // when
        Object execution = new HandlerExecution(null, null);
        // then
        assertThatNoException().isThrownBy(
                () -> registry.findAdapter(execution)
        );
    }

    @Test
    @DisplayName("핸들러에 따라 적절한 어댑터를 찾는다(컨트롤러)")
    void findHandlerAdapterController() {
        // given
        HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new AnnotationHandlerAdapter());
        registry.addHandlerAdapter(new ManualHandlerAdapter());
        // when
        Object controller = new ManualController();
        // then
        assertThatNoException().isThrownBy(
                () -> registry.findAdapter(controller)
        );
    }
}
