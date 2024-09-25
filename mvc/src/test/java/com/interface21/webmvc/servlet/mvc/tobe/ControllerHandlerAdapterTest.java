package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class ControllerHandlerAdapterTest {

    @Test
    @DisplayName("핸들러를 호출해 뷰 이름으로 JspView 만들어 반환한다.")
    void handle() throws Exception {
        // given
        HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        Controller loginViewController = (req, res) -> "/login.jsp";

        // when
        ModelAndView modelAndView = handlerAdapter.handle(
                new MockHttpServletRequest(),
                new MockHttpServletResponse(),
                loginViewController);

        // then
        assertThat(modelAndView.getModel()).isEmpty();
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
