package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingsTest {

    @Test
    void HandlerMappings_생성() {
        final HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping("samples"));

        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gitchan");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final Optional<Object> handler = handlerMappings.findHandlerMapping(request);

        assertAll(
                () -> assertThat(handler).isNotEmpty(),
                () -> assertThat(handler.get()).isNotNull(),
                () -> assertThat(handler.get()).isInstanceOf(HandlerExecution.class)
        );
    }
}
