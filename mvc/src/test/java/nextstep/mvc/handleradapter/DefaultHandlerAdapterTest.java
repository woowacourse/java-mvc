package nextstep.mvc.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.ResourceHandler;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.ResourceView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class DefaultHandlerAdapterTest {

    private DefaultHandlerAdapter defaultHandlerAdapter;

    @BeforeEach
    void setUp() {
        defaultHandlerAdapter = new DefaultHandlerAdapter();
    }

    @DisplayName("Controller와 HandlerExecution, ResourceHandler를 지원")
    @Test
    void supports() throws NoSuchMethodException {
        final TestAnnotationController controller = new TestAnnotationController();
        final Method method = controller.getClass().getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        assertThat(defaultHandlerAdapter.supports(new HandlerExecution(controller, method))).isTrue();
        assertThat(defaultHandlerAdapter.supports(new ResourceHandler("abc.txt"))).isTrue();
        assertThat(defaultHandlerAdapter.supports(new ModelAndView(new JspView("")))).isFalse();
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

    @DisplayName("ResourceHandler 처리")
    @Test
    void handlerResourceHandler() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        // when
        final ModelAndView expected = new ModelAndView(new ResourceView("abc.txt"));
        final ModelAndView actual = defaultHandlerAdapter.handle(request, response, new ResourceHandler("abc.txt"));
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
