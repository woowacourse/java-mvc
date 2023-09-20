package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new AnnotationHandlerAdapter();
    }

    @Test
    void handle() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var testController = new TestController();
        final var method = testController.getClass().getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final var handlerExecution = new HandlerExecution(testController, method);

        when(request.getAttribute("id")).thenReturn("maco");

        // when
        final ModelAndView result = handlerAdapter.handle(request, response, handlerExecution);

        // then
        assertThat(result.getView())
                .usingRecursiveComparison()
                .isEqualTo(new JspView(""));
        assertThat(result.getObject("id")).isEqualTo("maco");
    }

    @Test
    void supports() throws Exception {
        // given
        final var testController = new TestController();
        final var method = testController.getClass().getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final var handlerExecution = new HandlerExecution(testController, method);

        // when
        final boolean result = handlerAdapter.supports(handlerExecution);

        // then
        assertThat(result).isTrue();
    }
}
