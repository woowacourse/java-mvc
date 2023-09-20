package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManualHandlerMappingTest {

    @Mock
    HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 요청에_맞는_handler_를_반환한다() {
        // given
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        given(request.getRequestURI())
                .willReturn("/login");

        // when
        Controller handler = manualHandlerMapping.getHandler(request);

        // then
        assertThat(Controller.class.isAssignableFrom(handler.getClass())).isTrue();
    }
}
