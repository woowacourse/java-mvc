package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ViewFactoryTest {

    @Test
    void createJspView() {
        ViewFactory viewFactory = new ViewFactory();
        assertThat(viewFactory.createView("index.jsp")).isInstanceOf(JspView.class);
    }

    @Test
    void createJsonView() {
        ViewFactory viewFactory = new ViewFactory();
        assertThat(viewFactory.createView("{\"name\" : \"huni\"}")).isInstanceOf(JsonView.class);
    }
}
