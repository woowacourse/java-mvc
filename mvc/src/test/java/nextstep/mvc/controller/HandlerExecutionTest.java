package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionTest {

    @Test
    void 요청받은_메서드를_인스턴스가_invoke_하도록_한다() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");

        final var executor = new TestController();
        final var method = TestController.class.getMethod("findUserId", HttpServletRequest.class,
                HttpServletResponse.class);

        // when
        final var execution = new HandlerExecution(executor, method);

        // then
        final ModelAndView modelAndView = execution.handle(request, response);
        final Map<String, Object> model = modelAndView.getModel();

        assertThat(model).containsEntry("id", "gugu");
    }
}
