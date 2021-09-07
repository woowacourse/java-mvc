package nextstep.mvc.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;
import samples.TestManualController;

class DefaultHandlerAdapterTest {

    private DefaultHandlerAdapter defaultHandlerAdapter;

    @BeforeEach
    void setUp() {
        defaultHandlerAdapter = new DefaultHandlerAdapter();
    }

    @DisplayName("Controller와 HandlerExecution 핸들러를 지원")
    @Test
    void supports() throws NoSuchMethodException {
        final TestAnnotationController controller = new TestAnnotationController();
        final Method method = controller.getClass().getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        assertThat(defaultHandlerAdapter.supports(new TestManualController())).isTrue();
        assertThat(defaultHandlerAdapter.supports(new HandlerExecution(controller, method))).isTrue();
        assertThat(defaultHandlerAdapter.supports(new ModelAndView(new JspView("")))).isFalse();
    }

    @DisplayName("Controller 처리")
    @Test
    void handleController() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final ModelAndView expected = new ModelAndView(new JspView("I'm not groot."));

        final ModelAndView actual = defaultHandlerAdapter.handle(request, response, new TestManualController());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("HandlerExecution 처리")
    @Test
    void handleHandlerExecution() throws Exception {
        final TestAnnotationController controller = new TestAnnotationController();
        final Method method = controller.getClass().getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        final ModelAndView expected = new ModelAndView(new JspView(""));
        expected.addObject("id", request.getAttribute("id"));

        final ModelAndView actual = defaultHandlerAdapter.handle(request, response, new HandlerExecution(controller, method));
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}