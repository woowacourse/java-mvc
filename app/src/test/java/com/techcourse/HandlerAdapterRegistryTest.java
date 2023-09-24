package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import com.techcourse.controller.AnnotatedControllerForTest;
import java.util.Set;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdaptor;
import webmvc.org.springframework.web.servlet.view.ViewAdapter;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry registry = new HandlerAdapterRegistry(
            Set.of(new ControllerHandlerAdaptor(new ViewAdapter()), new HandlerExecutionHandlerAdaptor())
    );

    @Test
    void 적절한_어댑터를_반환한다() {
        // given
        Object interfaceHandler = (Controller) (req, res) -> null;
        Object annotatedHandler = new AnnotatedControllerForTest();

        // when
        HandlerAdapter result1 = registry.getHandlerAdapter(interfaceHandler);
        HandlerAdapter result2 = registry.getHandlerAdapter(annotatedHandler);

        // then
        assertThat(result1).isInstanceOf(ControllerHandlerAdaptor.class);
        assertThat(result2).isInstanceOf(HandlerExecutionHandlerAdaptor.class);
    }
}
