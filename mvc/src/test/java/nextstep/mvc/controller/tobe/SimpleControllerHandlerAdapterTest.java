package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimpleControllerHandlerAdapterTest {

    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new SimpleControllerHandlerAdapter();
    }

    @DisplayName("처리할 수 있는 핸들러인 경우 true를 반환한다.")
    @Test
    void 처리할_수_있는_핸들러인_경우_true를_반환한다() {
        // given
        Controller controller = new MockController();

        // when
        boolean actual = handlerAdapter.supports(controller);

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
        MockController controller = new MockController();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/mock");

        // when
        ModelAndView actual = handlerAdapter.handle(request, response, controller);

        // then
        assertThat(actual.getView()).isInstanceOf(JspView.class);
    }
}
