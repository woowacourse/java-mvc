package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javassist.NotFoundException;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerMappingRegistryTest {

    @Test
    void 핸들러를_조회할_수_있다() throws NoSuchMethodException, NotFoundException {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        handlerMappingRegistry.initialize();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        final Object handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat((HandlerExecution) handler).extracting("method")
                .isEqualTo(TestController.class.getDeclaredMethod(
                                "findUserId", HttpServletRequest.class, HttpServletResponse.class
                        )
                );
    }
}
