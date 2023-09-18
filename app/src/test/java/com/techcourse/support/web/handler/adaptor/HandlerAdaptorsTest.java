package com.techcourse.support.web.handler.adaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

class HandlerAdaptorsTest {

    private static final HandlerAdaptors handlerAdaptors = new HandlerAdaptors();

    @Test
    void testFindHandlerAdaptorWhenHandlerIsHandlerExecution() {
        //given
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        //when
        final HandlerAdaptor handlerAdaptor = handlerAdaptors.findHandlerAdaptor(handlerExecution);

        //then
        assertThat(handlerAdaptor).isInstanceOf(AnnotationHandlerAdaptor.class);
    }

    @Test
    void testFindHandlerAdaptorWhenHandlerIsController() {
        //given
        final Controller controller = mock(Controller.class);

        //when
        final HandlerAdaptor handlerAdaptor = handlerAdaptors.findHandlerAdaptor(controller);

        //then
        assertThat(handlerAdaptor).isInstanceOf(ManualHandlerAdaptor.class);
    }
}
