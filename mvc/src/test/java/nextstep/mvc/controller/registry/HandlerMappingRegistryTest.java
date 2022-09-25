package nextstep.mvc.controller.registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerMappingRegistryTest {

    @DisplayName("HandlerMappingRegistry는 각 HandlerMapping을 등록할 수 있다.")
    @Test
    void addHandlerMappings() throws NoSuchMethodException {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        handlerMappingRegistry.initialize();

        // when
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        // then
        Object handler = handlerMappingRegistry.getHandler(request);
        assertThat((HandlerExecution) handler).extracting("method")
            .isEqualTo(TestController.class.getDeclaredMethod(
                "save", HttpServletRequest.class, HttpServletResponse.class));
    }
}
