package nextstep.mvc.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fixture.RequestFixture;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.web.support.RequestMethod;

class HandlerMappingRegistryTest {

    @DisplayName("annotation 기반 handler 조회")
    @Test
    void getHandlerAboutAnnotation() {
        final HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.add(new AnnotationHandlerMapping("samples"));
        registry.init();

        final HttpServletRequest request = RequestFixture.getRequest();

        assertThat(registry.getHandler(request)).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("추가되지 않은 handler 조회 시 예외 발생")
    @Test
    void getHandlerNotExist() {
        final HandlerMappingRegistry registry = new HandlerMappingRegistry();
        final HttpServletRequest request = RequestFixture.request("/not-exist-path", RequestMethod.GET);

        assertThatThrownBy(() -> registry.getHandler(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("요청한 핸들러가 존재하지 않습니다.");
    }
}
