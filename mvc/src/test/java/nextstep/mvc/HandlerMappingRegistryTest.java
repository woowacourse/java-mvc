package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class HandlerMappingRegistryTest {

    @DisplayName("핸들러 매핑을 추가하고, 핸들러를 찾을 수 있다.")
    @Test
    void getHandler() throws ServletException {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry(new ArrayList<>());
        handlerMappingRegistry.addHandlerMapping(
                new AnnotationHandlerMapping(TestAnnotationController.class.getPackageName())
        );
        handlerMappingRegistry.initialize();

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        assertThat(handlerMappingRegistry.getHandler(request).get()).isInstanceOf(HandlerExecution.class);
    }
}
