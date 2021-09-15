package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResourceHandlerMappingTest {

    @DisplayName("존재하는 파일이면 적절한 핸들러를 반환. 존재하지 않을 경우 null 반환.")
    @Test
    void initialize() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/TestAnnotationController.java");
        when(request.getMethod()).thenReturn("GET");

        final ResourceHandlerMapping handlerMapping = new ResourceHandlerMapping("src/test/java/samples");
        handlerMapping.initialize();
        assertThat(handlerMapping.getHandler(request)).isNotNull();

        when(request.getRequestURI()).thenReturn("/nonono.java");
        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
