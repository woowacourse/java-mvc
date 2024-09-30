package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;

class ViewResolverTest {

    @Test
    @DisplayName("response content type이 application/json이면 jsonView를 반환한다.")
    void render_json_view() {
        // given
        final ViewResolver viewResolver = new ViewResolver(new ModelAndView(new JsonView()));
        final HttpServletResponse response = mock(HttpServletResponse.class);
        given(response.getContentType())
                .willReturn(MediaType.APPLICATION_JSON_UTF8_VALUE);

        // when
        final View resolve = viewResolver.resolve(response);

        // then
        assertThat(resolve).isInstanceOf(JsonView.class);
    }

    @Test
    @DisplayName("response content type이 application/json이 아니면 jspView를 반환한다.")
    void render_jsp_view() {
        // given
        final ViewResolver viewResolver = new ViewResolver(new ModelAndView(new JspView("/index.jsp")));
        final HttpServletResponse response = mock(HttpServletResponse.class);
        given(response.getContentType())
                .willReturn("JSP");

        // when
        final View resolve = viewResolver.resolve(response);

        // then
        assertThat(resolve).isInstanceOf(JspView.class);
    }
}
