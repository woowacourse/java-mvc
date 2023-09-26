package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.handler_adapter.HandlerExecutionHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handler_mapping.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.handler_mapping.HandlerMappingRegistry;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerMappingRegistry handlerMapping = new HandlerMappingRegistry(Set.of(
            new AnnotationHandlerMapping("samples")
    ));
    private final HandlerExecutionHandlerAdapter handlerAdaptor = new HandlerExecutionHandlerAdapter();

    @Test
    void Controller_어노테이션을_적용한_컨트롤러이면_지원한다() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        handlerMapping.initialize();
        Object handler = handlerMapping.getHandler(request).get();

        // when
        boolean result = handlerAdaptor.support(handler);

        // then
        assertThat(result).isTrue();
    }
}
