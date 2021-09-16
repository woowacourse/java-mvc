package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ModelAndViewTest {

    private static final TestUser USER = new TestUser("jihye", "mazzi");

    @DisplayName("값을 추가하고 확인한다.")
    @Test
    void addObjectAndGetObject() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("user", USER);

        Object user = mv.getObject("user");

        assertThat(user).isEqualTo(USER);
    }

    @DisplayName("뷰 이름을 확인한다.")
    @Test
    void getViewName() {
        String expected = "view.jsp";
        ModelAndView mv = new ModelAndView(expected);

        String actual = mv.getView();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("뷰 이름이 없을 경우 null이 반환된다.")
    @Test
    void getObject() {
        ModelAndView mv = new ModelAndView();

        String actual = mv.getView();

        assertThat(actual).isNull();
    }

    @DisplayName("모델 객체를 확인한다.")
    @Test
    void getModel() {
        ModelAndView mv = new ModelAndView();
        Map<String, Object> expected = Map.of(
            "user", USER
        );
        mv.addObject("user", USER);

        Map<String, Object> actual = mv.getModel();

        assertThat(actual).containsAllEntriesOf(expected);
    }
}