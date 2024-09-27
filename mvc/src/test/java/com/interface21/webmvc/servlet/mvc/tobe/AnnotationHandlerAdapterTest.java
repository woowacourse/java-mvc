package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.handlerAdapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlerMapping.HandlerExecution;

import samples.TestController;

class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("컨트롤러 어노테이션을 지원하는지 확인한다.")
    void supoort() {
        var sut = new AnnotationHandlerAdapter();
        var handlerExecutor = mock(HandlerExecution.class);

        assertThat(sut.support(handlerExecutor)).isTrue();
    }

    @Test
    @DisplayName("컨트롤러 어노테이션이 아니면 지원하지 않는다.")
    void notSupport() {
        var sut = new AnnotationHandlerAdapter();
        var controller = mock(Controller.class);

        assertThat(sut.support(controller)).isFalse();
    }

    @Test
    @DisplayName("request를 처리하여 ModelAndView를 반환한다.")
    void handle() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        var sut = new AnnotationHandlerAdapter();
        var controller = new TestController();
        var method = controller.getClass().getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        var handler = new HandlerExecution(controller, method);

        // when
        var actual = sut.handle(request, response, handler);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(ModelAndView.class)
        );
    }
}
