package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ControllerHandlerAdapterTest {

    @Test
    void Controller_구현체는_true를_반환한다() {
        HandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

        boolean expect = controllerHandlerAdapter.isSupport(new ForwardController(""));

        assertThat(expect).isTrue();
    }

    @Test
    void HandlerExecution_구현체는_false를_반환한다() throws Exception {
        HandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        var handlerExecution = new HandlerExecution(null, null);

        boolean expect = controllerHandlerAdapter.isSupport(handlerExecution);

        assertThat(expect).isFalse();
    }

    @Test
    void Controller_구현체의_execute_메서드를_호출한다() throws Exception {
        HandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        var controller = new Controller() {
            @Override
            public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
                return "/index.jsp";
            }
        };
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        var modelAndView = controllerHandlerAdapter.handle(request, response, controller);

        assertThat(modelAndView).isNotNull();
    }
}
