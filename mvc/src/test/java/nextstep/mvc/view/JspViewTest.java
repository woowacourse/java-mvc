package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JspViewTest {

    @DisplayName("viewName 으로 생성한다.")
    @ValueSource(strings = {"/login", "/login.jsp"})
    @ParameterizedTest
    void create(String input) {
        String expected = "/login.jsp";

        JspView actual = new JspView(input);

        assertThat(actual.getViewName()).isEqualTo(expected);
    }

    @DisplayName("viewName 이 다른 파일 형식이면 예외를 반환한다.")
    @ValueSource(strings = {"login.js", "login.html", "login.css", "login.png"})
    @ParameterizedTest
    void createFail(String input) {
        assertThatThrownBy(() -> new JspView(input))
            .isInstanceOf(IllegalArgumentException.class);
    }
}