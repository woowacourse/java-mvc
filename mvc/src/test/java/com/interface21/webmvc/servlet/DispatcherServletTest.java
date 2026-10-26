package com.interface21.webmvc.servlet;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

class DispatcherServletTest {

    @Test
    void 주입된_핸들러_매핑을_순서대로_초기화한다() {
        // given
        HandlerMapping first = mock(HandlerMapping.class);
        HandlerMapping second = mock(HandlerMapping.class);
        DispatcherServlet servlet = new DispatcherServlet(List.of(first, second));

        // when
        servlet.init();

        // then
        InOrder initializationOrder = inOrder(first, second);
        initializationOrder.verify(first).initialize();
        initializationOrder.verify(second).initialize();
    }
}
