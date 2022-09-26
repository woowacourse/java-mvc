package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    void getAdapter() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        final Object handler = handlerMappingRegistry.getHandler(request);

        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());

        // when
        final HandlerAdapter adapter = handlerAdapterRegistry.getAdapter(handler);

        // then
        final ModelAndView modelAndView = adapter.handle(request, response, handler);
        final String id = (String) modelAndView.getObject("id");
        assertThat(id).isEqualTo("gugu");
    }
}
