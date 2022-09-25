package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerMappingRegistryTest {

    @Test
    void 어노테이션에_등록한_컨트롤러를_반환한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.add(new AnnotationHandlerMapping("samples"));
        registry.init();

        Object handler = registry.getHandler(request).get();
        assertThat(handler).extracting("instance").isInstanceOf(TestController.class);
    }

    @Test
    void 핸들러가_존재하지_않는경우_Null_을_반환한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/noop");
        when(request.getMethod()).thenReturn("GET");

        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.add(new AnnotationHandlerMapping("samples"));
        registry.init();

        assertThat(registry.getHandler(request)).isEmpty();
    }
}
