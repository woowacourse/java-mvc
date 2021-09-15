package nextstep.mvc.registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMapping handlerMapping;
    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMapping = mock(HandlerMapping.class);
    }

    @DisplayName("HandlerMapping을 레지스트리에 추가한다.")
    @Test
    void addHandlerMapping() {
        assertThatCode(() -> handlerMappingRegistry.addHandlerMapping(handlerMapping))
            .doesNotThrowAnyException();
    }

    @DisplayName("HandlerMapping을 초기화한다.")
    @Test
    void initializeHandlerMappings() {
        assertThatCode(() -> handlerMappingRegistry.initializeHandlerMappings())
            .doesNotThrowAnyException();
    }

    @DisplayName("적절한 Handler를 찾는다. - 성공")
    @Test
    void findHandler() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
        // when
        when(handlerMapping.getHandler(request)).thenReturn(Object.class);

        // then
        assertThat(handlerMappingRegistry.findHandler(request))
            .isNotNull();
    }

    @DisplayName("적절한 Handler를 찾는다. - 실패, 적절한 핸들러 없음.")
    @Test
    void findHandlerFailed() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
        // when
        when(handlerMapping.getHandler(request)).thenReturn(null);

        // then
        assertThatThrownBy(() -> handlerMappingRegistry.findHandler(request))
            .isInstanceOf(IllegalStateException.class);
    }
}
