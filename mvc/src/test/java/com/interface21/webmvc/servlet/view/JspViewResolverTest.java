package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.View;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewResolverTest {

    @DisplayName("viewName이 .jsp로 끝나면 JsonView를 반환한다.")
    @Test
    void givenViewNameEndJsp_thenReturnJspView() {
        JspViewResolver jspViewResolver = new JspViewResolver();
        View view = jspViewResolver.resolveViewName("hello.jsp");

        assertThat(view).isInstanceOf(JspView.class);
    }

    @DisplayName("viewName이 .jsp로 끝나지 않으면 null을 반환한다.")
    @Test
    void givenViewNameNotEndJsp_thenReturnJspView() {
        JspViewResolver jspViewResolver = new JspViewResolver();
        View view = jspViewResolver.resolveViewName("hello");

        assertThat(view).isNull();
    }
}
