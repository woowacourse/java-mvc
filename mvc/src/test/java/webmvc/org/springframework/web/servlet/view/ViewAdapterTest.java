package webmvc.org.springframework.web.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.View;

class ViewAdapterTest {

    private final ViewAdapter viewAdapter = new ViewAdapter();

    @Test
    void viewName으로_View_구현체를_가져온다() {
        // given
        String viewName = "redirect:/index.jsp";

        // when
        View view = viewAdapter.getView(viewName);

        // then
        assertThat(view).isInstanceOf(JspView.class);
    }
}
