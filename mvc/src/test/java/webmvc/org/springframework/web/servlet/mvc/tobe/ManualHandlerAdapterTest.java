package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

class ManualHandlerAdapterTest {

    private ManualHandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new ManualHandlerAdapter();
    }

    @Test
    void 핸들러가_Controller_타입이면_true를_반환한다() {
        // given
        final Controller controller = mock(Controller.class);

        // when
        final boolean result = handlerAdapter.supports(controller);

        // thend
        assertThat(result).isTrue();
    }

    @Test
    void 핸들러가_Controller_타입이_아니면_false를_반환한다() {
        // given
        final String controller = "Controller";

        // when
        final boolean result = handlerAdapter.supports(controller);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 인자로_들어온_핸들러의_메소드를_어댑터를_통해_실행한다() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handlerMapping = mock(HandlerMapping.class);

        final TestController handler = new TestController();
        when(handlerMapping.getHandler(request)).thenReturn(handler);

        // when
        final ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);

        // then
        assertThat(modelAndView.getView()).isEqualTo("test.jsp");
    }

    static class TestController implements Controller {

        @Override
        public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
            return "test.jsp";
        }
    }
}
