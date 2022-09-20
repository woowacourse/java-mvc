package nextstep.mvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    @DisplayName("init 메소드는 등록되 handlerMapping 객체들의 initialize 메서드를 한번씩 호출한다.")
    void init() {
        final HandlerMapping handlerMapping = mock(HandlerMapping.class);
        final HandlerMapping antherHandlerMapping = mock(HandlerMapping.class);

        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(handlerMapping);
        dispatcherServlet.addHandlerMapping(antherHandlerMapping);
        dispatcherServlet.init();

        verify(handlerMapping, times(1)).initialize();
        verify(antherHandlerMapping, times(1)).initialize();
    }
}
