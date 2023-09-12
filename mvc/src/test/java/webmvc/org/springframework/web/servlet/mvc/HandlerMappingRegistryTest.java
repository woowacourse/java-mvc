package webmvc.org.springframework.web.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerMappingRegistryTest {

    @Test
    void 핸들러_매핑을_추가한다() {
        // given
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        final HandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        // then
        assertThat(handlerMappingRegistry.getHandler(request)).isPresent();
    }

    @Test
    void Request에_해당하는_핸들러를_가져온다() {
        // given
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry(List.of(
                new AnnotationHandlerMapping("samples")
        ));
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        final Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isPresent();
    }
}
