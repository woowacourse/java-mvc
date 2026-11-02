package com.interface21.webmvc.servlet.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    private final ManualHandlerAdapter adapter = new ManualHandlerAdapter();

    @Test
    void supportsLegacyController() {
        assertThat(adapter.support(mock(Controller.class))).isTrue();
        assertThat(adapter.support(new Object())).isFalse();
    }

    @Test
    void convertsLegacyControllerViewNameToModelAndView() throws Exception {
        final Controller controller = mock(Controller.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(controller.execute(request, response)).thenReturn("redirect:/index.jsp");

        final ModelAndView modelAndView = adapter.handle(request, response, controller);

        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
        assertThat(modelAndView.getModel()).isEmpty();
    }
}
