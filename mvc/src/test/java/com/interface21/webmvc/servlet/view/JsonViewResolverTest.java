package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.ViewResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewResolverTest {

    @DisplayName("뷰 이름이 jsonView면, JsonView를 반환한다.")
    @Test
    void givenJsonPrefix_thenReturnJsonView() {
        ViewResolver viewResolver = new JsonViewResolver();
        View view = viewResolver.resolveViewName("jsonView");

        assertThat(view).isInstanceOf(JsonView.class);
    }

    @DisplayName("뷰 이름이 jsonView가 아니면 null을 반환한다.")
    @Test
    void givenNotJsonPrefix_thenReturnNull() {
        ViewResolver viewResolver = new JsonViewResolver();
        View view = viewResolver.resolveViewName("jspView");

        assertThat(view).isNull();
    }
}
