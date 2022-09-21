package nextstep.mvc.registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.fixtures.HttpServletFixtures;
import nextstep.mvc.controller.tobe.mappings.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.mappings.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("핸들러 매핑을 추가한다.")
    void addHandlerMappings() {
        // given
        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        // when & then
        assertThatNoException()
                .isThrownBy(() -> registry.addHandlerMapping(new AnnotationHandlerMapping("samples")));
    }

    @Test
    @DisplayName("추가한 핸들러 매핑을 초기화한다.")
    void initialize() {
        // given
        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(new AnnotationHandlerMapping("samples"));

        // when & then
        assertThatNoException()
                .isThrownBy(registry::initialize);
    }

    @Test
    @DisplayName("적절한 핸들러 매핑을 찾는다.")
    void findHandler() {
        // given
        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        registry.initialize();

        HttpServletRequest request = HttpServletFixtures.createRequest();
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        Object handler = registry.findHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
