package nextstep.mvc.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.AnnotationHandlerMapping;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("HandlerMappings 테스트")
class HandlerMappingsTest {

    private HandlerMappings handlerMappings;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handlerMappings = new HandlerMappings();
        handlerMappings.add(new AnnotationHandlerMapping("samples"));
        handlerMappings.initialize();
        request = mock(HttpServletRequest.class);
    }

    @DisplayName("HttpServletRequest에 매핑되는 handler를 찾아준다 - 성공")
    @Test
    void getHandler() {
        // given
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn(String.valueOf(RequestMethod.GET));

        // when
        final Object handlerObject = handlerMappings.getHandler(request);

        // then
        assertThat(handlerObject).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("HttpServletRequest에 매핑되는 handler를 찾아준다 - 실패 - 존재하지 않음.")
    @Test
    void getHandlerFailureWhenNotExists() {
        // given
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn(String.valueOf(RequestMethod.POST));

        // when
        // then
        assertThatThrownBy(() -> handlerMappings.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}