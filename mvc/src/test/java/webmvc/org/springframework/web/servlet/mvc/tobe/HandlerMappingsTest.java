package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.HandlerMappings;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerMappingsTest {

    @Test
    void 핸들러매핑_일급컬렉션은_요청에_따라서_올바른_핸들러매핑을_반환한다() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HandlerMappings handlerMappings = new HandlerMappings(List.of(
                new AnnotationHandlerMapping("samples")
        ));
        handlerMappings.initialize();
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        Object handler = handlerMappings.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
