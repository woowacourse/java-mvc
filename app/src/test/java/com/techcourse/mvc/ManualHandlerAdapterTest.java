package com.techcourse.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class ManualHandlerAdapterTest {

    private ManualHandlerAdapter manualHandlerAdapter;

    @BeforeEach
    void setUp() {
        manualHandlerAdapter = new ManualHandlerAdapter();
    }

    @Test
    void handle() {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var handler = new TestController();

        // when
        final ModelAndView result = manualHandlerAdapter.handle(request, response, handler);

        // then
        assertThat(result.getView())
                .usingRecursiveComparison()
                .isEqualTo(new JspView("/test.jsp"));
    }

    @Test
    void supports() {
        // given
        final var handler = new TestController();

        // when
        final boolean supports = manualHandlerAdapter.supports(handler);

        // then
        assertThat(supports).isTrue();
    }
}
