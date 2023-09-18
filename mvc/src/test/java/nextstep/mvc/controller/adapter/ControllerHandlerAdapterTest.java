package nextstep.mvc.controller.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ForwardController;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

    @Test
    void 지원_확인_테스트() {
        final Controller controller = new ForwardController("/gitchan");
        assertTrue(controllerHandlerAdapter.supports(controller));
    }

    @Test
    void 실행_테스트() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final Controller controller = new ForwardController("/gitchan");

        final ModelAndView view = controllerHandlerAdapter.handle(request, response, controller);

        assertEquals(view.getView().viewName(), "/gitchan");
    }
}
