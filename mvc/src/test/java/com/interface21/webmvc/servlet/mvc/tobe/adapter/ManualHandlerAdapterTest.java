package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.ManualHandlerAdapter;

import samples.TestManualController;

class ManualHandlerAdapterTest {

    @Nested
    @DisplayName("지원하는 핸들러인지 판단")
    class Supports {

        @Test
        @DisplayName("성공 : 지원하는 핸들러(Controller)는 true 반환")
        void supportsTrue() {
            ManualHandlerAdapter adapter = new ManualHandlerAdapter();

            boolean actual = adapter.supports(new TestManualController());

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("성공 : 지원하는 핸들러는 false 반환")
        void supportsFalse() {
            ManualHandlerAdapter adapter = new ManualHandlerAdapter();

            boolean actual = adapter.supports("not exists");

            assertThat(actual).isFalse();
        }
    }

    @Test
    @DisplayName("핸들러 실행")
    void handle() throws Exception {
        ManualHandlerAdapter adapter = new ManualHandlerAdapter();
        Controller controller = mock(Controller.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(controller.execute(request, response)).thenReturn("/");

        ModelAndView actual = adapter.handle(request, response, controller);

        ModelAndView expected = new ModelAndView(new JspView("/"));
        assertThat(actual.getView()).isEqualTo(expected.getView());
    }
}
