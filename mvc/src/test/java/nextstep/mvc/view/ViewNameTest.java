package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ViewNameTest {

    @DisplayName("입력 문자열로 시작하는 ViewName인지 확인한다.")
    @Test
    void startsWith() {
        // given
        final ViewName redirectHome = ViewName.REDIRECT_HOME;

        // when- then
        assertThat(redirectHome.startsWith("redirect:/")).isTrue();
    }

    @DisplayName("입력 문자열로 시작하는 ViewName인지 확인한다.")
    @Test
    void startsWithFalse() {
        // given
        final ViewName redirectHome = ViewName.HOME;

        // when- then
        assertThat(redirectHome.startsWith("redirect:/")).isFalse();
    }

    @DisplayName("substring을 가져온다.")
    @Test
    void substring() {
        // given
        final ViewName redirectHome = ViewName.REDIRECT_HOME;

        // when- then
        assertThat(redirectHome.substring("redirect:".length())).isEqualTo("/");
    }
}
