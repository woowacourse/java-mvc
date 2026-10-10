package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.RedirectView;
import org.junit.jupiter.api.Test;

class UrlBasedViewResolverTest {

    private final UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();

    @Test
    void resolveRedirectView() {
        assertThat(viewResolver.resolveViewName("redirect:/index.jsp")).isInstanceOf(RedirectView.class);
    }

    @Test
    void resolveJspView() {
        assertThat(viewResolver.resolveViewName("/register.jsp")).isInstanceOf(JspView.class);
    }
}
