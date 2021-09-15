package nextstep.mvc.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingsTest {

    private HandlerMappings handlerMappings;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handlerMappings = new HandlerMappings();
        request = mock(HttpServletRequest.class);
    }

    @DisplayName("지원하는 핸들러 반환")
    @Test
    void getHandler() {
        final HandlerMapping handlerMapping = mock(HandlerMapping.class);
        final Object handler = new Object();
        handlerMappings.add(handlerMapping);

        when(handlerMapping.getHandler(request)).thenReturn(handler);

        assertThat(handlerMappings.getHandler(request)).isEqualTo(handler);
    }

    @DisplayName("지원하는 핸들러가 없는 경우 예외 처리")
    @Test
    void cannotFindHandler() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> handlerMappings.getHandler(request))
            .withMessageContaining("적절한 핸들러를 찾지 못 했습니다.");
    }
}
