package nextstep.mvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.RegisterController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ManualHandlerAdapterTest {
    private ManualHandlerAdapter manualHandlerAdapter;

    @BeforeEach
    void setUp() {
        manualHandlerAdapter = new ManualHandlerAdapter();
    }

    @Test
    void support() {
        final RegisterController registerController = new RegisterController();
        boolean supports = manualHandlerAdapter.supports(registerController);

        assertThat(supports).isTrue();
    }

    @Test
    void handle() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final RegisterController registerController = new RegisterController();

        ModelAndView modelAndView = manualHandlerAdapter.handle(request, response, registerController);
        assertThat(modelAndView.getView()).usingRecursiveComparison()
                .isEqualTo(new JspView("redirect:/index.jsp"));
    }
}
