package nextstep.mvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @DisplayName("init 메서드를 호출하면 등록된 handlerMapping 객체들의 initialize 메서드를 한번씩 호출한다.")
    @Test
    void init() {
        final var handlerMapping1 = mock(HandlerMapping.class);
        final var handlerMapping2 = mock(HandlerMapping.class);

        final var dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(handlerMapping1);
        dispatcherServlet.addHandlerMapping(handlerMapping2);
        dispatcherServlet.init();

        verify(handlerMapping1, times(1)).initialize();
        verify(handlerMapping2, times(1)).initialize();
    }
}
