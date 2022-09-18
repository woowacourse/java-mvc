package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class ModelAndViewTest {

    @Test
    void model에_attribute_값을_넣는다() {
        View view = mock(View.class);
        ModelAndView modelAndView = new ModelAndView(view);

        modelAndView.addObject("attribute", "value");

        assertThat(modelAndView.getObject("attribute")).isEqualTo("value");
    }

    @Test
    void render는_modelAndView가_가진_view의_render를_호출한다() throws Exception {
        View view = mock(View.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ModelAndView modelAndView = new ModelAndView(view);

        modelAndView.render(request, response);

        verify(view).render(modelAndView.getModel(), request, response);
    }
}
