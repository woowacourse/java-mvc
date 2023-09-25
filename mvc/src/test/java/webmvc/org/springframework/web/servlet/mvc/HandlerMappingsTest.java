package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingsTest {
    private HandlerExecution expected = mock();

    class MockHandlerMapping implements HandlerMapping {
        @Override
        public HandlerExecution getHandler(final HttpServletRequest request) {
            if (request.getRequestURI().equals("/hello")) {
                return expected;
            }
            return null;
        }
    }

    @Test
    void 올바른_핸들러를_반환한다() throws ServletException {
        //given
        final List<HandlerMapping> handlers = List.of(new MockHandlerMapping());
        final var handlerMappings = new HandlerMappings(handlers);

        //when
        final var mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getRequestURI()).thenReturn("/hello");
        HandlerExecution actual = handlerMappings.getHandler(mockRequest);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 핸들러가_없는경우_예외가_발생한다() {
        //given
        final List<HandlerMapping> handlers = List.of(new MockHandlerMapping());
        final var handlerMappings = new HandlerMappings(handlers);

        //when
        final var mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getRequestURI()).thenReturn("/world");

        //then
        assertThatThrownBy(() -> handlerMappings.getHandler(mockRequest))
                .isInstanceOf(ServletException.class)
                .hasMessage("핸들러를 찾을 수 없습니다.");
    }

}
