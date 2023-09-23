package com.techcourse.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ControllerHandlerMappingTest {

    @Test
    void 요청에_해당하는_컨트롤러를_반환한다() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        ControllerHandlerMapping controllerHandlerMapping = new ControllerHandlerMapping();

        controllerHandlerMapping.initialize();
        when(httpServletRequest.getRequestURI()).thenReturn("/");

        assertThat(controllerHandlerMapping.getHandler(httpServletRequest)).isNotNull();
    }

    @Test
    void 요청에_해당하는_컨트롤러가_없으면_null_반환한다() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        ControllerHandlerMapping controllerHandlerMapping = new ControllerHandlerMapping();

        controllerHandlerMapping.initialize();
        when(httpServletRequest.getRequestURI()).thenReturn("notFound");

        assertThat(controllerHandlerMapping.getHandler(httpServletRequest)).isNull();
    }
}
