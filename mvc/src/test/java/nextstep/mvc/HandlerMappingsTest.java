package nextstep.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingsTest {

    @Test
    @DisplayName("핸들러 반환 테스트")
    void getHandlerTest() {

        // given
        HandlerMappings handlerMappings = new HandlerMappings(new AnnotationHandlerMapping("samples"));
        handlerMappings.initialize();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        final Object handler = handlerMappings.getHandler(request);

        // then
        assertThat(handler).isExactlyInstanceOf(HandlerExecution.class);
    }
}
