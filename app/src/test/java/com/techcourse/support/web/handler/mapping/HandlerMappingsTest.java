package com.techcourse.support.web.handler.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.support.web.handler.adaptor.ManualHandlerMappingWrapped;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import java.util.List;
import java.util.Optional;

class HandlerMappingsTest {

    private static final HandlerMappings handlerMappings = new HandlerMappings(
            List.of(
                    new ManualHandlerMappingWrapped(),
                    new AnnotationHandlerMapping("com.techcourse"))
    );

    @BeforeAll
    static void setUp() {
        handlerMappings.initialize();
    }

    @Test
    void testGetHandler() {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register");

        //when
        final Optional<Object> optionalHandler = handlerMappings.getHandler(request);

        //then
        assertThat(optionalHandler).isNotEmpty();
        assertThat(optionalHandler.get()).isInstanceOf(Controller.class);
    }
}
