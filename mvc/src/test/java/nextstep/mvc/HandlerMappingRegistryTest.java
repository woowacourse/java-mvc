package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @DisplayName("적절한 핸들러가 있으면 반환한다.")
    @Test
    void getHandlers_Exist() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HandlerMapping containsProperHandler = mock(HandlerMapping.class);
        when(containsProperHandler.getHandler(request)).thenReturn(new Object());
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(containsProperHandler);

        assertThat(handlerMappingRegistry.getHandler(request)).isPresent();
    }

    @DisplayName("적절한 핸들러가 없으면 empty Optional을 반환한다.")
    @Test
    void getHandlers_Not_Exist() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HandlerMapping noProperHandler = mock(HandlerMapping.class);
        when(noProperHandler.getHandler(request)).thenReturn(null);
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(noProperHandler);

        assertThat(handlerMappingRegistry.getHandler(request)).isEmpty();
    }
}
