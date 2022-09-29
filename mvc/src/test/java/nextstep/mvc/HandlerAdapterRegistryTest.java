package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.FakeHandlerAdapter;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("핸들러 어댑터를 찾는다")
    void getHandlerAdapter() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.add(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();

        final Object handlerMapping = handlerMappingRegistry.getHandlerMapping(request);

        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.add(new HandlerExecutionHandlerAdapter());

        assertThat(handlerAdapterRegistry.getHandlerAdapter(handlerMapping)).isInstanceOf(
                HandlerExecutionHandlerAdapter.class);
    }

    @Test
    @DisplayName("지원하지 않는 핸들러 어댑터를 찾으면 예외가 발생한다")
    void getHandlerAdapterUriException() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.add(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();

        final Object handlerMapping = handlerMappingRegistry.getHandlerMapping(request);

        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.add(new FakeHandlerAdapter());

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handlerMapping))
                .isExactlyInstanceOf(NoSuchElementException.class);
    }
}
