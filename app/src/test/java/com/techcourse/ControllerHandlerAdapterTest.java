package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import samples.AnnotationTestController;
import samples.TestController;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class ControllerHandlerAdapterTest {

    private ControllerHandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new ControllerHandlerAdapter();
    }

    @DisplayName("ManualHandler를 지원한다.")
    @Test
    void supports() {
        // given
        Controller controller = new TestController();

        // when
        boolean actual = handlerAdapter.supports(controller);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("ManualHandler외의 handler는 지원하지 않는다.")
    @Test
    void cannotSupport() {
        // given
        AnnotationTestController controller = new AnnotationTestController();

        // when
        boolean actual = handlerAdapter.supports(controller);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("인자로 받은 handler를 수행하여 ModelAndView를 반환한다.")
    @Test
    void handle() throws Exception {
        // given
        Controller handler = new TestController();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        // then
        assertThat(modelAndView.getView()).isEqualTo(new JspView("/test.jsp"));
    }
}
