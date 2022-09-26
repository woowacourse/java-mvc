package nextstep.mvc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import nextstep.mvc.controller.AnnotationHandlerMapping;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    void init_메서드는_handlerMapping들을_초기화한다() {
        // given
        final HandlerMapping handlerMapping1 = mock(AnnotationHandlerMapping.class);
        final HandlerMapping handlerMapping2 = mock(HandlerMapping.class);

        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(handlerMapping1);
        dispatcherServlet.addHandlerMapping(handlerMapping2);

        // when
        dispatcherServlet.init();

        // then
        assertAll(
                () -> verify(handlerMapping1).initialize(),
                () -> verify(handlerMapping2).initialize()
        );
    }
}
