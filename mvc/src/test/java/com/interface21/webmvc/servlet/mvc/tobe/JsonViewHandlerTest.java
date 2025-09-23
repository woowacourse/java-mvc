package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.view.JsonView;
import com.interface21.webmvc.servlet.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewHandlerTest {

    private AnnotationHandlerMapping handlerMapping;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("모델에 객체가 하나일 경우 JsonView를 반환한다.")
    void singleObjectReturnsJsonView() throws Exception {
        // given
        when(request.getServletPath()).thenReturn("/json/single");
        when(request.getMethod()).thenReturn("GET");

        // when
        final HandlerExecution handler = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handler.handle(request, response);

        // then
        assertAll(
                () -> assertThat(modelAndView.getView()).isInstanceOf(JsonView.class),
                () -> assertThat(modelAndView.getModel()).hasSize(1),
                () -> assertThat(modelAndView.getObject("user"))
                        .extracting("name").isEqualTo("gugu")
        );
    }

    @Test
    @DisplayName("모델에 객체가 여러 개일 경우에도 JsonView를 반환한다.")
    void multipleObjectsReturnJsonView() throws Exception {
        // given
        when(request.getServletPath()).thenReturn("/json/multiple");
        when(request.getMethod()).thenReturn("GET");

        // when
        final HandlerExecution handler = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handler.handle(request, response);

        // then
        assertAll(
                () -> assertThat(modelAndView.getView()).isInstanceOf(JsonView.class),
                () -> assertThat(modelAndView.getModel()).hasSize(2),
                () -> assertThat(modelAndView.getObject("user1")).extracting("name").isEqualTo("gugu"),
                () -> assertThat(modelAndView.getObject("user2")).extracting("name").isEqualTo("sully")
        );
    }
}
