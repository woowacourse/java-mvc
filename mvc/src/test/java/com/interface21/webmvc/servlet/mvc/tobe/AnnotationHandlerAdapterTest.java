package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.handlerAdapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlerMapping.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;

import samples.TestController;

class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("컨트롤러 어노테이션을 지원하는지 확인한다.")
    void supoort()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz = TestController.class;
        Method[] methods = clazz.getDeclaredMethods();
        var sut = new AnnotationHandlerAdapter();

        var handlerExecutor = new HandlerExecution(clazz.getConstructor().newInstance(), methods[0]);

        assertThat(sut.support(handlerExecutor)).isTrue();
    }

    @Test
    @DisplayName("컨트롤러 어노테이션이 아니면 지원하지 않는다.")
    void notSupport() {
        var sut = new AnnotationHandlerAdapter();
        var controller = new Controller() {
            @Override
            public String execute(HttpServletRequest req, HttpServletResponse res) {
                return "/test";
            }
        };

        assertThat(sut.support(controller)).isFalse();
    }

    @Test
    @DisplayName("request를 처리하여 ModelAndView를 반환한다.")
    void handle() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        var sut = new AnnotationHandlerAdapter();
        var controller = new TestController();
        var method = controller.getClass().getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        var handler = new HandlerExecution(controller, method);

        // when
        var actual = sut.handle(request, response, handler);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getView()).isInstanceOf(JspView.class)
        );
    }

}
