package com.interface21.context.container;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;

import samples.TestApp;
import samples.TestController;
import samples.TestController2;
import samples.TestHandlerAdapter;
import samples.TestHandlerMapping;

class ContainerTest {

    @Test
    @DisplayName("Scan all mvc and controller instances.")
    void getInstancesOf() {
        // given
        var sut = Container.getInstance();

        // when
        Container.run(TestApp.class);

        // then
        List<Object> tobeControllers = sut.getInstancesAnnotatedOf(com.interface21.context.stereotype.Controller.class);
        List<HandlerMapping> mappings = sut.getInstancesOf(HandlerMapping.class);
        List<HandlerAdapter> adapters = sut.getInstancesOf(HandlerAdapter.class);
        assertAll(
                () -> assertThat(tobeControllers)
                        .hasSize(2)
                        .hasOnlyElementsOfTypes(TestController.class, TestController2.class),
                () -> assertThat(mappings)
                        .hasSize(2)
                        .hasOnlyElementsOfTypes(AnnotationHandlerMapping.class, TestHandlerMapping.class),
                () -> assertThat(adapters)
                        .hasSize(2)
                        .hasOnlyElementsOfTypes(AnnotationHandlerAdapter.class, TestHandlerAdapter.class)
        );
    }
}
