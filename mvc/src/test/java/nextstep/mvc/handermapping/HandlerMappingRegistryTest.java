package nextstep.mvc.handermapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.common.exception.NotFoundHandlerMappingException;
import nextstep.mvc.handeradapter.TestHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @DisplayName("HandlerMapping 를 추가하고 핸들러 찾는다.")
    @Test
    void addAndGetHandlerMapping() {
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        TestHandler handler = new TestHandler();
        when(handlerMapping.getHandler(any())).thenReturn(handler);

        assertThat(handler).isOfAnyClassIn(TestHandler.class);
    }

    @DisplayName("HandlerMapping 를 찾지 못하면 예외가 발생한다.")
    @Test
    void getInvalidHandlerMapping() {
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        HttpServletRequest request = mock(HttpServletRequest.class);

        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(NotFoundHandlerMappingException.class)
                .hasMessage("핸들러 매핑을 찾을 수 없습니다.");
    }
}
