package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @DisplayName("지원하는 핸들러가 있다.")
    @Test
    void isSupportsTest() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter(handlerMapping);

        // when, then
        assertThat(handlerAdapter.isSupports(request)).isTrue();
    }

    @DisplayName("지원하는 핸들러가 없다.")
    @Test
    void isSupportsTest1() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("DELETE");

        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter(handlerMapping);

        // when, then
        assertThat(handlerAdapter.isSupports(request)).isFalse();
    }

    @DisplayName("어뎁터가 핸들러를 실행하고 ModelAndView를 얻는다.")
    @Test
    void handleTest() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter(handlerMapping);

        // when
        ModelAndView modelAndView = handlerAdapter.handle(request, response);

        // then
        assertAll(
            () -> assertThat(modelAndView.getView()).isEqualTo(new JspView("")),
            () -> assertThat(modelAndView.getModel().containsKey("id")).isTrue()
        );
    }
}
