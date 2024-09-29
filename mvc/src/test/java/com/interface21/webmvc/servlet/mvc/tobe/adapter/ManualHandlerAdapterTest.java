package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ManualHandlerAdapterTest {

    @Test
    @DisplayName("파라미터가 처리할 수 있는 handler라면 true를 반환한다.")
    void supports() {
        Object handlerExecution = mock(Controller.class);

        assertTrue(new ManualHandlerAdapter().supports(handlerExecution));
    }

    @Test
    @DisplayName("파라미터가 처리할 수 없는 handler라면 false를 반환한다.")
    void supports_WhenCanNot() {
        Object handlerExecution = mock(HandlerExecution.class);

        assertFalse(new ManualHandlerAdapter().supports(handlerExecution));
    }
}
