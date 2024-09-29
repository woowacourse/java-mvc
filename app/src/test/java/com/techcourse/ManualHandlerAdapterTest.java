package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    @DisplayName("적절한 ModelAndView를 반환한다.")
    @Test
    void handle() {
        // given
        RegisterViewController registerViewController = new RegisterViewController();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
        String expectedViewName = "/register.jsp";

        // when
        ModelAndView actualModelAndView = manualHandlerAdapter.handle(request, response, registerViewController);
        String actualViewName = ((JspView) actualModelAndView.getView()).viewName;

        // then
        assertThat(actualViewName).isEqualTo(expectedViewName);
    }
}
