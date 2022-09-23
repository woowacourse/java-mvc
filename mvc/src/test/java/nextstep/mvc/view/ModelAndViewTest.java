package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ModelAndViewTest {

    @DisplayName(value = "render 호출 시 View의 render()가 호출됨")
    @Test
    void render() throws Exception {
        // given
        final View view = mock(View.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ModelAndView modelAndView = new ModelAndView(view);

        // when
        modelAndView.render(request, response);

        // then
        verify(view).render(new HashMap<>(), request, response);
    }
}
