package webmvc.org.springframework.web.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerMappingsTest {

    @Test
    void HandlerMapping을_입력받아_추가한다() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test/get");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMappings handlerMappings = new HandlerMappings();
        final HandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");

        // when
        handlerMappings.add(handlerMapping);

        // then
        assertThat(handlerMappings.getHandler(request)).isPresent();
    }

    @Test
    void 입력받은_HttpServletRequest에_해당하는_HandlerMapping을_반환한다() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test/get");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMappings handlerMappings = new HandlerMappings();
        final HandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMappings.add(handlerMapping);

        // when
        final Optional<Object> handler = handlerMappings.getHandler(request);

        // then
        assertThat(handler).isPresent();
    }
}
