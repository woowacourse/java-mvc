package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HandlerMappingRegistryTest {

    @Test
    void HandlerMapping을_등록하고_가져온다() throws Exception {
        var handlerMappingRegistry = new HandlerMappingRegistry();
        var handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        assertThat(handlerMappingRegistry.getHandler(request)).isPresent();
    }
}
