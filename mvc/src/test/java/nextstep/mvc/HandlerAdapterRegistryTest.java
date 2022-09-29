package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.RequestMappingHandlerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Test
    void handlerAdapter를_추가한다() {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.add(new AnnotationHandlerMapping("nextstep.mvc"));
        handlerMappingRegistry.initialize();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/stub");
        when(request.getMethod()).thenReturn("GET");

        handlerAdapterRegistry.add(new RequestMappingHandlerAdapter());
        final Optional<Object> handler = handlerMappingRegistry.findHandler(request);

        assertThatCode(() -> handlerAdapterRegistry.findHandlerAdapter(handler.get()))
                .doesNotThrowAnyException();
    }

    @Test
    void handlerAdapter가_존재하지_않으면_예외가_발생한다() {
        assertThatThrownBy(() -> handlerAdapterRegistry.findHandlerAdapter(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 HandlerAdapter가 존재하지 않습니다.");
    }
}
