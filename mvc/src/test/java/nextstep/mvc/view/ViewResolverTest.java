package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ViewResolverTest {

    @DisplayName("Render를 수행할 수 있다.")
    @Test
    void resolve() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        TestView testView = new TestView();
        ModelAndView modelAndView = new ModelAndView(testView);
        ViewResolver.resolve(request, response, modelAndView);

        // then
        assertThat(testView.isInvoked()).isTrue();
    }

    private class TestView implements View {

        private boolean isInvoked;

        @Override
        public void render(final Map<String, ?> model,
                           final HttpServletRequest request,
                           final HttpServletResponse response) {
            this.isInvoked = true;
        }

        public boolean isInvoked() {
            return isInvoked;
        }
    }
}
