package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    private ControllerHandlerAdapter handlerAdapter;

    @BeforeEach
    public void setUp() {
        handlerAdapter = new ControllerHandlerAdapter();
    }

    @DisplayName("컨트롤러 인스턴스를 지원한다.")
    @Test
    void support_true() {
        // given
        Controller controller = mock(Controller.class);

        // when
        boolean actual = handlerAdapter.support(controller);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("컨트롤러 인스턴스가 아니면 지원하지 않는다.")
    @Test
    void support_false() {
        // given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        // when
        boolean actual = handlerAdapter.support(handlerExecution);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("ModelAndView를 반환한다.")
    @Test
    void handle() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller controller = mock(Controller.class);

        String expectedViewName = "testView";
        when(controller.execute(request, response)).thenReturn(expectedViewName);

        // when
        ModelAndView actual = handlerAdapter.handle(request, response, controller);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getView()).isInstanceOf(JspView.class);
    }
}
