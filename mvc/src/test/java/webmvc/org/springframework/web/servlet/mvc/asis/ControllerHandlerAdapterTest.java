package webmvc.org.springframework.web.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.HandlerAdapter;
import webmvc.org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ControllerHandlerAdapterTest {

    private final HandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

    @Test
    void 핸들러를_지원하는지_확인한다() {
        // given
        final Controller controller = new ForwardController("/hello");

        // expect
        assertThat(controllerHandlerAdapter.isSupport(controller)).isTrue();
    }

    @Test
    void 핸들러를_실행시킨다() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final Controller controller = new ForwardController("/hello");

        // when
        final ModelAndView modelAndView = controllerHandlerAdapter.handle(request, response, controller);

        // then
        assertThat(modelAndView.getView().getViewName()).isEqualTo("/hello");
    }
}
