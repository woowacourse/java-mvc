package nextstep.mvc.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fixture.RequestFixture;
import jakarta.servlet.http.HttpServletRequest;

class HandlerMappingRegistryTest {

    @DisplayName("annotation 기반 handler mapping 초기화")
    @Test
    void annotationInit() {
        final HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.add(new AnnotationHandlerMapping("samples"));
        registry.init();

        final HttpServletRequest request = RequestFixture.getRequest();

        assertThat(registry.getHandler(request)).isInstanceOf(HandlerExecution.class);
    }
}
