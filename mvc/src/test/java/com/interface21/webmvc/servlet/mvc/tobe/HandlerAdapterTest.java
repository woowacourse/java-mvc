package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterTest {

    @Test
    @DisplayName("기존 컨트롤러의 뷰 경로를 JspView로 변환하여 포워드한다")
    void adaptsLegacyControllerViewNameToJspView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        Controller controller = (req, res) -> "/legacy.jsp";
        when(request.getRequestDispatcher("/legacy.jsp")).thenReturn(requestDispatcher);

        HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("기존 컨트롤러의 redirect: 경로를 JspView로 변환하여 리다이렉트한다")
    void adaptsLegacyControllerRedirectViewNameToJspView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller controller = (req, res) -> "redirect:/login";

        HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(response).sendRedirect("/login");
    }
}
