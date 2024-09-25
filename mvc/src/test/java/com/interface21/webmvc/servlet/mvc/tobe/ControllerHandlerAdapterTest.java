package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class ControllerHandlerAdapterTest {

    @Test
    @DisplayName("Controller로 작성된 핸들러를 실행해 ModelAndView로 변환한다.")
    void convertControllerReturnValueToModelAndView() throws Exception {
        Controller controller = Mockito.mock(Controller.class);
        Mockito.when(controller.execute(any(HttpServletRequest.class), any(HttpServletResponse.class)))
                .thenReturn("test");
        ControllerHandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        ModelAndView mav = handlerAdapter.handle(request, response, controller);

        assertThat(mav).isNotNull()
                .extracting(ModelAndView::getView)
                .isInstanceOf(JspView.class)
                .hasFieldOrPropertyWithValue("viewName", "test");
    }
}
