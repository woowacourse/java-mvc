package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.tobe.adapter.AnnotationHandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class AnnotationHandlerAdapterTest {
    private final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
    private final Fixtures fixtures = new Fixtures();

    @DisplayName("매핑될 핸들러를 어노테이션 핸들러가 지원하는지 확인한다.")
    @Test
    void supports() {
        TestAnnotationController handler = new TestAnnotationController();
        HandlerExecution handlerExecution = new HandlerExecution(handler,
                fixtures.getHandlerMethod(handler, "findUserId"));
        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
    }

    @DisplayName("어노테이션 핸들러가 요청에 대한 ModelAndView 를 반환한다.")
    @Test
    void handle() throws Exception {
        final HttpServletRequest request = spy(HttpServletRequest.class);
        final HttpServletResponse response = spy(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Method findUserId = fixtures.getHandlerMethod(new TestAnnotationController(), "findUserId");
        HandlerExecution handlerExecution = new HandlerExecution(fixtures.getInstance(findUserId), findUserId);
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerExecution);

        JspView jspView = (JspView) modelAndView.getView();
        assertThat(jspView.getViewName()).isEqualTo("");
        assertThat(modelAndView.getModel()).usingRecursiveComparison()
                .comparingOnlyFields("id")
                .isEqualTo(Map.of("id", "gugu"));
    }
}
