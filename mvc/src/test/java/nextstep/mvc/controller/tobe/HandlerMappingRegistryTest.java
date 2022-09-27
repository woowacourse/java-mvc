package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HandlerMappingRegistryTest {

    @DisplayName("지원하는 핸들러를 반환한다.")
    @Test
    void getHandler() {
        // given
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        final AnnotationHandlerMapping handlerMapping = mock(AnnotationHandlerMapping.class);
        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final Object expected = mock(HandlerExecution.class);

        given(handlerMapping.getHandler(request))
                .willReturn(expected);

        // when
        final Object handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isEqualTo(expected);
    }
}
