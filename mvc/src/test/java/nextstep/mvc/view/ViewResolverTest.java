package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ViewResolverTest {

    @DisplayName("render를 진행한다")
    @Test
    void resolve() throws Exception {
        TestView testView = new TestView();
        ModelAndView modelAndView = new ModelAndView(testView);
        ViewResolver.resolve(modelAndView, null, null);

        assertThat(testView.isCalled()).isTrue();
    }

    public static class TestView implements View {

        private boolean isCalled;

        @Override
        public void render(Map<String, ?> model, HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
            this.isCalled = true;
        }

        public boolean isCalled() {
            return isCalled;
        }
    }
}
