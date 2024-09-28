package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.OldController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ControllerHandlerAdapterTest {

    @Test
    @DisplayName("기존 Controller를 변경하지 않으면서 HandlerExecution 역할을 할 수 있도록 한다.")
    void handle() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        HandlerExecution handlerExecution = new ControllerHandlerAdapter(new OldController());
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        JspView view = (JspView) modelAndView.getView();
        assertThat(view.getViewName()).isEqualTo("viewName");
    }
}
