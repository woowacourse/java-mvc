package nextstep.mvc.viewresolver;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JspViewResolverTest {

    private JspViewResolver jspViewResolver;

    @BeforeEach
    void setUp() {
        jspViewResolver = new JspViewResolver();
    }

    @DisplayName("Jsp 관련 뷰인지 확인한다.")
    @Test
    void supports() {
        boolean isSupport = jspViewResolver.supports("index.jsp");
        boolean isNotSupport = jspViewResolver.supports("index.htm");

        assertThat(isSupport).isTrue();
        assertThat(isNotSupport).isFalse();
    }

    @DisplayName("뷰 이름으로 뷰 객체를 반환한다.")
    @Test
    void resolveViewName() {
        View view = jspViewResolver.resolveViewName("index.jsp");

        assertThat(view).isInstanceOf(JspView.class);
    }
}