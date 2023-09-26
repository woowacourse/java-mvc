package com.techcourse.support.web.handler.adaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.handler.adaptor.HandlerAdaptors;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import java.util.List;
import java.util.Optional;

class HandlerAdaptorsTest {

    private static final HandlerAdaptors handlerAdaptors = new HandlerAdaptors(
            List.of(new AnnotationHandlerAdaptor())
    );

    @Test
    void testFindHandlerAdaptorWhenHandlerIsHandlerExecution() {
        //given
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        //when
        final Optional<HandlerAdaptor> optionalHandlerAdaptor = handlerAdaptors.findHandlerAdaptor(handlerExecution);

        //then
        assertThat(optionalHandlerAdaptor).isNotEmpty();
        assertThat(optionalHandlerAdaptor.get()).isInstanceOf(AnnotationHandlerAdaptor.class);
    }
}
