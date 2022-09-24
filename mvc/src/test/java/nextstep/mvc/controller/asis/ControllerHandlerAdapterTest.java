package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    @DisplayName("Controller 을 지원해줄 수 있다. (support() 결과가 true 이다.)")
    @Test
    void supportController() {
        // given
        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        final TestController testController = new TestController();

        // when
        final boolean result = controllerHandlerAdapter.supports(testController);

        // then
        assertThat(result).isTrue();
    }
    
    @DisplayName("Controller 를 통해서 요청을 처리할 수 있다.")
    @Test
    void handleController() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/test");
        when(request.getMethod()).thenReturn("GET");

        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        final TestController testController = new TestController();

        assertDoesNotThrow(() -> controllerHandlerAdapter.handle(request, response, testController));
    }

    class TestController implements Controller {

        @Override
        public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
            return "/test.jsp";
        }
    }
}
