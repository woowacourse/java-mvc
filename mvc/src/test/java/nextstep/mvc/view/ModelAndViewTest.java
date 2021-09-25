package nextstep.mvc.view;

import nextstep.mvc.exception.MvcException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ModelAndViewTest {

    @DisplayName("view의 이름을 가져온다 - 성공")
    @Test
    void getViewName() {
        // given
        String expected = "corgi";

        // when
        ModelAndView modelAndView = new ModelAndView(expected);
        String actual = modelAndView.getViewName();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("view의 이름을 가져온다 - 실패, view의 이름 대신 View 객체가 들어가 있음")
    @Test
    void getViewName_fail_viewInstanceGiven() {
        // given
        View view = new JspView("corgi");

        // when // then
        ModelAndView modelAndView = new ModelAndView(view);
        assertThatThrownBy(modelAndView::getViewName)
                .isInstanceOf(MvcException.class);
    }

    @DisplayName("view를 가져온다 - 성공")
    @Test
    void getView() {
        // given
        View expected = new JspView("corgi");

        // when
        ModelAndView modelAndView = new ModelAndView(expected);
        View actual = modelAndView.getView();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("view를 가져온다 - 실패, View 객체 대신 view 이름이 들어가 있음")
    @Test
    void getView_fail_viewNameGiven() {
        // given
        String viewName = "corgi";

        // when // then
        ModelAndView modelAndView = new ModelAndView(viewName);
        assertThatThrownBy(modelAndView::getView)
                .isInstanceOf(MvcException.class);
    }
}
