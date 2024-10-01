package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.NotFoundView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.ViewResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ViewResolversTest {

    private ViewResolvers viewResolvers = new ViewResolvers();

    @BeforeEach
    void setUp() {
        viewResolvers = new ViewResolvers();
    }

    @DisplayName("viewResolver가 null이면 예외를 발생시킨다.")
    @Test
    void givenNull_thenThrowNPE() {
        assertThatThrownBy(() -> viewResolvers.addViewResolvers(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("viewResolver가 존재하지 않습니다.");
    }

    @DisplayName("viewResolver를 주면 뷰 리졸버 목록에 넣는다.")
    @Test
    void givenViewResolver_thenAddResolver() {
        ViewResolver jsonViewResolver = new JsonViewResolver();

        viewResolvers.addViewResolvers(jsonViewResolver);
        assertThat(viewResolvers.size()).isEqualTo(1);
    }

    @DisplayName("viewName을 반환하면 그에 맞는 view를 반환한다.")
    @Test
    void givenViewName_thenFindView() {
        ViewResolver jsonViewResolver = new JsonViewResolver();
        viewResolvers.addViewResolvers(jsonViewResolver);
        View view = viewResolvers.resolveViewName("jsonView");

        assertThat(view).isInstanceOf(JsonView.class);
    }

    @DisplayName("존재하지 않는 ViewName 주면 예외를 발생시킨다.")
    @Test
    void givenInValidViewName_thenThrowException() {
        assertThatThrownBy(() -> viewResolvers.resolveViewName("invalid"))
                .isInstanceOf(NotFoundView.class)
                .hasMessage("viewName : invalid 에 해당하는 뷰가 존재하지 않습니다.");
    }
}
