package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new AnnotationHandlerAdapter();
    }

    @DisplayName("처리할 수 있는 핸들러인 경우 true를 반환한다.")
    @Test
    void 처리할_수_있는_핸들러인_경우_true를_반환한다() throws NoSuchMethodException {
        // given
        MockAnnotationController mockAnnotationController = new MockAnnotationController();
        Class<?> controllerClazz = mockAnnotationController.getClass();
        Method method = controllerClazz.getMethod("mock", HttpServletRequest.class, HttpServletResponse.class);

        HandlerExecution handlerExecution = new HandlerExecution(method);

        // when
        boolean actual = handlerAdapter.supports(handlerExecution);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("처리할 수 없는 핸들러인 경우 false를 반환한다.")
    @Test
    void 처리할_수_없는_핸들러인_경우_false를_반환한다() {
        // given
        String unhandledHandler = "";

        // when
        boolean actual = handlerAdapter.supports(unhandledHandler);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("처리 가능한 핸들러를 핸들하면 ModelAndView 객체를 반환한다.")
    @Test
    void 처리_가능한_핸들러를_핸들하면_ModelAndView_객체를_반환한다() throws Exception {
        // given
        MockAnnotationController mockAnnotationController = new MockAnnotationController();
        Class<?> controllerClazz = mockAnnotationController.getClass();
        Method method = controllerClazz.getMethod("mock", HttpServletRequest.class, HttpServletResponse.class);

        HandlerExecution handlerExecution = new HandlerExecution(method);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        ModelAndView actual = handlerAdapter.handle(request, response, handlerExecution);

        // then
        assertThat(actual.getView()).isInstanceOf(JspView.class);
    }
}
