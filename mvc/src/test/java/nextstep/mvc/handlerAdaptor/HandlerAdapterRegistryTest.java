package nextstep.mvc.handlerAdaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.handlerMapping.AnnotationHandlerMapping;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("핸들러 어댑터를 추가하고 핸들러를 통해 적절히 요청을 처리할 수 있다.")
    void addAndGetHandlerAdapter() throws Exception {
        // given
        final HandlerAdapterRegistry handlerAdapterRegistry = HandlerAdapterRegistry.from();
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        final AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        // when
        when(request.getAttribute("id")).thenReturn("zero");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // then
        final Object handler = handlerMapping.getHandler(request);
        final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
        assertThat(modelAndView.getObject("id")).isEqualTo("zero");
    }
}