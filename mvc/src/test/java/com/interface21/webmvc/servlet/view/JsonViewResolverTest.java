package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.ViewResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewResolverTest {

    @DisplayName("뷰 이름이 json:으로 시작하면 JsonView를 반환한다.")
    @Test
    void givenJsonPrefix_thenReturnJsonView() {
        ViewResolver viewResolver = new JsonViewResolver();
        View view = viewResolver.resolveViewName("json:api");

        assertThat(view).isInstanceOf(JsonView.class);
    }

    @DisplayName("뷰 이름이 json:으로 시작하지 않으면 null을 반환한다.")
    @Test
    void givenNotJsonPrefix_thenReturnNull() {
        ViewResolver viewResolver = new JsonViewResolver();
        View view = viewResolver.resolveViewName("view.json");

        assertThat(view).isNull();
    }

    @DisplayName("뷰 이름이 null이면 NPE를 던진다.")
    @Test
    void givenNull_thenThrowNPE() {
        ViewResolver viewResolver = new JsonViewResolver();

        assertThatThrownBy(() -> viewResolver.resolveViewName(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("viewName의 값이 null입니다.");
    }
}
