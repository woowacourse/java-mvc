package webmvc.org.springframework.web.servlet.mvc.view;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JspViewTest {

    @Test
    void 생성자는_유효한_값을_전달하면_JspView를_초기화한다() {
        assertDoesNotThrow(() -> new JspView("/hello.jsp"));
    }

    @Test
    void 생성자는_유효하지_않은_viewName을_전달하면_예외가_발생한다() {
        final String invalidViewName = null;

        assertThatThrownBy(() -> new JspView(invalidViewName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이동할 페이지를 입력해주세요.");
    }

    @Test
    void 생성자는_jsp_접미사가_없는_viewName을_전달하면_예외가_발생한다() {
        final String notJspViewName = "hello.html";

        assertThatThrownBy(() -> new JspView(notJspViewName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("올바르지 않은 jsp 이름입니다.");
    }
}
