package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.controller.Controller;
import com.interface21.webmvc.servlet.mvc.controller.InterfaceControllerAdaptor;
import com.interface21.webmvc.servlet.mvc.handler.AnnotationControllerAdaptor;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdaptorsTest {

    private HandlerAdaptors handlerAdaptors;

    @BeforeEach
    void setUp() {
        handlerAdaptors = new HandlerAdaptors();
    }

    @DisplayName("해당 핸들러를 처리할 수 있는 어댑터를 찾아서 반환한다.")
    @Test
    void findHandler() {
        Controller mockInterfaceHandler = mock(Controller.class);
        HandlerExecution mockAnnotationHandler = mock(HandlerExecution.class);

        assertAll(
                () -> assertThat(handlerAdaptors.findAdaptor(mockInterfaceHandler))
                        .isInstanceOf(InterfaceControllerAdaptor.class),
                () -> assertThat(handlerAdaptors.findAdaptor(mockAnnotationHandler))
                        .isInstanceOf(AnnotationControllerAdaptor.class)
        );
    }

    @DisplayName("해당 핸들러를 처리할 어댑터가 없을 때 예외를 발생시킨다.")
    @Test
    void findHandlerWhenNotExist() {
        Object unknownHandler = new Object();

        assertThatThrownBy(() -> handlerAdaptors.findAdaptor(unknownHandler));
    }
}
