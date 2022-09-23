package nextstep.mvc.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestManualController;
import samples.TestManualHandlerMapping;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("AnnotationHandlerMapping을 등록하고 handler를 꺼내온다.")
    void getHandler() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        willReturn("/get-manual")
                .given(request)
                .getRequestURI();
        willReturn("GET")
                .given(request)
                .getMethod();

        final HandlerMappingRegistry registry = new HandlerMappingRegistry();
        final TestManualHandlerMapping handlerMapping = new TestManualHandlerMapping();
        handlerMapping.initialize();

        // when
        registry.addHandlerMapping(handlerMapping);
        final Object handler = registry.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(TestManualController.class);
    }
}
