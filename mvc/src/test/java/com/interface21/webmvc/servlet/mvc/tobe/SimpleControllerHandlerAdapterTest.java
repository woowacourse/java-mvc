package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimpleControllerHandlerAdapterTest {

    private SimpleControllerHandlerAdapter adapter;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        adapter = new SimpleControllerHandlerAdapter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @DisplayName("Controller 타입을 지원한다.")
    @Test
    void supports_Controller() {
        // given
        final Controller controller = (req, res) -> "";

        // when
        final boolean supports = adapter.supports(controller);

        // then
        assertThat(supports).isTrue();
    }

    @DisplayName("Controller가 아닌 타입은 지원하지 않는다.")
    @Test
    void supports_NotController() {
        // given
        final Object handler = new Object();

        // when
        final boolean supports = adapter.supports(handler);

        // then
        assertThat(supports).isFalse();
    }

    @DisplayName("Controller를 실행하고 결과를 ModelAndView로 변환한다.")
    @Test
    void handle() throws Exception {
        // given
        final String viewName = "/index.jsp";
        final Controller controller = (req, res) -> viewName;

        // when
        final ModelAndView modelAndView = adapter.handle(request, response, controller);

        // then
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
